/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.didiglobal.logi.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.didiglobal.logi.security.properties.LogiSecurityProper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDaoImpl<T> {
    @Autowired
    protected LogiSecurityProper logiSecurityProper;

    protected QueryWrapper<T> getQueryWrapperWithAppName() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"app_name", (Object)this.logiSecurityProper.getAppName());
        return queryWrapper;
    }
}

