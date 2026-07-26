package io.yak.framework.security.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathUtil {
  public static long getRandomNumber(int len) {
    if (len <= 0 || len > 18) {
      return 0L;
    }
    return (long)((Math.random() + 1.0) * Math.pow(10.0, len));
  }

  public static Set<Long> getIntersection(List<Long> list1,
                                             List<Long> list2) {
    HashSet<Long> result = new HashSet<Long>();
    HashSet<Long> set = new HashSet<Long>(list2);
    for (Integer num : list1) {
      if (!set.contains(num))
        continue;
      result.add(num);
    }
    return result;
  }

  private MathUtil() {}
}
