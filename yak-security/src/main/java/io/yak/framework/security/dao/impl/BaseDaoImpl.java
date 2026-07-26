package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Compatibility base for DAO implementations not yet migrated to lambda wrappers.
 * Application isolation is enforced centrally by the tenant interceptor, so the
 * compatibility wrapper deliberately adds no duplicate app-name predicate.
 */
public abstract class BaseDaoImpl<T> {
  protected QueryWrapper<T> getQueryWrapperWithAppName() {
    return new QueryWrapper<>();
  }
}
