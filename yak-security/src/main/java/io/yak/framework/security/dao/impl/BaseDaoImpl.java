package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.properties.YakSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDaoImpl<T> {
  @Autowired protected YakSecurityProperties yakSecurityProperties;

  protected QueryWrapper<T> getQueryWrapperWithAppName() {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq((Object) "app_name",
                    (Object)this.yakSecurityProperties.getAppName());
    return queryWrapper;
  }
}
