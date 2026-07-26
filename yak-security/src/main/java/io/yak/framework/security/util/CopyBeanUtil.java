package io.yak.framework.security.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

/**
 * Bean 属性复制工具。
 *
 * <p>统一处理单个对象、集合及分页对象之间的同名属性转换。
 *
 * @author weifuwan
 */
public final class CopyBeanUtil {
  private CopyBeanUtil() { throw new IllegalStateException("Utility class"); }

  public static <T> T copy(Object source, Class<T> target) {
    if (source == null || target == null) {
      return null;
    }
    try {
      T newInstance = target.getDeclaredConstructor().newInstance();
      BeanUtils.copyProperties(source, newInstance);
      return newInstance;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T, K> List<K> copyList(List<T> source, Class<K> target) {
    if (null == source || source.isEmpty()) {
      return Collections.emptyList();
    }
    return source.stream()
        .map(e -> CopyBeanUtil.copy(e, target))
        .collect(Collectors.toList());
  }

  public static <T, K> List<K> copyList(List<T> source, Class<K> target,
                                        Consumer<K> consumer) {
    if (null == source || source.isEmpty()) {
      return Collections.emptyList();
    }
    return source.stream()
        .map(e -> CopyBeanUtil.copy(e, target))
        .peek(consumer)
        .collect(Collectors.toList());
  }

  public static <T, K> IPage<K> copyPage(IPage<T> source, Class<K> target) {
    if (source == null || target == null) {
      return null;
    }
    Page<K> targetPage = new Page<>();
    BeanUtils.copyProperties(source, targetPage);
    targetPage.setTotal(source.getTotal());
    targetPage.setRecords(CopyBeanUtil.copyList(source.getRecords(), target));
    return targetPage;
  }

  public static <T, K> IPage<K> copyPageExcludeList(IPage<T> source) {
    if (source == null) {
      return null;
    }
    return (IPage)CopyBeanUtil.copy(source, Page.class);
  }
}
