/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.didiglobal.logi.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.didiglobal.logi.security.common.dto.resource.type.ResourceTypeQueryDTO;
import com.didiglobal.logi.security.common.entity.ResourceType;
import com.didiglobal.logi.security.common.po.ResourceTypePO;
import com.didiglobal.logi.security.dao.ResourceTypeDao;
import com.didiglobal.logi.security.dao.impl.BaseDaoImpl;
import com.didiglobal.logi.security.dao.mapper.ResourceTypeMapper;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class ResourceTypeDaoImpl
extends BaseDaoImpl<ResourceTypePO>
implements ResourceTypeDao {
    @Autowired
    private ResourceTypeMapper resourceTypeMapper;

    @Override
    public List<ResourceType> selectAll() {
        return CopyBeanUtil.copyList(this.resourceTypeMapper.selectList(null), ResourceType.class);
    }

    @Override
    public IPage<ResourceType> selectPage(ResourceTypeQueryDTO queryDTO) {
        Page pageInfo = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        String typeName = queryDTO.getTypeName();
        queryWrapper.like(!StringUtils.isEmpty((Object)typeName), (Object)"type_name", (Object)typeName);
        this.resourceTypeMapper.selectPage((IPage)pageInfo, (Wrapper)queryWrapper);
        return CopyBeanUtil.copyPage(pageInfo, ResourceType.class);
    }

    @Override
    public ResourceType selectByResourceTypeId(Integer resourceTypeId) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object)"id", (Object)resourceTypeId);
        return CopyBeanUtil.copy(this.resourceTypeMapper.selectOne((Wrapper)queryWrapper), ResourceType.class);
    }

    @Override
    public void insertBatch(List<ResourceType> resourceTypeList) {
        if (CollectionUtils.isEmpty(resourceTypeList)) {
            return;
        }
        List<ResourceTypePO> resourceTypePOList = CopyBeanUtil.copyList(resourceTypeList, ResourceTypePO.class);
        for (ResourceTypePO resourceTypePO : resourceTypePOList) {
            resourceTypePO.setAppName(this.logiSecurityProper.getAppName());
            this.resourceTypeMapper.insert(resourceTypePO);
        }
    }
}

