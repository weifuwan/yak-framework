/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.springframework.util.StringUtils
 *  org.springframework.web.context.request.RequestContextHolder
 *  org.springframework.web.context.request.ServletRequestAttributes
 */
package com.didiglobal.logi.security.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpRequestUtil {
    public static final String USER_ID = "X-SSO-USER-ID";
    public static final String USER = "X-SSO-USER";
    public static final String PROJECT_ID = "X-LOGI-SECURITY-PROJECT-ID";
    public static final Integer REDIRECT_CODE = 401;
    public static final Integer COOKIE_OR_SESSION_MAX_AGE_UNIT_SEC = 86400;

    private HttpRequestUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getHeaderValue(String headerKey) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(headerKey);
    }

    public static String getOperator() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return HttpRequestUtil.getOperator(request);
    }

    public static String getOperator(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String operator = (String)session.getAttribute(USER);
        if (StringUtils.isEmpty((Object)operator)) {
            return HttpRequestUtil.getOperatorFromHeader(request);
        }
        return operator;
    }

    public static String getOperatorFromHeader(HttpServletRequest request) {
        String operator = request.getHeader(USER);
        if (StringUtils.isEmpty((Object)operator)) {
            return "";
        }
        return operator;
    }

    public static Integer getOperatorId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userIdStr = session.getAttribute(USER_ID);
        Integer id = HttpRequestUtil.strConvertInteger(String.valueOf(userIdStr));
        if (id == null) {
            return HttpRequestUtil.getOperatorIdFromHeader(request);
        }
        return id;
    }

    public static Integer getOperatorIdFromHeader(HttpServletRequest request) {
        Integer id = HttpRequestUtil.strConvertInteger(request.getHeader(USER_ID));
        if (id == null) {
            return -1;
        }
        return id;
    }

    public static Integer getProjectId(HttpServletRequest request, int defaultAppid) {
        String projectIdStr = request.getHeader(PROJECT_ID);
        if (StringUtils.isEmpty((Object)projectIdStr)) {
            return defaultAppid;
        }
        return HttpRequestUtil.strConvertInteger(projectIdStr);
    }

    public static Integer getProjectId(HttpServletRequest request) {
        String projectIdStr = request.getHeader(PROJECT_ID);
        if (StringUtils.isEmpty((Object)projectIdStr)) {
            return null;
        }
        return HttpRequestUtil.strConvertInteger(projectIdStr);
    }

    private static Integer strConvertInteger(String str) {
        try {
            return StringUtils.isEmpty((Object)str) ? null : Integer.valueOf(str);
        } catch (Exception ignore) {
            return null;
        }
    }
}

