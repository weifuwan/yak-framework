/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.dao;

import com.didiglobal.logi.security.common.dto.user.UserProjectDTO;
import com.didiglobal.logi.security.common.entity.UserProject;
import com.didiglobal.logi.security.common.po.UserProjectPO;
import java.util.List;

public interface UserProjectDao {
    public List<Integer> selectUserIdListByProjectId(Integer var1, int var2);

    public List<Integer> selectProjectIdListByUserIdList(List<Integer> var1);

    public List<UserProjectPO> selectProjectListByUserIdList(List<Integer> var1);

    public void insertBatch(List<UserProject> var1);

    public int deleteUserProject(List<UserProject> var1);

    public void deleteByProjectId(Integer var1);

    public void deleteByProjectIdAndUserType(Integer var1, int var2);

    public List<UserProject> selectByProjectIds(List<Integer> var1);

    public List<UserProject> select(UserProjectDTO var1);
}

