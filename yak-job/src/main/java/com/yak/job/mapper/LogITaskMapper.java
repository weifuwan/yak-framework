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

import com.yak.job.common.po.LogITaskPO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LogITaskMapper {
    @Insert(value={"INSERT INTO logi_task(task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, app_name, owner) VALUES(#{taskCode}, #{taskName}, #{taskDesc}, #{cron}, #{className}, #{params}, #{retryTimes}, #{lastFireTime}, #{timeout}, #{status}, #{subTaskCodes}, #{consensual}, #{taskWorkerStr}, #{appName}, #{owner})"})
    public int insert(LogITaskPO var1);

    @Delete(value={"delete from logi_task where task_code=#{taskCode} and app_name=#{appName}"})
    public int deleteByCode(@Param(value="taskCode") String var1, @Param(value="appName") String var2);

    @Update(value={"update logi_task set task_name=#{taskName}, task_desc=#{taskDesc}, cron=#{cron}, class_name=#{className}, params=#{params}, retry_times=#{retryTimes}, last_fire_time=#{lastFireTime}, timeout=#{timeout}, status=#{status}, sub_task_codes=#{subTaskCodes}, consensual=#{consensual}, task_worker_str=#{taskWorkerStr}, owner=#{owner} where task_code=#{taskCode}"})
    public int updateByCode(LogITaskPO var1);

    @Update(value={"update logi_task set task_worker_str=#{taskWorkerStr} where task_code=#{taskCode}"})
    public int updateTaskWorkStrByCode(LogITaskPO var1);

    @Select(value={"select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task where task_code=#{taskCode} and app_name=#{appName}"})
    public LogITaskPO selectByCode(@Param(value="taskCode") String var1, @Param(value="appName") String var2);

    @Select(value={"<script>select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task where app_name=#{appName} and codes in <foreach collection='codes' item='code' index='index' open='(' close=')' separator=','>  #{code} </foreach> </script>"})
    public List<LogITaskPO> selectByCodes(@Param(value="codes") List<String> var1, @Param(value="appName") String var2);

    @Select(value={"select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task"})
    public List<LogITaskPO> selectAll();

    @Select(value={"select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task where app_name=#{appName}"})
    public List<LogITaskPO> selectByAppName(@Param(value="appName") String var1);

    @Select(value={"select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task where app_name=#{appName} and status=1"})
    public List<LogITaskPO> selectRuningByAppName(@Param(value="appName") String var1);

    @Select(value={"select id, task_code, task_name, task_desc, cron, class_name, params, retry_times, last_fire_time, timeout, status, sub_task_codes, consensual, task_worker_str, create_time, update_time, app_name, owner from logi_task where app_name=#{appName}  order by id desc limit #{start}, #{size} "})
    public List<LogITaskPO> selectByAppNameAndSize(@Param(value="appName") String var1, @Param(value="start") int var2, @Param(value="size") int var3);

    @Select(value={"select count(1) from logi_task where app_name=#{appName}"})
    public int selectCountByAppName(@Param(value="appName") String var1);

    public List<LogITaskPO> pagineListByCondition(@Param(value="appName") String var1, @Param(value="id") Long var2, @Param(value="taskDesc") String var3, @Param(value="className") String var4, @Param(value="jobStatus") Integer var5, @Param(value="start") Integer var6, @Param(value="size") Integer var7);

    public Integer pagineCountByCondition(@Param(value="appName") String var1, @Param(value="id") Long var2, @Param(value="taskDesc") String var3, @Param(value="className") String var4, @Param(value="jobStatus") Integer var5);
}

