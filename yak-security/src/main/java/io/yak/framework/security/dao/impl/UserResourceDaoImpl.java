package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.UserResourceQueryDTO;
import io.yak.framework.security.common.entity.UserResource;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import io.yak.framework.security.common.po.UserResourcePO;
import io.yak.framework.security.dao.UserResourceDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.UserResourceMapper;
import io.yak.framework.security.util.CopyBeanUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserResourceDaoImpl
        extends BaseDaoImpl<UserResourcePO> implements UserResourceDao {
    @Autowired
    private UserResourceMapper userResourceMapper;

    private QueryWrapper<UserResourcePO>
    wrapQueryCriteria(Long userId, UserResourceQueryDTO queryDTO) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        Long resourceTypeId = queryDTO.getResourceTypeId();
        ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) queryWrapper.eq(
                (Object) ("control_" +
                        "level"),
                (Object) queryDTO
                        .getControlLevel()))
                .eq(userId != null, (Object) "user_id",
                        (Object) userId))
                .eq(queryDTO.getProjectId() != null,
                        (Object) "project_id",
                        (Object) queryDTO.getProjectId()))
                .eq(resourceTypeId != null, (Object) "resource_type_id",
                        (Object) resourceTypeId))
                .eq(queryDTO.getResourceId() != null, (Object) "resource_id",
                        (Object) queryDTO.getResourceId());
        return queryWrapper;
    }

    @Override
    public int selectCountByUserId(Long userId,
                                   UserResourceQueryDTO queryDTO) {
        return this.userResourceMapper.selectCount(
                (Wrapper) this.wrapQueryCriteria(userId, queryDTO));
    }

    @Override
    public void deleteByUserId(Long userId, UserResourceQueryDTO queryDTO) {
        this.userResourceMapper.delete(
                (Wrapper) this.wrapQueryCriteria(userId, queryDTO));
    }

    @Override
    public void deleteByControlLevel(ControlLevelCode controlLevel) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object) "control_level", (Object) controlLevel.getType());
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void insert(UserResource userResource) {
        UserResourcePO userResourcePO =
                CopyBeanUtil.copy(userResource, UserResourcePO.class);
        this.userResourceMapper.insert(userResourcePO);
    }

    @Override
    public void insertBatch(List<UserResource> userResourceList) {
        if (CollectionUtils.isEmpty(userResourceList)) {
            return;
        }
        List<UserResourcePO> userResourcePOList =
                CopyBeanUtil.copyList(userResourceList, UserResourcePO.class);
        for (UserResourcePO userResourcePO : userResourcePOList) {
            this.userResourceMapper.insert(userResourcePO);
        }
    }

    @Override
    public void deleteByUserIdList(List<Long> userIdList,
                                   UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object) "user_id", userIdList);
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void deleteByProjectIdList(List<Long> projectIdList,
                                      UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object) "project_id", projectIdList);
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void deleteByResourceTypeIdList(List<Long> resourceTypeIdList,
                                           UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object) "resource_type_id", resourceTypeIdList);
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void deleteByResourceIdList(List<Long> resourceIdList,
                                       UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.in((Object) "resource_id", resourceIdList);
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public int selectCountByUserIdAndControlLevel(Long userId,
                                                  ControlLevelCode controlLevel) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper) queryWrapper.eq(userId != null, (Object) "user_id",
                (Object) userId))
                .eq((Object) "control_level", (Object) controlLevel.getType());
        return this.userResourceMapper.selectCount((Wrapper) queryWrapper);
    }

    @Override
    public int selectCount(UserResourceQueryDTO queryDTO) {
        return this.userResourceMapper.selectCount(
                (Wrapper) this.wrapQueryCriteria(null, queryDTO));
    }

    @Override
    public List<Long>
    selectResourceIdListByUserId(Long userId, UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(userId, queryDTO);
        queryWrapper.select(new String[]{"resource_id"});
        List resourceIdList =
                this.userResourceMapper.selectObjs((Wrapper) queryWrapper);
        return resourceIdList.stream()
                .map(Integer.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWithoutUserIdList(UserResourceQueryDTO queryDTO,
                                        List<Long> excludeUserIdList) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        if (!CollectionUtils.isEmpty(excludeUserIdList)) {
            queryWrapper.notIn((Object) "user_id", excludeUserIdList);
        }
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void deleteByUserIdWithoutProjectIdList(Long userId,
                                                   UserResourceQueryDTO queryDTO,
                                                   List<Long> excludeIdList) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(userId, queryDTO);
        if (!CollectionUtils.isEmpty(excludeIdList)) {
            queryWrapper.notIn((Object) "project_id", excludeIdList);
        }
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public void
    deleteByUserIdWithoutResourceTypeIdList(Long userId,
                                            UserResourceQueryDTO queryDTO,
                                            List<Long> excludeIdList) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(userId, queryDTO);
        if (!CollectionUtils.isEmpty(excludeIdList)) {
            queryWrapper.notIn((Object) "resource_type_id", excludeIdList);
        }
        this.userResourceMapper.delete((Wrapper) queryWrapper);
    }

    @Override
    public int selectCountGroupByUserId(UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.select(new String[]{"COUNT(*)"}).groupBy((Object) "user_id");
        return this.userResourceMapper.selectObjs((Wrapper) queryWrapper).size();
    }

    @Override
    public List<Long>
    selectUserIdListGroupByUserId(UserResourceQueryDTO queryDTO) {
        QueryWrapper<UserResourcePO> queryWrapper =
                this.wrapQueryCriteria(null, queryDTO);
        queryWrapper.select(new String[]{"user_id"}).groupBy((Object) "user_id");
        return this.userResourceMapper.selectObjs((Wrapper) queryWrapper)
                .stream()
                .map(Integer.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Integer selectControlLevel(ControlLevelQueryDTO queryDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) queryWrapper
                .select(
                        new String[]{"MAX(control_level)"})
                .eq((Object) "user_id",
                        (Object) queryDTO.getUserId()))
                .eq((Object) "project_id",
                        (Object) queryDTO.getProjectId()))
                .eq((Object) "resource_type_id",
                        (Object) queryDTO.getResourceTypeId()))
                .eq((Object) "resource_id", (Object) queryDTO.getResourceId());
        List objects = this.userResourceMapper.selectObjs((Wrapper) queryWrapper);
        return (Integer) objects.get(0);
    }
}
