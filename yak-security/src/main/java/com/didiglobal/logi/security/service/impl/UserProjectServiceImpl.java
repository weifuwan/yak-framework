/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.security.service.impl;

import com.didiglobal.logi.security.common.dto.user.UserProjectDTO;
import com.didiglobal.logi.security.common.entity.UserProject;
import com.didiglobal.logi.security.common.enums.project.ProjectUserCode;
import com.didiglobal.logi.security.dao.UserProjectDao;
import com.didiglobal.logi.security.service.UserProjectService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="logiSecurityUserProjectServiceImpl")
public class UserProjectServiceImpl
implements UserProjectService {
    private static final int NORMAL = 0;
    private static final int OWNER = 1;
    @Autowired
    private UserProjectDao userProjectDao;

    @Override
    public List<Integer> getUserIdListByProjectId(Integer projectId, ProjectUserCode code) {
        if (projectId == null) {
            return new ArrayList<Integer>();
        }
        return this.userProjectDao.selectUserIdListByProjectId(projectId, code.getType());
    }

    @Override
    public List<Integer> getProjectIdListByUserIdList(List<Integer> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<Integer>();
        }
        return this.userProjectDao.selectProjectIdListByUserIdList(userIdList);
    }

    @Override
    public void saveUserProject(Integer projectId, List<Integer> userIdList) {
        if (projectId == null || CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        this.userProjectDao.insertBatch(this.getUserProjectList(projectId, userIdList, 0));
    }

    @Override
    public void delUserProject(Integer projectId, List<Integer> userIdList) {
        if (projectId == null || CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        this.userProjectDao.deleteUserProject(this.getUserProjectList(projectId, userIdList, 0));
    }

    @Override
    public void saveOwnerProject(Integer projectId, List<Integer> ownerIdList) {
        if (projectId == null || CollectionUtils.isEmpty(ownerIdList)) {
            return;
        }
        this.userProjectDao.insertBatch(this.getUserProjectList(projectId, ownerIdList, 1));
    }

    @Override
    public void delOwnerProject(Integer projectId, List<Integer> ownerIdList) {
        if (projectId == null || CollectionUtils.isEmpty(ownerIdList)) {
            return;
        }
        this.userProjectDao.deleteUserProject(this.getUserProjectList(projectId, ownerIdList, 1));
    }

    @Override
    public void updateUserProject(Integer projectId, List<Integer> userIdList) {
        this.deleteUserProjectByProjectId(projectId);
        this.saveUserProject(projectId, userIdList);
    }

    @Override
    public void updateUserInformationAssociatedWithProject(Integer projectId, List<Integer> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<Integer> userIds = this.userProjectDao.selectUserIdListByProjectId(projectId, 0);
        List<Integer> filterUserIdList = userIdList.stream().filter(id -> !userIds.contains(id)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterUserIdList)) {
            return;
        }
        this.saveUserProject(projectId, filterUserIdList);
    }

    @Override
    public void updateOwnerProject(Integer projectId, List<Integer> ownerIdList) {
        this.deleteOwnerProjectByProjectId(projectId);
        this.saveOwnerProject(projectId, ownerIdList);
    }

    @Override
    public void updateOwnerInformationAssociatedWithProject(Integer projectId, List<Integer> ownerIdList) {
        if (CollectionUtils.isEmpty(ownerIdList)) {
            return;
        }
        List<Integer> userIds = this.userProjectDao.selectUserIdListByProjectId(projectId, 0);
        List<Integer> filterOwnerIdList = ownerIdList.stream().filter(id -> !userIds.contains(id)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterOwnerIdList)) {
            return;
        }
        this.saveUserProject(projectId, filterOwnerIdList);
    }

    @Override
    public void deleteUserProjectByProjectId(Integer projectId) {
        if (projectId == null) {
            return;
        }
        this.userProjectDao.deleteByProjectIdAndUserType(projectId, 0);
    }

    @Override
    public void deleteOwnerProjectByProjectId(Integer projectId) {
        if (projectId == null) {
            return;
        }
        this.userProjectDao.deleteByProjectIdAndUserType(projectId, 1);
    }

    private List<UserProject> getUserProjectList(Integer projectId, List<Integer> userIdList, int userType) {
        ArrayList<UserProject> userProjectList = new ArrayList<UserProject>();
        for (Integer userId : userIdList) {
            UserProject userProject = new UserProject();
            userProject.setProjectId(projectId);
            userProject.setUserId(userId);
            userProject.setUserType(userType);
            userProjectList.add(userProject);
        }
        return userProjectList;
    }

    @Override
    public List<UserProject> lisUserProjectByProjectIds(List<Integer> projectIds) {
        return this.userProjectDao.selectByProjectIds(projectIds);
    }

    @Override
    public List<UserProject> lisUserProjectByUserProjectDTO(UserProjectDTO userProjectDTO) {
        return this.userProjectDao.select(userProjectDTO);
    }
}

