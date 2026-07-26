/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JavaType
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.type.CollectionType
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeanUtils
 */
package com.yak.job.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertTo(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T tgt = null;
        try {
            tgt = targetClass.newInstance();
            BeanUtils.copyProperties((Object) source, tgt);
        } catch (Exception e) {
            logger.warn("convert obj2Obj error||msg={}", (Object) e.getMessage(), (Object) e);
        }
        return tgt;
    }

    public static <T> List<T> convertToList(String source, Class<T> targetClass) {
        try {
            CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
            return (List) objectMapper.readValue(source, (JavaType) javaType);
        } catch (Exception e) {
            logger.error("", (Throwable) e);
            return null;
        }
    }

    public static String convertToJson(Object source) {
        try {
            return source == null ? null : objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            logger.error("source to json error, e->", (Throwable) e);
            return null;
        }
    }
}

