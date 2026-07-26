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
package com.yak.job.mapper;

import com.yak.job.common.po.YakJobPO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface YakJobMapper {
    @Delete(value = {"delete from yak_job where job_code=#{code}"})
    public int deleteByCode(String var1);

    @Insert(value = {"INSERT INTO yak_job(job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name) VALUES(#{jobCode}, #{taskCode}, #{className}, #{tryTimes}, #{workerCode}, #{startTime}, #{createTime}, #{updateTime}, #{appName})"})
    public int insert(YakJobPO var1);

    @Select(value = {"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from yak_job"})
    public List<YakJobPO> selectAll();

    @Select(value = {"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from yak_job where app_name=#{appName}"})
    public List<YakJobPO> selectByAppName(@Param(value = "appName") String var1);

    @Select(value = {"select id, job_code, task_code, class_name, try_times, worker_code, start_time, create_time, update_time, app_name from yak_job where job_code=#{JobCode} and app_name=#{appName}"})
    public YakJobPO selectByCode(@Param(value = "code") String var1, @Param(value = "appName") String var2);
}

