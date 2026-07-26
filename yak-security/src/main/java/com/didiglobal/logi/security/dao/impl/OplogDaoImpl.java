/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.didiglobal.logi.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.didiglobal.logi.security.common.dto.oplog.OplogQueryDTO;
import com.didiglobal.logi.security.common.entity.Oplog;
import com.didiglobal.logi.security.common.po.OplogPO;
import com.didiglobal.logi.security.dao.OplogDao;
import com.didiglobal.logi.security.dao.impl.BaseDaoImpl;
import com.didiglobal.logi.security.dao.mapper.OplogMapper;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class OplogDaoImpl
extends BaseDaoImpl<OplogPO>
implements OplogDao {
    @Autowired
    private OplogMapper oplogMapper;

    @Override
    public IPage<Oplog> selectPageWithoutDetail(OplogQueryDTO queryDTO) {
        Page pageInfo = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.eq(queryDTO.getOperateType() != null, (Object)"operate_type", (Object)queryDTO.getOperateType())).eq(queryDTO.getTargetType() != null, (Object)"target_type", (Object)queryDTO.getTargetType())).eq(StringUtils.hasText((String)queryDTO.getOperationMethods()), (Object)"operation_methods", (Object)queryDTO.getOperationMethods())).like(!StringUtils.isEmpty((Object)queryDTO.getDetail()), (Object)"detail", (Object)queryDTO.getDetail())).like(!StringUtils.isEmpty((Object)queryDTO.getTarget()), (Object)"target", (Object)queryDTO.getTarget())).like(!StringUtils.isEmpty((Object)queryDTO.getOperator()), (Object)"operator", (Object)queryDTO.getOperator());
        if (queryDTO.getStartTime() != null) {
            queryWrapper.ge((Object)"create_time", (Object)new Timestamp(queryDTO.getStartTime()));
        }
        if (queryDTO.getEndTime() != null) {
            queryWrapper.le((Object)"create_time", (Object)new Timestamp(queryDTO.getEndTime()));
        }
        queryWrapper.select(new String[]{"id"});
        pageInfo.setTotal((long)this.oplogMapper.selectCount((Wrapper)queryWrapper).intValue());
        queryWrapper.orderByDesc((Object)"update_time");
        queryWrapper.select(new String[]{"id", "operate_type", "detail", "target", "target_type", "operator_ip", "operator", "create_time", "update_time"});
        this.oplogMapper.selectPage((IPage)pageInfo, (Wrapper)queryWrapper);
        return CopyBeanUtil.copyPage(pageInfo, Oplog.class);
    }

    @Override
    public Oplog selectByOplogId(Integer oplogId) {
        if (oplogId == null) {
            return null;
        }
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object)"id", (Object)oplogId);
        return CopyBeanUtil.copy(this.oplogMapper.selectOne((Wrapper)queryWrapper), Oplog.class);
    }

    @Override
    public void insert(Oplog oplog) {
        OplogPO oplogPO = CopyBeanUtil.copy(oplog, OplogPO.class);
        oplogPO.setAppName(this.logiSecurityProper.getAppName());
        this.oplogMapper.insert(oplogPO);
        oplog.setId(oplogPO.getId());
    }

    @Override
    public List<String> listTargetType() {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.select(new String[]{"distinct target_type"});
        List oplogPOS = this.oplogMapper.selectList((Wrapper)queryWrapper);
        if (!CollectionUtils.isEmpty((Collection)oplogPOS)) {
            return oplogPOS.stream().map(OplogPO::getTargetType).collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }
}

