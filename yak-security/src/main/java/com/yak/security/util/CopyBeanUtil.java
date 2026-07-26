/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.springframework.beans.BeanUtils
 */
package com.yak.security.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

public class CopyBeanUtil {
    private CopyBeanUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            T newInstance = target.newInstance();
            BeanUtils.copyProperties((Object)source, newInstance);
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
        return source.stream().map(e -> CopyBeanUtil.copy(e, target)).collect(Collectors.toList());
    }

    public static <T, K> List<K> copyList(List<T> source, Class<K> target, Consumer<K> consumer) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> CopyBeanUtil.copy(e, target)).peek(consumer).collect(Collectors.toList());
    }

    public static <T, K> IPage<K> copyPage(IPage<T> source, Class<K> target) {
        if (source == null || target == null) {
            return null;
        }
        IPage newInstance = (IPage)CopyBeanUtil.copy(source, Page.class);
        if (newInstance != null) {
            newInstance.setTotal(source.getTotal());
            newInstance.setRecords(CopyBeanUtil.copyList(source.getRecords(), target));
        }
        return newInstance;
    }

    public static <T, K> IPage<K> copyPageExcludeList(IPage<T> source) {
        if (source == null) {
            return null;
        }
        return (IPage)CopyBeanUtil.copy(source, Page.class);
    }
}

