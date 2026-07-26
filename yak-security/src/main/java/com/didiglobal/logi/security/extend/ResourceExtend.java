/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.extend;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.dto.resource.ResourceDTO;
import java.util.List;

public interface ResourceExtend {
    public PagingData<ResourceDTO> getResourcePage(Integer var1, Integer var2, String var3, int var4, int var5);

    public List<ResourceDTO> getResourceList(Integer var1, Integer var2);

    public int getResourceCnt(Integer var1, Integer var2);
}

