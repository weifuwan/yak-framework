/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 */
package com.yak.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yak.security.common.dto.resource.type.ResourceTypeQueryDTO;
import com.yak.security.common.entity.ResourceType;

import java.util.List;

public interface ResourceTypeDao {
    public List<ResourceType> selectAll();

    public IPage<ResourceType> selectPage(ResourceTypeQueryDTO var1);

    public ResourceType selectByResourceTypeId(Integer var1);

    public void insertBatch(List<ResourceType> var1);
}

