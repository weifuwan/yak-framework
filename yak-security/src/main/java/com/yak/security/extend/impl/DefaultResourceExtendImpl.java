/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Component
 */
package com.yak.security.extend.impl;

import com.yak.security.common.PagingData;
import com.yak.security.common.dto.resource.ResourceDTO;
import com.yak.security.extend.ResourceExtend;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component(value="logiSecurityDefaultResourceExtendImpl")
public class DefaultResourceExtendImpl
implements ResourceExtend {
    @Override
    public PagingData<ResourceDTO> getResourcePage(Integer projectId, Integer resourceTypeId, String resourceName, int page, int size) {
        return null;
    }

    @Override
    public List<ResourceDTO> getResourceList(Integer projectId, Integer resourceTypeId) {
        return new ArrayList<ResourceDTO>();
    }

    @Override
    public int getResourceCnt(Integer projectId, Integer resourceTypeId) {
        return 0;
    }
}

