/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.yak.job.core.worker;

import com.yak.job.YakJobProperties;
import com.yak.job.common.Result;
import com.yak.job.common.po.YakWorkerPO;
import com.yak.job.mapper.YakWorkerMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class WorkerManagerImpl
        implements WorkerManager {
    @Autowired
    private YakWorkerMapper yakWorkerMapper;
    @Autowired
    private YakJobProperties yakJobProperties;

    @Override
    public Result<List<String>> listAllWorkerIps() {
        List<YakWorkerPO> yakWorkerPOS = this.yakWorkerMapper.selectByAppName(this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakWorkerPOS)) {
            return Result.buildFail("\u83b7\u53d6\u4e0d\u5230 worker\uff01");
        }
        return Result.buildSucc(new ArrayList(yakWorkerPOS.stream().map(YakWorkerPO::getIp).collect(Collectors.toSet())));
    }

    @Override
    public Map<String, YakWorkerPO> mapAllWorkers() {
        List<YakWorkerPO> yakWorkerPOS = this.yakWorkerMapper.selectByAppName(this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakWorkerPOS)) {
            return new HashMap<String, YakWorkerPO>();
        }
        return yakWorkerPOS.stream().collect(Collectors.toMap(YakWorkerPO::getIp, l -> l, (l1, l2) -> l1));
    }
}

