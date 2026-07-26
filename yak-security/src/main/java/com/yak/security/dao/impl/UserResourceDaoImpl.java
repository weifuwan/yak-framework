/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.yak.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yak.security.common.dto.resource.ControlLevelQueryDTO;
import com.yak.security.common.dto.resource.UserResourceQueryDTO;
import com.yak.security.common.entity.UserResource;
import com.yak.security.common.enums.resource.ControlLevelCode;
import com.yak.security.common.po.UserResourcePO;
import com.yak.security.dao.UserResourceDao;
import com.yak.security.dao.mapper.UserResourceMapper;
import com.yak.security.util.CopyBeanUtil;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserResourceDaoImpl
extends BaseDaoImpl<UserResourcePO>
implements UserResourceDao {
    @Autowired
    private UserResourceMapper userResourceMapper;

    private QueryWrapper<UserResourcePO> wrapQueryCriteria(Integer userId, UserResourceQueryDTO queryDTO) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        Integer resourceTypeId = queryDTO.getResourceTypeId();
        ((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.eq((Object)"control_level", (Object)queryDTO.getControlLevel())).eq(userId != null, (Object)"user_id", (Object)userId)).eq(queryDTO.getProjectId() != null, (Object)"project_id", (Object)queryDTO.getProjectId())).eq(resourceTypeId != null, (Object)"resource_type_id", (Object)resourceTypeId)).eq(queryDTO.getResourceId() != null, (Object)"resource_id", (Object)queryDTO.getResourceId());
        return queryWrapper;
    }

    @Override
    public int selectCountByUserId(Integer userId, UserResourceQueryDTO queryDTO) {
        return this.userResourceMapper.selectCount((Wrapper)this.wrapQueryCriteria(userId, queryDTO));
    }

    @Override
    public void deleteByUserId(Integer userId, UserResourceQueryDTO queryDTO) {
        this.userResourceMapper.delete((Wrapper)this.wrapQueryCriteria(userId, queryDTO));
    }

    @Override
    public void deleteByControlLevel(ControlLevelCode controlLevel) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object)"control_level", (Object)controlLevel.getType());
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void insert(UserResource userResource) {
        UserResourcePO userResourcePO = CopyBeanUtil.copy(userResource, UserResourcePO.class);
        userResourcePO.setAppName(this.logiSecurityProper.getAppName());
        this.userResourceMapper.insert(userResourcePO);
    }

    @Override
    public void insertBatch(List<UserResource> userResourceList) {
        if (CollectionUtils.isEmpty(userResourceList)) {
            return;
        }
        List<UserResourcePO> userResourcePOList = CopyBeanUtil.copyList(userResourceList, UserResourcePO.class);
        for (UserResourcePO userResourcePO : userResourcePOList) {
            userResourcePO.setAppName(this.logiSecurityProper.getAppName());
            this.userResourceMapper.insert(userResourcePO);
        }
    }

    @Override
    public void deleteByUserIdList(List<Integer> userIdList, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object)"user_id", userIdList);
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void deleteByProjectIdList(List<Integer> projectIdList, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object)"project_id", projectIdList);
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void deleteByResourceTypeIdList(List<Integer> resourceTypeIdList, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object)"resource_type_id", resourceTypeIdList);
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void deleteByResourceIdList(List<Integer> resourceIdList, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object)"resource_id", resourceIdList);
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public int selectCountByUserIdAndControlLevel(Integer userId, ControlLevelCode controlLevel) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper)queryWrapper.eq(userId != null, (Object)"user_id", (Object)userId)).eq((Object)"control_level", (Object)controlLevel.getType());
        return this.userResourceMapper.selectCount((Wrapper)queryWrapper);
    }

    @Override
    public int selectCount(UserResourceQueryDTO queryDTO) {
        return this.userResourceMapper.selectCount((Wrapper)this.wrapQueryCriteria(null, queryDTO));
    }

    @Override
    public List<Integer> selectResourceIdListByUserId(Integer userId, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(userId, queryDTO);
        queryWrapper.select(new String[]{"resource_id"});
        List resourceIdList = this.userResourceMapper.selectObjs((Wrapper)queryWrapper);
        return resourceIdList.stream().map(Integer.class::cast).collect(Collectors.toList());
    }

    @Override
    public void deleteWithoutUserIdList(UserResourceQueryDTO queryDTO, List<Integer> excludeUserIdList) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        if (!CollectionUtils.isEmpty(excludeUserIdList)) {
            queryWrapper.notIn((Object)"user_id", excludeUserIdList);
        }
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void deleteByUserIdWithoutProjectIdList(Integer userId, UserResourceQueryDTO queryDTO, List<Integer> excludeIdList) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(userId, queryDTO);
        if (!CollectionUtils.isEmpty(excludeIdList)) {
            queryWrapper.notIn((Object)"project_id", excludeIdList);
        }
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public void deleteByUserIdWithoutResourceTypeIdList(Integer userId, UserResourceQueryDTO queryDTO, List<Integer> excludeIdList) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(userId, queryDTO);
        if (!CollectionUtils.isEmpty(excludeIdList)) {
            queryWrapper.notIn((Object)"resource_type_id", excludeIdList);
        }
        this.userResourceMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public int selectCountGroupByUserId(UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.select(new String[]{"COUNT(*)"}).groupBy((Object)"user_id");
        return this.userResourceMapper.selectObjs((Wrapper)queryWrapper).size();
    }

    @Override
    public List<Integer> selectUserIdListGroupByUserId(UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper = this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.select(new String[]{"user_id"}).groupBy((Object)"user_id");
        return this.userResourceMapper.selectObjs((Wrapper)queryWrapper).stream().map(Integer.class::cast).collect(Collectors.toList());
    }

    @Override
    public Integer selectControlLevel(ControlLevelQueryDTO queryDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        ((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.select(new String[]{"MAX(control_level)"}).eq((Object)"user_id", (Object)queryDTO.getUserId())).eq((Object)"project_id", (Object)queryDTO.getProjectId())).eq((Object)"resource_type_id", (Object)queryDTO.getResourceTypeId())).eq((Object)"resource_id", (Object)queryDTO.getResourceId());
        List objects = this.userResourceMapper.selectObjs((Wrapper)queryWrapper);
        return (Integer)objects.get(0);
    }
}

