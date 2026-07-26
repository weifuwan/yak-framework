/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.yak.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yak.security.common.entity.OplogExtra;
import com.yak.security.common.po.OplogExtraPO;
import com.yak.security.dao.OplogExtraDao;
import com.yak.security.dao.mapper.OplogExtraMapper;
import com.yak.security.util.CopyBeanUtil;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OplogExtraDaoImpl
extends BaseDaoImpl<OplogExtraPO>
implements OplogExtraDao {
    @Autowired
    private OplogExtraMapper oplogExtraMapper;

    @Override
    public List<OplogExtra> selectListByType(Integer type) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object)"type", (Object)type);
        return CopyBeanUtil.copyList(this.oplogExtraMapper.selectList((Wrapper)queryWrapper), OplogExtra.class);
    }

    @Override
    public void insertBatch(List<OplogExtra> oplogExtraList) {
        if (CollectionUtils.isEmpty(oplogExtraList)) {
            return;
        }
        List<OplogExtraPO> oplogExtraPOList = CopyBeanUtil.copyList(oplogExtraList, OplogExtraPO.class);
        for (OplogExtraPO oplogExtraPO : oplogExtraPOList) {
            oplogExtraPO.setAppName(this.logiSecurityProper.getAppName());
            this.oplogExtraMapper.insert(oplogExtraPO);
        }
    }
}

