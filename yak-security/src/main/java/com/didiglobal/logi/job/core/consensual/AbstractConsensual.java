/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.didiglobal.logi.job.core.consensual;

import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.common.domain.LogIWorker;
import com.didiglobal.logi.job.common.po.LogIWorkerBlacklistPO;
import com.didiglobal.logi.job.core.WorkerSingleton;
import com.didiglobal.logi.job.core.consensual.Consensual;
import com.didiglobal.logi.job.mapper.LogIWorkerBlacklistMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractConsensual
implements Consensual {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConsensual.class);
    @Autowired
    private LogIWorkerBlacklistMapper logIWorkerBlacklistMapper;
    private static final String BLACKLIST_KEY = "BlacklistKey";
    private Cache<Object, Set<String>> blacklistCache = CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.MINUTES).build();

    @Override
    public boolean canClaim(LogITask logITask) {
        if (this.inBlacklist()) {
            return false;
        }
        return this.tryClaim(logITask);
    }

    public abstract boolean tryClaim(LogITask var1);

    private boolean inBlacklist() {
        Set<String> blacklist = this.blacklist();
        LogIWorker logIWorker = WorkerSingleton.getInstance().getLogIWorker();
        return blacklist.contains(logIWorker.getWorkerCode());
    }

    private Set<String> blacklist() {
        Set<String> blacklist = new HashSet<String>();
        try {
            blacklist = (Set)this.blacklistCache.get((Object)BLACKLIST_KEY, () -> {
                List<LogIWorkerBlacklistPO> logIWorkerBlacklistPOS = this.logIWorkerBlacklistMapper.selectAll();
                return logIWorkerBlacklistPOS.stream().map(LogIWorkerBlacklistPO::getWorkerCode).collect(Collectors.toSet());
            });
        } catch (ExecutionException e) {
            logger.error("class=AbstractConsensual||method=blacklist||url=||msg=", (Throwable)e);
        }
        return blacklist;
    }
}

