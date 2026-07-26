package io.yak.framework.security.util;

/** Utilities for normalizing numeric scalar values returned by database drivers. */
public final class DatabaseNumberUtils {
  private DatabaseNumberUtils() {}

  /**
   * Converts a nullable database numeric value to {@link Long}.
   *
   * @param value scalar value returned by the database
   * @return the long value, or {@code null} when the input is {@code null}
   * @throws IllegalStateException when the driver returned a non-numeric value
   */
  public static Long toLong(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Number) {
      return ((Number) value).longValue();
    }
    throw new IllegalStateException(
        "数据库 ID 查询结果不是数字类型，实际类型=" + value.getClass().getName());
  }
}
