package io.yak.framework.security.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * 安全模块统一的 JSON 转换工具，避免业务层绑定到特定的旧序列化实现。
 */
public final class JsonUtils {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JsonUtils() {}

  public static String toJson(Object value) {
    try {
      return OBJECT_MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      throw new IllegalArgumentException("JSON 序列化失败", exception);
    }
  }

  public static <T> T fromJson(String value, Class<T> type) {
    try {
      return OBJECT_MAPPER.readValue(value, type);
    } catch (JsonProcessingException exception) {
      throw new IllegalArgumentException("JSON 反序列化失败", exception);
    }
  }

  public static <T> List<T> toList(String value, Class<T> elementType) {
    JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(
        List.class, elementType);
    try {
      return OBJECT_MAPPER.readValue(value, type);
    } catch (JsonProcessingException exception) {
      throw new IllegalArgumentException("JSON 数组反序列化失败", exception);
    }
  }
}
