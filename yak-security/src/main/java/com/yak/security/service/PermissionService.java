/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.service;

import com.yak.security.common.dto.permission.PermissionDTO;
import com.yak.security.common.vo.permission.PermissionTreeVO;

import java.util.List;

public interface PermissionService {
    public PermissionTreeVO buildPermissionTreeWithHas(List<Integer> var1);

    public PermissionTreeVO buildPermissionTree();

    public PermissionTreeVO buildPermissionTreeByRoleId(Integer var1);

    public void savePermission(List<PermissionDTO> var1);
}

