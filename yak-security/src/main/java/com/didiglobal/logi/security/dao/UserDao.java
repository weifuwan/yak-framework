/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 */
package com.didiglobal.logi.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.didiglobal.logi.security.common.dto.user.UserBriefQueryDTO;
import com.didiglobal.logi.security.common.dto.user.UserQueryDTO;
import com.didiglobal.logi.security.common.entity.user.User;
import com.didiglobal.logi.security.common.entity.user.UserBrief;
import com.didiglobal.logi.security.common.po.UserPO;
import java.util.List;

public interface UserDao {
    public int addUser(UserPO var1) throws Exception;

    public int editUser(UserPO var1) throws Exception;

    public IPage<User> selectPageByUserIdList(UserQueryDTO var1, List<Integer> var2);

    public IPage<UserBrief> selectBriefPageByDeptIdList(UserBriefQueryDTO var1, List<Integer> var2);

    public User selectByUserId(Integer var1);

    public User selectByUserMail(String var1);

    public User selectByUserPhone(String var1);

    public boolean deleteByUserId(Integer var1);

    public List<UserBrief> selectBriefListByUserIdList(List<Integer> var1);

    public List<UserBrief> selectBriefListByNameAndDescOrderByCreateTime(String var1);

    public List<UserBrief> selectBriefListByDeptIdList(List<Integer> var1);

    public List<UserBrief> selectBriefListOrderByCreateTime(boolean var1);

    public List<UserBrief> selectAllBriefList();

    public List<Integer> selectUserIdListByUsernameOrRealName(String var1);

    public User selectByUsername(String var1);
}

