/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Delete
 *  org.apache.ibatis.annotations.Insert
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 */
package com.didiglobal.logi.job.mapper;

import com.didiglobal.logi.job.common.po.LogIJobPO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogIJobMapper {
    @Delete(value={"delete from logi_job where job_code=#{code}"})
    public int deleteByCode(String var1);

    @Insert(value={"INSERT INTO logi_job(job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name) VALUES(#{jobCode}, #{taskCode}, #{className}, #{tryTimes}, #{workerCode}, #{startTime}, #{createTime}, #{updateTime}, #{appName})"})
    public int insert(LogIJobPO var1);

    @Select(value={"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from logi_job"})
    public List<LogIJobPO> selectAll();

    @Select(value={"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from logi_job where app_name=#{appName}"})
    public List<LogIJobPO> selectByAppName(@Param(value="appName") String var1);

    @Select(value={"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from logi_job where job_code=#{JobCode} and app_name=#{appName}"})
    public LogIJobPO selectByCode(@Param(value="code") String var1, @Param(value="appName") String var2);
}

