package io.yak.framework.security.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;

class DatabaseNumberUtilsTest {
  @Test
  void convertsDriverNumberImplementationsWithoutNarrowing() {
    List<Number> values = List.of(
        Integer.valueOf(1), Long.valueOf(1L), BigInteger.ONE, BigDecimal.ONE);

    assertEquals(List.of(1L, 1L, 1L, 1L),
        values.stream().map(DatabaseNumberUtils::toLong).toList());
  }

  @Test
  void preservesLongIdBoundariesAndNull() {
    List<Long> ids = List.of(1L, 2147483647L, 2147483648L, Long.MAX_VALUE);
    assertEquals(ids, ids.stream().map(DatabaseNumberUtils::toLong).toList());
    assertNull(DatabaseNumberUtils.toLong(null));
  }

  @Test
  void rejectsNonNumericValues() {
    assertThrows(IllegalStateException.class, () -> DatabaseNumberUtils.toLong("1"));
  }

  @Test
  void checkedLongToIntegerConversionRejectsOverflow() {
    assertEquals(List.of(1, 2), List.of(1L, 2L).stream().map(Math::toIntExact).toList());
    assertThrows(ArithmeticException.class, () -> Math.toIntExact(2147483648L));
  }
}
