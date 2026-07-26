/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.security.service.impl;

import com.didiglobal.logi.security.common.entity.OplogExtra;
import com.didiglobal.logi.security.common.enums.oplog.OplogCode;
import com.didiglobal.logi.security.dao.OplogExtraDao;
import com.didiglobal.logi.security.service.OplogExtraService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="logiSecurityOplogExtraServiceImpl")
public class OplogExtraServiceImpl
implements OplogExtraService {
    @Autowired
    private OplogExtraDao oplogExtraDao;

    @Override
    public void saveOplogExtraList(List<String> nameList, OplogCode oplogCode) {
        if (oplogCode == null || CollectionUtils.isEmpty(nameList)) {
            return;
        }
        ArrayList<OplogExtra> oplogExtraList = new ArrayList<OplogExtra>();
        for (String name : nameList) {
            OplogExtra oplogExtra = new OplogExtra();
            oplogExtra.setInfo(name);
            oplogExtra.setType(oplogCode.getType());
        }
        this.oplogExtraDao.insertBatch(oplogExtraList);
    }

    @Override
    public List<String> getOplogExtraNameListByType(Integer type) {
        List<OplogExtra> oplogExtraList = this.oplogExtraDao.selectListByType(type);
        ArrayList<String> result = new ArrayList<String>();
        for (OplogExtra oplogExtra : oplogExtraList) {
            result.add(oplogExtra.getInfo());
        }
        return result;
    }
}

