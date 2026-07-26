/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 */
package com.didiglobal.logi.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.didiglobal.logi.security.common.dto.config.ConfigQueryDTO;
import com.didiglobal.logi.security.common.po.ConfigPO;
import java.util.List;

public interface ConfigDao {
    public int insert(ConfigPO var1);

    public int updateById(ConfigPO var1);

    public int update(ConfigPO var1);

    public int deleteById(Integer var1);

    public IPage<ConfigPO> selectPage(ConfigQueryDTO var1);

    public List<ConfigPO> listByCondition(ConfigPO var1);

    public List<ConfigPO> listConfigByGroup(String var1);

    public List<String> listDistinctGroup();

    public ConfigPO getbyId(Integer var1);

    public ConfigPO getByGroupAndName(String var1, String var2);
}

