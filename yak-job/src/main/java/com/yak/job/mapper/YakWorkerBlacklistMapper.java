/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Delete
 *  org.apache.ibatis.annotations.Insert
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 */
package com.yak.job.mapper;

import com.yak.job.common.po.YakWorkerBlacklistPO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 工作节点黑名单 MyBatis 映射接口。
 */
public interface YakWorkerBlacklistMapper {
    @Insert(value = {"INSERT INTO yak_worker_blacklist(worker_code) VALUES(#{workerCode})"})
    int insert(YakWorkerBlacklistPO var1);

    @Delete(value = {"delete from yak_worker_blacklist where worker_code=#{workerCode}"})
    int deleteByWorkerCode(@Param(value = "workerCode") String var1);

    @Select(value = {"select id, worker_code from yak_worker_blacklist"})
    List<YakWorkerBlacklistPO> selectAll();
}

