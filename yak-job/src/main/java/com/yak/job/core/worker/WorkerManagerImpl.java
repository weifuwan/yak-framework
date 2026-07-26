/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.yak.job.core.worker;

import com.yak.job.LogIJobProperties;
import com.yak.job.common.Result;
import com.yak.job.common.po.LogIWorkerPO;
import com.yak.job.mapper.LogIWorkerMapper;
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
    private LogIWorkerMapper logIWorkerMapper;
    @Autowired
    private LogIJobProperties logIJobProperties;

    @Override
    public Result<List<String>> listAllWorkerIps() {
        List<LogIWorkerPO> logIWorkerPOS = this.logIWorkerMapper.selectByAppName(this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logIWorkerPOS)) {
            return Result.buildFail("\u83b7\u53d6\u4e0d\u5230 worker\uff01");
        }
        return Result.buildSucc(new ArrayList(logIWorkerPOS.stream().map(LogIWorkerPO::getIp).collect(Collectors.toSet())));
    }

    @Override
    public Map<String, LogIWorkerPO> mapAllWorkers() {
        List<LogIWorkerPO> logIWorkerPOS = this.logIWorkerMapper.selectByAppName(this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logIWorkerPOS)) {
            return new HashMap<String, LogIWorkerPO>();
        }
        return logIWorkerPOS.stream().collect(Collectors.toMap(LogIWorkerPO::getIp, l -> l, (l1, l2) -> l1));
    }
}

