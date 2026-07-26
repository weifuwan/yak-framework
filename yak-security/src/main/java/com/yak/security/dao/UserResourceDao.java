/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.dao;

import com.yak.security.common.dto.resource.ControlLevelQueryDTO;
import com.yak.security.common.dto.resource.UserResourceQueryDTO;
import com.yak.security.common.entity.UserResource;
import com.yak.security.common.enums.resource.ControlLevelCode;

import java.util.List;

public interface UserResourceDao {
    public int selectCountByUserId(Integer var1, UserResourceQueryDTO var2);

    public void deleteByUserId(Integer var1, UserResourceQueryDTO var2);

    public void deleteByControlLevel(ControlLevelCode var1);

    public void insert(UserResource var1);

    public void insertBatch(List<UserResource> var1);

    public void deleteByUserIdList(List<Integer> var1, UserResourceQueryDTO var2);

    public void deleteByProjectIdList(List<Integer> var1, UserResourceQueryDTO var2);

    public void deleteByResourceTypeIdList(List<Integer> var1, UserResourceQueryDTO var2);

    public void deleteByResourceIdList(List<Integer> var1, UserResourceQueryDTO var2);

    public int selectCountByUserIdAndControlLevel(Integer var1, ControlLevelCode var2);

    public int selectCount(UserResourceQueryDTO var1);

    public List<Integer> selectResourceIdListByUserId(Integer var1, UserResourceQueryDTO var2);

    public void deleteWithoutUserIdList(UserResourceQueryDTO var1, List<Integer> var2);

    public void deleteByUserIdWithoutProjectIdList(Integer var1, UserResourceQueryDTO var2, List<Integer> var3);

    public void deleteByUserIdWithoutResourceTypeIdList(Integer var1, UserResourceQueryDTO var2, List<Integer> var3);

    public int selectCountGroupByUserId(UserResourceQueryDTO var1);

    public List<Integer> selectUserIdListGroupByUserId(UserResourceQueryDTO var1);

    public Integer selectControlLevel(ControlLevelQueryDTO var1);
}

