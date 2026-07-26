/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.google.common.collect.Lists
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.didiglobal.logi.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.oplog.OplogDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectBriefQueryDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectQueryDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectSaveDTO;
import com.didiglobal.logi.security.common.dto.resource.ResourceDTO;
import com.didiglobal.logi.security.common.entity.UserProject;
import com.didiglobal.logi.security.common.entity.dept.Dept;
import com.didiglobal.logi.security.common.entity.project.Project;
import com.didiglobal.logi.security.common.entity.project.ProjectBrief;
import com.didiglobal.logi.security.common.enums.ResultCode;
import com.didiglobal.logi.security.common.enums.project.ProjectUserCode;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVO;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVOWithUser;
import com.didiglobal.logi.security.common.vo.project.ProjectDeleteCheckVO;
import com.didiglobal.logi.security.common.vo.project.ProjectVO;
import com.didiglobal.logi.security.common.vo.user.UserBasicVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.dao.ProjectDao;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.extend.ResourceExtend;
import com.didiglobal.logi.security.extend.ResourceExtendBeanTool;
import com.didiglobal.logi.security.service.DeptService;
import com.didiglobal.logi.security.service.OplogService;
import com.didiglobal.logi.security.service.ProjectService;
import com.didiglobal.logi.security.service.UserProjectService;
import com.didiglobal.logi.security.service.UserService;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import com.didiglobal.logi.security.util.MathUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service(value="logiSecurityProjectServiceImpl")
public class ProjectServiceImpl
implements ProjectService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private DeptService deptService;
    @Autowired
    private OplogService oplogService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private ResourceExtendBeanTool resourceExtendBeanTool;

    @Override
    public ProjectVO getProjectDetailByProjectId(Integer projectId) throws LogiSecurityException {
        Project project = this.projectDao.selectByProjectId(projectId);
        if (project == null) {
            throw new LogiSecurityException(ResultCode.PROJECT_NOT_EXISTS);
        }
        ProjectVO projectVO = CopyBeanUtil.copy(project, ProjectVO.class);
        List<Integer> userIdList = this.userProjectService.getUserIdListByProjectId(projectId, ProjectUserCode.NORMAL);
        projectVO.setUserList(this.userService.getUserBriefListByUserIdList(userIdList));
        List<Integer> ownerIdList = this.userProjectService.getUserIdListByProjectId(projectId, ProjectUserCode.OWNER);
        projectVO.setOwnerList(this.userService.getUserBriefListByUserIdList(ownerIdList));
        projectVO.setDeptList(this.deptService.getDeptBriefListByChildId(projectVO.getDeptId()));
        projectVO.setCreateTime(project.getCreateTime());
        return projectVO;
    }

    @Override
    public ProjectBriefVO getProjectBriefByProjectId(Integer projectId) {
        if (projectId == null) {
            return null;
        }
        Project project = this.projectDao.selectByProjectId(projectId);
        return CopyBeanUtil.copy(project, ProjectBriefVO.class);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public ProjectVO createProject(ProjectSaveDTO saveVo, String operator) throws LogiSecurityException {
        this.checkParam(saveVo, false);
        Project project = CopyBeanUtil.copy(saveVo, Project.class);
        project.setProjectCode("p" + MathUtil.getRandomNumber(7));
        this.projectDao.insert(project);
        this.userProjectService.saveOwnerProject(project.getId(), saveVo.getOwnerIdList());
        this.userProjectService.saveUserProject(project.getId(), saveVo.getUserIdList());
        this.oplogService.saveOplog(new OplogDTO(operator, "\u65b0\u589e", "Project", saveVo.getProjectName(), "'' -> " + saveVo.getProjectName()));
        return CopyBeanUtil.copy(project, ProjectVO.class);
    }

    @Override
    public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO queryDTO) {
        List<Integer> projectIdList = null;
        if (!StringUtils.isEmpty((Object)queryDTO.getChargeUsername())) {
            List<Integer> userIdList = this.userService.getUserIdListByUsernameOrRealName(queryDTO.getChargeUsername());
            projectIdList = this.userProjectService.getProjectIdListByUserIdList(userIdList);
        }
        List<Object> deptIdList = null;
        deptIdList = Objects.isNull(queryDTO.getDeptId()) ? Collections.emptyList() : this.deptService.getDeptIdListByParentId(queryDTO.getDeptId());
        IPage<Project> page = this.projectDao.selectPageByDeptIdListAndProjectIdList(queryDTO, deptIdList, projectIdList);
        ArrayList<ProjectVO> projectVOList = new ArrayList<ProjectVO>();
        Map<Integer, Dept> deptMap = this.deptService.getAllDeptMap();
        for (Project project : page.getRecords()) {
            ProjectVO projectVO = CopyBeanUtil.copy(project, ProjectVO.class);
            List<Integer> userIdList = this.userProjectService.getUserIdListByProjectId(project.getId(), ProjectUserCode.NORMAL);
            projectVO.setUserList(this.userService.getUserBriefListByUserIdList(userIdList));
            List<Integer> ownerIdList = this.userProjectService.getUserIdListByProjectId(project.getId(), ProjectUserCode.OWNER);
            projectVO.setOwnerList(this.userService.getUserBriefListByUserIdList(ownerIdList));
            projectVO.setDeptList(this.deptService.getDeptBriefListFromDeptMapByChildId(deptMap, project.getDeptId()));
            projectVO.setCreateTime(project.getCreateTime());
            projectVOList.add(projectVO);
        }
        return new PagingData<ProjectVO>(projectVOList, page);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void deleteProjectByProjectId(Integer projectId, String operator) {
        Project project = this.projectDao.selectByProjectId(projectId);
        if (project == null) {
            throw new LogiSecurityException(ResultCode.PROJECT_NOT_EXISTS);
        }
        List<String> resources = this.listResourceOfProject(projectId);
        if (!CollectionUtils.isEmpty(resources)) {
            throw new LogiSecurityException(ResultCode.PROJECT_DEL_RESOURCE_NOT_NULL);
        }
        this.userProjectService.deleteUserProjectByProjectId(projectId);
        this.userProjectService.deleteOwnerProjectByProjectId(projectId);
        this.projectDao.deleteByProjectId(projectId);
        this.oplogService.saveOplog(new OplogDTO(operator, "\u5220\u9664", "Project", project.getProjectName(), project.getProjectName() + " -> ''"));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void updateProject(ProjectSaveDTO saveDTO, String operator) throws LogiSecurityException {
        if (this.projectDao.selectByProjectId(saveDTO.getId()) == null) {
            throw new LogiSecurityException(ResultCode.PROJECT_NOT_EXISTS);
        }
        this.checkParam(saveDTO, true);
        Project project = CopyBeanUtil.copy(saveDTO, Project.class);
        this.projectDao.update(project);
        if (!CollectionUtils.isEmpty(saveDTO.getUserIdList())) {
            this.userProjectService.updateUserProject(saveDTO.getId(), saveDTO.getUserIdList());
        }
        if (!CollectionUtils.isEmpty(saveDTO.getOwnerIdList())) {
            this.userProjectService.updateOwnerProject(saveDTO.getId(), saveDTO.getOwnerIdList());
        }
        this.oplogService.saveOplog(new OplogDTO(operator, "\u7f16\u8f91", "Project", saveDTO.getProjectName(), JSON.toJSONString((Object)saveDTO)));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void changeProjectStatus(Integer projectId, String operator) {
        Project project = this.projectDao.selectByProjectId(projectId);
        if (project == null) {
            return;
        }
        project.setRunning(project.getRunning() == false);
        this.projectDao.update(project);
        String curRunningTag = Boolean.TRUE.equals(project.getRunning()) ? "\u542f\u7528" : "\u505c\u7528";
        this.oplogService.saveOplog(new OplogDTO(operator, curRunningTag, "Project", project.getProjectName(), "status:" + project.getRunning()));
    }

    @Override
    public void addProjectUser(Integer projectId, Integer userId, String operator) {
        this.userProjectService.saveUserProject(projectId, new ArrayList<Integer>(userId));
        this.oplogService.saveOplog(new OplogDTO(operator, "\u9879\u76ee", "Project", projectId.toString(), "\u589e\u52a0\u9879\u76ee\u7528\u6237\uff1a" + userId));
    }

    @Override
    public void delProjectUser(Integer projectId, Integer userId, String operator) {
        this.userProjectService.delUserProject(projectId, Collections.singletonList(userId));
        this.oplogService.saveOplog(new OplogDTO(operator, "\u5220\u9664", "Project", projectId.toString(), "\u5220\u9664\u9879\u76ee\u7528\u6237\uff1a" + userId));
    }

    @Override
    public void addProjectOwner(Integer projectId, Integer ownerId, String operator) {
        this.userProjectService.saveOwnerProject(projectId, Collections.singletonList(ownerId));
        this.oplogService.saveOplog(new OplogDTO(operator, "\u65b0\u589e", "Project", projectId.toString(), "\u589e\u52a0\u9879\u76ee\u8d1f\u8d23\u4eba\uff1a" + ownerId));
    }

    @Override
    public void delProjectOwner(Integer projectId, Integer ownerId, String operator) {
        this.userProjectService.delOwnerProject(projectId, new ArrayList<Integer>(ownerId));
        this.oplogService.saveOplog(new OplogDTO(operator, "\u5220\u9664", "Project", projectId.toString(), "\u5220\u9664\u9879\u76ee\u8d1f\u8d23\u4eba\uff1a" + ownerId));
    }

    @Override
    public List<ProjectBriefVO> getProjectBriefList() {
        List<ProjectBrief> projectBriefList = this.projectDao.selectAllBriefList();
        return CopyBeanUtil.copyList(projectBriefList, ProjectBriefVO.class);
    }

    @Override
    public ProjectDeleteCheckVO checkBeforeDelete(Integer projectId) {
        return new ProjectDeleteCheckVO(projectId, this.listResourceOfProject(projectId));
    }

    @Override
    public PagingData<ProjectBriefVO> getProjectBriefPage(ProjectBriefQueryDTO queryDTO) {
        IPage<ProjectBrief> pageInfo = this.projectDao.selectBriefPage(queryDTO);
        List<ProjectBriefVO> list = CopyBeanUtil.copyList(pageInfo.getRecords(), ProjectBriefVO.class);
        return new PagingData<ProjectBriefVO>(list, pageInfo);
    }

    @Override
    public boolean checkProjectExist(Integer projectId) {
        return null != this.projectDao.selectByProjectId(projectId);
    }

    @Override
    public Result<List<UserBriefVO>> unassignedByProjectId(Integer projectId) throws LogiSecurityException {
        if (!this.checkProjectExist(projectId)) {
            throw new LogiSecurityException(ResultCode.PROJECT_NOT_EXISTS);
        }
        ProjectVO projectVO = this.getProjectDetailByProjectId(projectId);
        Set userIds = Optional.ofNullable(projectVO).map(ProjectVO::getUserList).orElse(Lists.newArrayList()).stream().map(UserBriefVO::getId).collect(Collectors.toSet());
        Optional.ofNullable(projectVO).map(ProjectVO::getOwnerList).orElse(Lists.newArrayList()).stream().map(UserBriefVO::getId).forEach(userIds::add);
        List userBriefVOS = this.userService.getAllUserBriefList().stream().filter(userBriefVO -> !userIds.contains(userBriefVO.getId())).collect(Collectors.toList());
        return Result.buildSucc(userBriefVOS);
    }

    @Override
    public Result<List<ProjectBriefVO>> getProjectBriefByUserId(Integer userId) {
        List<Integer> projectIds = this.userProjectService.getProjectIdListByUserIdList(Collections.singletonList(userId));
        if (CollectionUtils.isEmpty(projectIds)) {
            return Result.buildSucc(Lists.newArrayList());
        }
        List projectName = projectIds.stream().map(this::getProjectBriefByProjectId).collect(Collectors.toList());
        return Result.buildSucc(projectName);
    }

    @Override
    public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO queryDTO, List<Integer> ids) {
        List<Object> projectIdList = Lists.newArrayList();
        if (!StringUtils.isEmpty((Object)queryDTO.getChargeUsername())) {
            List<Integer> userIdList = this.userService.getUserIdListByUsernameOrRealName(queryDTO.getChargeUsername());
            projectIdList = this.userProjectService.getProjectIdListByUserIdList(userIdList);
        }
        if (!CollectionUtils.isEmpty(ids)) {
            for (Integer id : ids) {
                if (projectIdList.contains(id)) continue;
                projectIdList.add(id);
            }
        }
        List<Object> deptIdList = null;
        deptIdList = Objects.isNull(queryDTO.getDeptId()) ? Collections.emptyList() : this.deptService.getDeptIdListByParentId(queryDTO.getDeptId());
        IPage<Project> page = this.projectDao.selectPageByDeptIdListAndProjectIdList(queryDTO, deptIdList, projectIdList);
        ArrayList<ProjectVO> projectVOList = new ArrayList<ProjectVO>();
        Map<Integer, Dept> deptMap = this.deptService.getAllDeptMap();
        for (Project project : page.getRecords()) {
            ProjectVO projectVO = CopyBeanUtil.copy(project, ProjectVO.class);
            List<Integer> userIdList = this.userProjectService.getUserIdListByProjectId(project.getId(), ProjectUserCode.NORMAL);
            projectVO.setUserList(this.userService.getUserBriefListByUserIdList(userIdList));
            List<Integer> ownerIdList = this.userProjectService.getUserIdListByProjectId(project.getId(), ProjectUserCode.OWNER);
            projectVO.setOwnerList(this.userService.getUserBriefListByUserIdList(ownerIdList));
            projectVO.setDeptList(this.deptService.getDeptBriefListFromDeptMapByChildId(deptMap, project.getDeptId()));
            projectVO.setCreateTime(project.getCreateTime());
            projectVOList.add(projectVO);
        }
        return new PagingData<ProjectVO>(projectVOList, page);
    }

    private void checkParam(ProjectSaveDTO saveVo, boolean isUpdate) throws LogiSecurityException {
        if (StringUtils.isEmpty((Object)saveVo.getProjectName())) {
            throw new LogiSecurityException(ResultCode.PROJECT_NAME_CANNOT_BE_BLANK);
        }
        Integer projectId = isUpdate ? saveVo.getId() : null;
        int count = this.projectDao.selectCountByProjectNameAndNotProjectId(saveVo.getProjectName(), projectId);
        if (count > 0) {
            throw new LogiSecurityException(ResultCode.PROJECT_NAME_ALREADY_EXISTS);
        }
    }

    private List<String> listResourceOfProject(Integer projectId) {
        ArrayList<String> resources = new ArrayList<String>();
        Project project = this.projectDao.selectByProjectId(projectId);
        if (null == project) {
            return resources;
        }
        ResourceExtend resourceExtend = this.resourceExtendBeanTool.getResourceExtendImpl();
        if (null == resourceExtend) {
            return resources;
        }
        List<ResourceDTO> resourceDTOList = resourceExtend.getResourceList(projectId, null);
        if (CollectionUtils.isEmpty(resourceDTOList)) {
            return resources;
        }
        return resourceDTOList.stream().map(ResourceDTO::getResourceName).collect(Collectors.toList());
    }

    @Override
    public List<ProjectBriefVOWithUser> listProjectBriefVOWithUserByProjectIds(List<Integer> projectIds) {
        List<Project> projects = this.projectDao.selectProjectBriefByProjectIds(projectIds);
        List<UserProject> userProjects = this.userProjectService.lisUserProjectByProjectIds(projectIds);
        Map projectId2UserProjectListMap = userProjects.stream().collect(Collectors.groupingBy(UserProject::getProjectId, Collectors.groupingBy(UserProject::getUserType, Collectors.mapping(UserProject::getUserId, Collectors.toSet()))));
        List<Integer> userIds = userProjects.stream().map(UserProject::getUserId).distinct().collect(Collectors.toList());
        List<UserBasicVO> userBasicListByUserIdList = this.userService.getUserBasicListByUserIdList(userIds);
        Map<Integer, UserBasicVO> userId2UserBasicVOMap = userBasicListByUserIdList.stream().collect(Collectors.toMap(UserBasicVO::getId, i -> i));
        Consumer<ProjectBriefVOWithUser> projectBriefVOWithUserConsumer = projectBriefVOWithUser -> {
            Integer projectId = projectBriefVOWithUser.getId();
            Optional.ofNullable(projectId2UserProjectListMap.get(projectId)).ifPresent(userType2UserIdsMaps -> {
                if (userType2UserIdsMaps.containsKey(ProjectUserCode.NORMAL.getType())) {
                    List<UserBasicVO> userList = ((Set)userType2UserIdsMaps.get(ProjectUserCode.NORMAL.getType())).stream().map(userId2UserBasicVOMap::get).collect(Collectors.toList());
                    projectBriefVOWithUser.setUserList(userList);
                }
                if (userType2UserIdsMaps.containsKey(ProjectUserCode.OWNER.getType())) {
                    List<UserBasicVO> ownerList = ((Set)userType2UserIdsMaps.get(ProjectUserCode.OWNER.getType())).stream().map(userId2UserBasicVOMap::get).collect(Collectors.toList());
                    projectBriefVOWithUser.setOwnerList(ownerList);
                }
            });
        };
        List<ProjectBriefVOWithUser> projectBriefVOWithUsers = CopyBeanUtil.copyList(projects, ProjectBriefVOWithUser.class, projectBriefVOWithUserConsumer);
        return projectBriefVOWithUsers;
    }
}

