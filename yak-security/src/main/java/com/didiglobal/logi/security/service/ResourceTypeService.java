/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.service;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.dto.resource.type.ResourceTypeQueryDTO;
import com.didiglobal.logi.security.common.vo.resource.ResourceTypeVO;
import java.util.List;

public interface ResourceTypeService {
    public List<ResourceTypeVO> getAllResourceTypeList();

    public List<Integer> getAllResourceTypeIdList();

    public PagingData<ResourceTypeVO> getResourceTypePage(ResourceTypeQueryDTO var1);

    public ResourceTypeVO getResourceTypeByResourceTypeId(Integer var1);

    public void saveResourceType(List<String> var1);
}

