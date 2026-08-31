package io.yak.framework.security.controller.v1;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 锁定消息中心的新旧 HTTP 契约，避免后续重构再次产生前后端路由漂移。 */
class MessageControllerContractTest {

  @Test
  void controllerKeepsLegacyAndNewMessageRoutes() {
    RequestMapping root = MessageController.class.getAnnotation(RequestMapping.class);
    assertNotNull(root);
    assertTrue(Arrays.asList(root.value())
            .contains("/yak-security/api/v1/message"));

    assertGet("list", "/list");
    assertGet("page", "/page");
    assertGet("detail", "/detail");
    assertGet("unreadCount", "/unread-count");
    assertPost("markRead", "/mark-read");
    assertPost("batchRead", "/batch-read");
    assertPut("switchStatus", "/switch");
  }

  private static void assertGet(String methodName, String path) {
    GetMapping mapping = method(methodName).getAnnotation(GetMapping.class);
    assertNotNull(mapping, methodName);
    assertTrue(Arrays.asList(mapping.value()).contains(path), methodName);
  }

  private static void assertPost(String methodName, String path) {
    PostMapping mapping = method(methodName).getAnnotation(PostMapping.class);
    assertNotNull(mapping, methodName);
    assertTrue(Arrays.asList(mapping.value()).contains(path), methodName);
  }

  private static void assertPut(String methodName, String path) {
    PutMapping mapping = method(methodName).getAnnotation(PutMapping.class);
    assertNotNull(mapping, methodName);
    assertTrue(Arrays.asList(mapping.value()).contains(path), methodName);
  }

  private static Method method(String name) {
    return Arrays.stream(MessageController.class.getDeclaredMethods())
            .filter(candidate -> candidate.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Missing controller method: " + name));
  }
}
