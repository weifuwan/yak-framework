/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Mapper
 */
package com.yak.security.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yak.security.common.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper
extends BaseMapper<UserPO> {
}

