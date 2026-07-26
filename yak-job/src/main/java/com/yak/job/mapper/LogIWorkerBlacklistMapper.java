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

import com.yak.job.common.po.LogIWorkerBlacklistPO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LogIWorkerBlacklistMapper {
    @Insert(value={"INSERT INTO logi_worker_blacklist(worker_code) VALUES(#{workerCode})"})
    public int insert(LogIWorkerBlacklistPO var1);

    @Delete(value={"delete from logi_worker_blacklist where worker_code=#{workerCode}"})
    public int deleteByWorkerCode(@Param(value="workerCode") String var1);

    @Select(value={"select id, worker_code from logi_worker_blacklist"})
    public List<LogIWorkerBlacklistPO> selectAll();
}

