/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Delete
 *  org.apache.ibatis.annotations.Insert
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 *  org.apache.ibatis.annotations.Update
 */
package com.yak.job.mapper;

import com.yak.job.common.po.LogIWorkerPO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LogIWorkerMapper {
    @Insert(value={"INSERT INTO logi_worker(worker_code, worker_name, ip, cpu, cpu_used, memory, memory_used, jvm_memory, jvm_memory_used, job_num, heartbeat, app_name) VALUES(#{workerCode}, #{workerName}, #{ip}, #{cpu}, #{cpuUsed}, #{memory}, #{memoryUsed}, #{jvmMemory}, #{jvmMemoryUsed}, #{jobNum}, #{heartbeat}, #{appName})"})
    public int insert(LogIWorkerPO var1);

    @Update(value={"INSERT INTO logi_worker(worker_code, worker_name, ip, cpu, cpu_used, memory, memory_used, jvm_memory, jvm_memory_used, job_num, heartbeat, app_name) VALUES(#{workerCode}, #{workerName}, #{ip}, #{cpu}, #{cpuUsed}, #{memory}, #{memoryUsed}, #{jvmMemory}, #{jvmMemoryUsed}, #{jobNum}, #{heartbeat}, #{appName}) ON DUPLICATE KEY UPDATE cpu=#{cpu}, worker_name=#{workerName}, ip=#{ip}, cpu_used=#{cpuUsed}, memory=#{memory}, memory_used=#{memoryUsed}, jvm_memory=#{jvmMemory}, jvm_memory_used=#{jvmMemoryUsed}, job_num=#{jobNum}, heartbeat=#{heartbeat}, app_name=#{appName}"})
    public int saveOrUpdateById(LogIWorkerPO var1);

    @Update(value={"update logi_worker set cpu=#{cpu}, worker_name=#{workerName}, ip=#{ip}, cpu_used=#{cpuUsed}, memory=#{memory}, memory_used=#{memoryUsed}, jvm_memory=#{jvmMemory}, jvm_memory_used=#{jvmMemoryUsed}, job_num=#{jobNum}, heartbeat=#{heartbeat}, app_name=#{appName} where worker_code=#{workerCode} and app_name=#{appName}"})
    public int updateByCode(LogIWorkerPO var1);

    @Select(value={"select id, worker_code, worker_name, ip, cpu, cpu_used, memory, memory_used, jvm_memory, jvm_memory_used, job_num, heartbeat, app_name from logi_worker where worker_code=#{workerCode} and app_name=#{appName}"})
    public LogIWorkerPO selectByCode(@Param(value="workerCode") String var1, @Param(value="appName") String var2);

    @Delete(value={"delete from logi_worker where worker_code=#{workerCode}"})
    public int deleteByCode(@Param(value="workerCode") String var1);

    @Select(value={"select worker_code, worker_name, ip, cpu, cpu_used, memory, memory_used, jvm_memory,jvm_memory_used, job_num, heartbeat, app_name, update_time from logi_worker where app_name=#{appName}"})
    public List<LogIWorkerPO> selectByAppName(@Param(value="appName") String var1);

    @Select(value={"select count(1) from logi_worker where app_name=#{appName}"})
    public int countByAppName(@Param(value="appName") String var1);
}

