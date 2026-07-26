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
package com.yak.job.core.consensual;

import com.yak.job.common.domain.YakTask;
import com.yak.job.common.domain.YakWorker;
import com.yak.job.common.po.YakWorkerBlacklistPO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.mapper.YakWorkerBlacklistMapper;
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
    private static final String BLACKLIST_KEY = "BlacklistKey";
    @Autowired
    private YakWorkerBlacklistMapper yakWorkerBlacklistMapper;
    private Cache<Object, Set<String>> blacklistCache = CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.MINUTES).build();

    @Override
    public boolean canClaim(YakTask yakTask) {
        if (this.inBlacklist()) {
            return false;
        }
        return this.tryClaim(yakTask);
    }

    public abstract boolean tryClaim(YakTask var1);

    private boolean inBlacklist() {
        Set<String> blacklist = this.blacklist();
        YakWorker yakWorker = WorkerSingleton.getInstance().getYakWorker();
        return blacklist.contains(yakWorker.getWorkerCode());
    }

    private Set<String> blacklist() {
        Set<String> blacklist = new HashSet<String>();
        try {
            blacklist = (Set) this.blacklistCache.get((Object) BLACKLIST_KEY, () -> {
                List<YakWorkerBlacklistPO> yakWorkerBlacklistPOS = this.yakWorkerBlacklistMapper.selectAll();
                return yakWorkerBlacklistPOS.stream().map(YakWorkerBlacklistPO::getWorkerCode).collect(Collectors.toSet());
            });
        } catch (ExecutionException e) {
            logger.error("class=AbstractConsensual||method=blacklist||url=||msg=", (Throwable) e);
        }
        return blacklist;
    }
}

