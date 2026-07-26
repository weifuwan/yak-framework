/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 */
package com.yak.security.service;

import com.yak.security.common.PagingData;
import com.yak.security.common.dto.resource.AssignToManyUserDTO;
import com.yak.security.common.dto.resource.AssignToOneUserDTO;
import com.yak.security.common.dto.resource.BatchAssignDTO;
import com.yak.security.common.dto.resource.ControlLevelQueryDTO;
import com.yak.security.common.dto.resource.MByRDataQueryDTO;
import com.yak.security.common.dto.resource.MByRQueryDTO;
import com.yak.security.common.dto.resource.MByUDataQueryDTO;
import com.yak.security.common.dto.resource.MByUQueryDTO;
import com.yak.security.common.dto.resource.UserResourceQueryDTO;
import com.yak.security.common.enums.resource.ControlLevelCode;
import com.yak.security.common.vo.resource.MByRDataVO;
import com.yak.security.common.vo.resource.MByRVO;
import com.yak.security.common.vo.resource.MByUDataVO;
import com.yak.security.common.vo.resource.MByUVO;
import com.yak.security.exception.LogiSecurityException;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface UserResourceService {
    public int getResourceCntByUserId(Integer var1, UserResourceQueryDTO var2);

    public PagingData<MByRVO> getManageByResourcePage(MByRQueryDTO var1) throws LogiSecurityException;

    public PagingData<MByUVO> getManageByUserPage(MByUQueryDTO var1);

    public void assignResourcePermission(AssignToOneUserDTO var1) throws LogiSecurityException;

    public void assignResourcePermission(AssignToManyUserDTO var1, HttpServletRequest var2) throws LogiSecurityException;

    public void batchAssignResourcePermission(BatchAssignDTO var1, HttpServletRequest var2) throws LogiSecurityException;

    public List<MByUDataVO> getManagerByUserDataList(MByUDataQueryDTO var1) throws LogiSecurityException;

    public List<MByRDataVO> getManagerByResourceDataList(MByRDataQueryDTO var1) throws LogiSecurityException;

    public boolean getViewPermissionControlStatus();

    public void changeResourceViewControlStatus();

    public ControlLevelCode getControlLevel(ControlLevelQueryDTO var1) throws LogiSecurityException;
}

