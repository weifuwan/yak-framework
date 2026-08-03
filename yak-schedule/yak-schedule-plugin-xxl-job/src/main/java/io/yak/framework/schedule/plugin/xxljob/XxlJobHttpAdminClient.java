package io.yak.framework.schedule.plugin.xxljob;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ScheduleProviderException;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 面向 XXL-JOB 3.4 官方管理端表单接口的默认客户端。
 *
 * <p>官方未提供稳定的任务管理 OpenAPI，因此该客户端被刻意隔离在插件内部，
 * 应用可以声明自己的 {@link XxlJobAdminClient} Bean 覆盖它。</p>
 */
public final class XxlJobHttpAdminClient implements XxlJobAdminClient {
    private final XxlJobPluginProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private volatile boolean loggedIn;

    public XxlJobHttpAdminClient(
            XxlJobPluginProperties properties,
            ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        CookieManager cookieManager = new CookieManager(
                null, CookiePolicy.ACCEPT_ALL);
        this.httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .connectTimeout(Duration.ofSeconds(
                        Math.max(1, properties.getConnectTimeoutSeconds())))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    @Override
    public String create(Map<String, String> form) {
        JsonNode response = postAuthenticated("/jobinfo/insert", form);
        return response.path("data").asText();
    }

    @Override
    public void update(String id, Map<String, String> form) {
        Map<String, String> values = new LinkedHashMap<>(form);
        values.put("id", id);
        postAuthenticated("/jobinfo/update", values);
    }

    @Override
    public void delete(String id) {
        postAuthenticated("/jobinfo/delete", Map.of("ids[]", id));
    }

    @Override
    public void pause(String id) {
        postAuthenticated("/jobinfo/stop", Map.of("ids[]", id));
    }

    @Override
    public void resume(String id) {
        postAuthenticated("/jobinfo/start", Map.of("ids[]", id));
    }

    @Override
    public void trigger(String id, String executorParam) {
        postAuthenticated("/jobinfo/trigger", Map.of(
                "id", id,
                "executorParam", executorParam == null ? "" : executorParam,
                "addressList", ""));
    }

    @Override
    public Optional<XxlJobAdminTask> find(String id, int jobGroupId) {
        JsonNode response = postAuthenticated("/jobinfo/pageList", Map.of(
                "offset", "0",
                "pagesize", "1000",
                "jobGroup", String.valueOf(jobGroupId),
                "triggerStatus", "-1",
                "name", "",
                "executorHandler", XxlJobScheduleEngine.HANDLER_NAME,
                "author", ""));
        JsonNode rows = response.path("data").path("data");
        if (!rows.isArray()) {
            return Optional.empty();
        }
        for (JsonNode row : rows) {
            if (id.equals(row.path("id").asText())) {
                return Optional.of(new XxlJobAdminTask(
                        id,
                        row.path("triggerStatus").asInt(),
                        row.path("triggerLastTime").asLong(),
                        row.path("triggerNextTime").asLong()));
            }
        }
        return Optional.empty();
    }

    private JsonNode postAuthenticated(
            String path,
            Map<String, String> form) {
        ensureLogin();
        HttpResponse<String> response = send(path, form);
        if (isAuthenticationRedirect(response)) {
            synchronized (this) {
                loggedIn = false;
            }
            ensureLogin();
            response = send(path, form);
        }
        return requireSuccess(path, response);
    }

    private synchronized void ensureLogin() {
        if (loggedIn) {
            return;
        }
        HttpResponse<String> response = send("/auth/doLogin", Map.of(
                "userName", value(properties.getUsername()),
                "password", value(properties.getPassword()),
                "ifRemember", "on"));
        requireSuccess("/auth/doLogin", response);
        loggedIn = true;
    }

    private HttpResponse<String> send(
            String path,
            Map<String, String> form) {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri(path))
                    .timeout(Duration.ofSeconds(
                            Math.max(1, properties.getRequestTimeoutSeconds())))
                    .header(
                            "Content-Type",
                            "application/x-www-form-urlencoded;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(encode(form)))
                    .build();
            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ScheduleProviderException(
                    "XXL-JOB Admin request interrupted: " + path,
                    exception);
        } catch (IOException exception) {
            throw new ScheduleProviderException(
                    "XXL-JOB Admin request failed: " + path,
                    exception);
        }
    }

    private JsonNode requireSuccess(
            String path,
            HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ScheduleProviderException(
                    "XXL-JOB Admin HTTP " + response.statusCode()
                            + " for " + path);
        }
        try {
            JsonNode json = objectMapper.readTree(response.body());
            if (json.path("code").asInt(-1) != 200) {
                throw new ScheduleProviderException(
                        "XXL-JOB Admin rejected " + path + ": "
                                + json.path("msg").asText("unknown error"));
            }
            return json;
        } catch (ScheduleProviderException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ScheduleProviderException(
                    "Invalid XXL-JOB Admin response for " + path,
                    exception);
        }
    }

    private boolean isAuthenticationRedirect(HttpResponse<String> response) {
        return response.statusCode() == 301
                || response.statusCode() == 302
                || response.statusCode() == 303;
    }

    private URI uri(String path) {
        String base = properties.getAdminAddress().trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return URI.create(base + path);
    }

    private String encode(Map<String, String> form) {
        return form.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(value(entry.getValue())))
                .collect(Collectors.joining("&"));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}
