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

import com.yak.job.common.po.YakTaskLockPO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface YakTaskLockMapper {
    @Insert(value = {"INSERT INTO yak_task_lock(task_code, worker_code, expire_time, create_time, update_time, app_name) VALUES(#{taskCode}, #{workerCode}, #{expireTime}, #{createTime}, #{updateTime}, #{appName})"})
    public int insert(YakTaskLockPO var1);

    @Update(value = {"update yak_task_lock set expire_time=#{expireTime} where id=#{id}"})
    public int update(@Param(value = "id") Long var1, @Param(value = "expireTime") Long var2);

    @Select(value = {"select id, task_code, worker_code, expire_time, create_time, update_time, app_name from yak_task_lock where app_name=#{appName}"})
    public List<YakTaskLockPO> selectByAppName(@Param(value = "appName") String var1);

    @Select(value = {"select id, task_code, worker_code, expire_time, create_time, update_time, app_name from yak_task_lock where task_code=#{taskCode} and app_name=#{appName}"})
    public List<YakTaskLockPO> selectByTaskCode(@Param(value = "taskCode") String var1, @Param(value = "appName") String var2);

    @Select(value = {"select id, task_code, worker_code, expire_time, create_time, update_time, app_name from yak_task_lock where worker_code=#{workerCode} and app_name=#{appName}"})
    public List<YakTaskLockPO> selectByWorkerCode(@Param(value = "workerCode") String var1, @Param(value = "appName") String var2);

    @Select(value = {"select id, task_code, worker_code, expire_time, create_time, update_time, app_name from yak_task_lock where task_code=#{taskCode} and worker_code=#{workerCode} and app_name=#{appName}"})
    public List<YakTaskLockPO> selectByTaskCodeAndWorkerCode(@Param(value = "taskCode") String var1, @Param(value = "workerCode") String var2, @Param(value = "appName") String var3);

    @Delete(value = {"delete from yak_task_lock where id=#{id}"})
    public int deleteById(@Param(value = "id") Long var1);

    @Delete(value = {"delete from yak_task_lock where worker_code=#{workerCode} and app_name=#{appName}"})
    public int deleteByWorkerCodeAndAppName(@Param(value = "workerCode") String var1, @Param(value = "appName") String var2);

    public int deleteByIds(List<Long> var1);
}
