/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.security.service.impl;

import com.didiglobal.logi.security.common.dto.permission.PermissionDTO;
import com.didiglobal.logi.security.common.entity.Permission;
import com.didiglobal.logi.security.common.enums.ResultCode;
import com.didiglobal.logi.security.common.vo.permission.PermissionTreeVO;
import com.didiglobal.logi.security.dao.PermissionDao;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.service.PermissionService;
import com.didiglobal.logi.security.service.RolePermissionService;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import com.didiglobal.logi.security.util.MathUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="logiSecurityPermissionServiceImpl")
public class PermissionServiceImpl
implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionService rolePermissionService;

    private PermissionTreeVO buildPermissionTree(Set<Integer> permissionHasSet) throws LogiSecurityException {
        List<Permission> permissionList = this.permissionDao.selectAllAndAscOrderByLevel();
        PermissionTreeVO root = PermissionTreeVO.builder().leaf(false).has(true).id(0).childList(new ArrayList<PermissionTreeVO>()).build();
        HashMap<Integer, PermissionTreeVO> parentMap = new HashMap<Integer, PermissionTreeVO>(permissionList.size());
        parentMap.put(0, root);
        for (Permission permission : permissionList) {
            PermissionTreeVO parent;
            PermissionTreeVO permissionTreeVO = CopyBeanUtil.copy(permission, PermissionTreeVO.class);
            if (permission.getLeaf() != null && !permission.getLeaf().booleanValue()) {
                permissionTreeVO.setChildList(new ArrayList<PermissionTreeVO>());
            }
            if ((parent = (PermissionTreeVO)parentMap.get(permission.getParentId())) == null) {
                throw new LogiSecurityException(ResultCode.PERMISSION_DATA_ERROR);
            }
            permissionTreeVO.setHas(parent.getHas() != false && permissionHasSet.contains(permission.getId()));
            parent.getChildList().add(permissionTreeVO);
            parentMap.put(permissionTreeVO.getId(), permissionTreeVO);
        }
        return root;
    }

    @Override
    public PermissionTreeVO buildPermissionTreeWithHas(List<Integer> permissionHasList) {
        PermissionTreeVO permissionTreeVO = null;
        try {
            permissionTreeVO = this.buildPermissionTree(new HashSet<Integer>(permissionHasList));
        } catch (LogiSecurityException e) {
            e.printStackTrace();
        }
        return permissionTreeVO;
    }

    @Override
    public PermissionTreeVO buildPermissionTree() {
        return this.buildPermissionTreeWithHas(new ArrayList<Integer>());
    }

    @Override
    public PermissionTreeVO buildPermissionTreeByRoleId(Integer roleId) {
        List<Integer> permissionIdList = this.rolePermissionService.getPermissionIdListByRoleId(roleId);
        return this.buildPermissionTreeWithHas(permissionIdList);
    }

    @Override
    public void savePermission(List<PermissionDTO> permissionDTOList) {
        if (CollectionUtils.isEmpty(permissionDTOList)) {
            return;
        }
        ArrayList<Permission> permissionList = new ArrayList<Permission>();
        HashMap<PermissionDTO, Integer> permissionDTOMap = new HashMap<PermissionDTO, Integer>();
        LinkedList<PermissionDTO> queue = new LinkedList<PermissionDTO>();
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setChildPermissionDTOList(permissionDTOList);
        queue.offer(permissionDTO);
        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                PermissionDTO dto = (PermissionDTO)queue.poll();
                if (dto == null) continue;
                Permission permission = CopyBeanUtil.copy(dto, Permission.class);
                permission.setLevel(level);
                permission.setParentId((Integer)permissionDTOMap.get(dto));
                permission.setLeaf(CollectionUtils.isEmpty(dto.getChildPermissionDTOList()));
                permission.setId(0);
                if (level > 0) {
                    permission.setId((int)this.getPermissionId());
                    permissionList.add(permission);
                }
                for (PermissionDTO temp : dto.getChildPermissionDTOList()) {
                    permissionDTOMap.put(temp, permission.getId());
                    queue.offer(temp);
                }
            }
            ++level;
        }
        this.permissionDao.insertBatch(permissionList);
    }

    private long getPermissionId() {
        return System.currentTimeMillis() % 1000L * (long)Math.pow(10.0, 5.0) + MathUtil.getRandomNumber(5);
    }
}

