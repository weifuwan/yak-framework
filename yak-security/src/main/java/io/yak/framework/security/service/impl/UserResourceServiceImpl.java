package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.project.ProjectBriefQueryDTO;
import io.yak.framework.security.common.dto.resource.AssignToManyUserDTO;
import io.yak.framework.security.common.dto.resource.AssignToOneUserDTO;
import io.yak.framework.security.common.dto.resource.BatchAssignDTO;
import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUQueryDTO;
import io.yak.framework.security.common.dto.resource.ResourceDTO;
import io.yak.framework.security.common.dto.resource.UserResourceQueryDTO;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.dto.user.UserBriefQueryDTO;
import io.yak.framework.security.common.entity.UserResource;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import io.yak.framework.security.common.enums.resource.HasLevelCode;
import io.yak.framework.security.common.enums.resource.ShowLevelCode;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.resource.MByRDataVO;
import io.yak.framework.security.common.vo.resource.MByRVO;
import io.yak.framework.security.common.vo.resource.MByUDataVO;
import io.yak.framework.security.common.vo.resource.MByUVO;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.dao.UserResourceDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.ResourceExtend;
import io.yak.framework.security.extend.ResourceExtendBeanTool;
import io.yak.framework.security.service.DeptService;
import io.yak.framework.security.service.ProjectService;
import io.yak.framework.security.service.ResourceTypeService;
import io.yak.framework.security.service.UserResourceService;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.CopyBeanUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityUserResourceServiceImpl")
/**
 * 用户资源授权服务，按应用、项目和资源层级维护授权关系，实现数据隔离。
 * 批量授权方法使用事务保证授权关系、审计消息和关联数据的一致性。
 */
public class UserResourceServiceImpl implements UserResourceService {
  @Autowired private UserResourceDao userResourceDao;
  @Autowired private DeptService deptService;
  @Autowired private UserService userService;
  @Autowired private ProjectService projectService;
  @Autowired private ResourceTypeService resourceTypeService;
  @Autowired private ResourceExtendBeanTool resourceExtendBeanTool;

  @Override
  public int getResourceCntByUserId(Integer userId,
                                    UserResourceQueryDTO queryDTO) {
    if (userId == null) {
      return 0;
    }
    return this.userResourceDao.selectCountByUserId(userId, queryDTO);
  }

  @Override
  public boolean getViewPermissionControlStatus() {
    UserResourceQueryDTO queryDTO =
        UserResourceQueryDTO.getOpenViewPermissionControlQueryEntity();
    return this.userResourceDao.selectCountByUserId(0, queryDTO) > 0;
  }

  @Override
  public List<MByUDataVO> getManagerByUserDataList(MByUDataQueryDTO queryDTO)
      throws YakSecurityException {
    this.checkParam(queryDTO);
    Integer projectId = queryDTO.getProjectId();
    Integer resourceTypeId = queryDTO.getResourceTypeId();
    int showLevel = queryDTO.getShowLevel();
    int controlLevel = queryDTO.getControlLevel();
    int userId = queryDTO.getUserId();
    boolean isBatch = queryDTO.getBatch();
    ArrayList<MByUDataVO> resultList = new ArrayList<MByUDataVO>();
    if (ShowLevelCode.PROJECT.getType().equals(showLevel)) {
      List<ProjectBriefVO> projectBriefVOList =
          this.projectService.getProjectBriefList();
      for (ProjectBriefVO projectBriefVO : projectBriefVOList) {
        MByUDataVO dataVo = new MByUDataVO(projectBriefVO.getId(),
                                           projectBriefVO.getProjectName());
        HasLevelCode hasLevel = this.getHasLevel(
            isBatch, controlLevel, userId, projectBriefVO.getId(), null, null);
        dataVo.setHasLevel(hasLevel.getType());
        resultList.add(dataVo);
      }
    } else if (ShowLevelCode.RESOURCE_TYPE.getType().equals(showLevel)) {
      List<ResourceTypeVO> resourceTypeVOList =
          this.resourceTypeService.getAllResourceTypeList();
      for (ResourceTypeVO resourceTypeVO : resourceTypeVOList) {
        MByUDataVO dataVo = new MByUDataVO(resourceTypeVO.getId(),
                                           resourceTypeVO.getTypeName());
        HasLevelCode hasLevel =
            this.getHasLevel(isBatch, controlLevel, userId, projectId,
                             resourceTypeVO.getId(), null);
        dataVo.setHasLevel(hasLevel.getType());
        resultList.add(dataVo);
      }
    } else {
      ResourceExtend resourceExtend =
          this.resourceExtendBeanTool.getResourceExtendImpl();
      List<ResourceDTO> resourceDTOList =
          resourceExtend.getResourceList(projectId, resourceTypeId);
      for (ResourceDTO resourceDto : resourceDTOList) {
        MByUDataVO dataVo = new MByUDataVO(resourceDto.getResourceId(),
                                           resourceDto.getResourceName());
        HasLevelCode hasLevel =
            this.getHasLevel(isBatch, controlLevel, userId, projectId,
                             resourceTypeId, resourceDto.getResourceId());
        dataVo.setHasLevel(hasLevel.getType());
        resultList.add(dataVo);
      }
    }
    return resultList;
  }

  @Override
  public List<MByRDataVO>
  getManagerByResourceDataList(MByRDataQueryDTO queryDTO)
      throws YakSecurityException {
    this.checkParam(queryDTO);
    Integer projectId = queryDTO.getProjectId();
    Integer resourceTypeId = queryDTO.getResourceTypeId();
    Integer resourceId = queryDTO.getResourceId();
    int controlLevel = queryDTO.getControlLevel();
    boolean isBatch = queryDTO.getBatch();
    List<UserBriefVO> userBriefVOList =
        this.userService.getAllUserBriefListOrderByCreateTime(false);
    List<MByRDataVO> result = Collections.synchronizedList(new ArrayList());
    userBriefVOList.parallelStream().forEach(userBriefVO -> {
      MByRDataVO dataVo = new MByRDataVO();
      dataVo.setUserId(userBriefVO.getId());
      dataVo.setUserName(userBriefVO.getUserName());
      dataVo.setRealName(userBriefVO.getRealName());
      HasLevelCode hasLevel =
          this.getHasLevel(isBatch, controlLevel, userBriefVO.getId(),
                           projectId, resourceTypeId, resourceId);
      dataVo.setHasLevel(hasLevel.getType());
      result.add(dataVo);
    });
    return result;
  }

  private HasLevelCode getHasLevel(boolean isBatch, int controlLevel,
                                   int userId, Integer projectId,
                                   Integer resourceTypeId, Integer resourceId) {
    if (isBatch) {
      return HasLevelCode.NONE;
    }
    UserResourceQueryDTO queryDTO = new UserResourceQueryDTO(
        controlLevel, projectId, resourceTypeId, resourceId);
    int hasResourceCnt = this.getResourceCntByUserId(userId, queryDTO);
    if (hasResourceCnt == 0) {
      return HasLevelCode.NONE;
    }
    int resourceCnt = 1;
    if (resourceId == null) {
      ResourceExtend resourceExtend =
          this.resourceExtendBeanTool.getResourceExtendImpl();
      resourceCnt = resourceExtend.getResourceCnt(projectId, resourceTypeId);
      return hasResourceCnt == resourceCnt ? HasLevelCode.ALL
                                           : HasLevelCode.HALF;
    }
    return hasResourceCnt == resourceCnt ? HasLevelCode.ALL : HasLevelCode.NONE;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public void changeResourceViewControlStatus() {
    boolean isOn = this.getViewPermissionControlStatus();
    if (isOn) {
      UserResourceQueryDTO queryDTO =
          UserResourceQueryDTO.getOpenViewPermissionControlQueryEntity();
      this.userResourceDao.deleteByUserId(0, queryDTO);
    } else {
      this.userResourceDao.deleteByControlLevel(ControlLevelCode.VIEW);
      UserResource userResource =
          UserResource.getOpenViewPermissionControlEntity();
      this.userResourceDao.insert(userResource);
    }
  }

  @Override
  public ControlLevelCode getControlLevel(ControlLevelQueryDTO queryDTO)
      throws YakSecurityException {
    if (queryDTO.getUserId() == null) {
      throw new YakSecurityException(ResultCode.USER_ID_CANNOT_BE_NULL);
    }
    if (queryDTO.getProjectId() == null) {
      throw new YakSecurityException(ResultCode.PROJECT_ID_CANNOT_BE_NULL);
    }
    if (queryDTO.getResourceTypeId() == null) {
      throw new YakSecurityException(
          ResultCode.RESOURCE_TYPE_ID_CANNOT_BE_NULL);
    }
    if (queryDTO.getResourceId() == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_ID_CANNOT_BE_NULL);
    }
    Integer controlLevel = this.userResourceDao.selectControlLevel(queryDTO);
    if (controlLevel == null) {
      if (!this.getViewPermissionControlStatus()) {
        return ControlLevelCode.VIEW;
      }
      return ControlLevelCode.NONE;
    }
    return ControlLevelCode.getByType(controlLevel);
  }

  private void checkParam(Integer controlLevel, Integer projectId,
                          Integer resourceTypeId, Integer resourceId)
      throws YakSecurityException {
    if (projectId == null) {
      throw new YakSecurityException(ResultCode.PROJECT_ID_CANNOT_BE_NULL);
    }
    if (resourceTypeId == null && resourceId != null) {
      throw new YakSecurityException(ResultCode.RESOURCE_ASSIGN_ERROR);
    }
    if (ControlLevelCode.getByType(controlLevel) == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_INVALID_CONTROL_LEVEL);
    }
  }

  private void checkParam(MByRDataQueryDTO queryDTO)
      throws YakSecurityException {
    this.checkParam(queryDTO.getControlLevel(), queryDTO.getProjectId(),
                    queryDTO.getResourceTypeId(), queryDTO.getResourceId());
  }

  private void checkParam(MByUDataQueryDTO queryDTO)
      throws YakSecurityException {
    if (queryDTO.getUserId() == null) {
      throw new YakSecurityException(ResultCode.USER_ID_CANNOT_BE_NULL);
    }
    if (ControlLevelCode.getByType(queryDTO.getControlLevel()) == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_INVALID_CONTROL_LEVEL);
    }
    this.checkParam(queryDTO.getShowLevel(), queryDTO.getProjectId(),
                    queryDTO.getResourceTypeId());
  }

  private void checkParam(AssignToOneUserDTO assignDTO)
      throws YakSecurityException {
    if (assignDTO.getUserId() == null) {
      throw new YakSecurityException(ResultCode.USER_ID_CANNOT_BE_NULL);
    }
    if (ControlLevelCode.getByType(assignDTO.getControlLevel()) == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_INVALID_CONTROL_LEVEL);
    }
    if (assignDTO.getProjectId() == null &&
        assignDTO.getResourceTypeId() != null) {
      throw new YakSecurityException(ResultCode.RESOURCE_ASSIGN_ERROR_2);
    }
  }

  private List<UserResource> getUserResourceList(Integer projectId,
                                                 Integer resourceTypeId,
                                                 int controlLevel,
                                                 List<Integer> idList,
                                                 List<Integer> userIdList) {
    List<Integer> resourceTypeIdList;
    ArrayList<Integer> projectIdList;
    ArrayList<Integer> resourceIdList = null;
    if (projectId == null) {
      projectIdList = new ArrayList<Integer>(idList);
      resourceTypeIdList = this.resourceTypeService.getAllResourceTypeIdList();
    } else if (resourceTypeId == null) {
      projectIdList = new ArrayList();
      projectIdList.add(projectId);
      resourceTypeIdList = new ArrayList<Integer>(idList);
    } else {
      projectIdList = new ArrayList();
      projectIdList.add(projectId);
      resourceTypeIdList = new ArrayList<Integer>();
      resourceTypeIdList.add(resourceTypeId);
      resourceIdList = new ArrayList<Integer>(idList);
    }
    List<ResourceDTO> resourceDTOList = this.getResourceDTOList(
        projectIdList, resourceTypeIdList, resourceIdList);
    return this.buildUserResourceList(controlLevel, userIdList,
                                      resourceDTOList);
  }

  private List<ResourceDTO> getResourceDTOList(List<Integer> projectIdList,
                                               List<Integer> resourceTypeIdList,
                                               List<Integer> resourceIdList) {
    ArrayList<ResourceDTO> resourceDTOList = new ArrayList<ResourceDTO>();
    for (Integer projectId : projectIdList) {
      for (Integer resourceTypeId : resourceTypeIdList) {
        if (resourceIdList == null) {
          ResourceExtend resourceExtend =
              this.resourceExtendBeanTool.getResourceExtendImpl();
          List<ResourceDTO> list =
              resourceExtend.getResourceList(projectId, resourceTypeId);
          if (list == null)
            continue;
          resourceDTOList.addAll(list);
          continue;
        }
        for (Integer resourceId : resourceIdList) {
          resourceDTOList.add(
              new ResourceDTO(projectId, resourceTypeId, resourceId));
        }
      }
    }
    return resourceDTOList;
  }

  private List<UserResource>
  buildUserResourceList(int controlLevel, List<Integer> userIdList,
                        List<ResourceDTO> resourceDTOList) {
    ArrayList<UserResource> userResourceList = new ArrayList<UserResource>();
    for (Integer userId : userIdList) {
      for (ResourceDTO resourceDTO : resourceDTOList) {
        UserResource userResource = new UserResource(resourceDTO);
        userResource.setUserId(userId);
        userResource.setControlLevel(controlLevel);
        userResourceList.add(userResource);
      }
    }
    return userResourceList;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public void assignResourcePermission(AssignToOneUserDTO assignDTO)
      throws YakSecurityException {
    this.checkParam(assignDTO);
    Integer userId = assignDTO.getUserId();
    Integer projectId = assignDTO.getProjectId();
    Integer resourceTypeId = assignDTO.getResourceTypeId();
    int controlLevel = assignDTO.getControlLevel();
    UserResourceQueryDTO queryDTO =
        new UserResourceQueryDTO(controlLevel, projectId, resourceTypeId);
    if (CollectionUtils.isEmpty(assignDTO.getExcludeIdList())) {
      this.userResourceDao.deleteByUserId(userId, queryDTO);
    } else if (projectId == null) {
      this.userResourceDao.deleteByUserIdWithoutProjectIdList(
          userId, queryDTO, assignDTO.getExcludeIdList());
    } else if (resourceTypeId == null) {
      this.userResourceDao.deleteByUserIdWithoutResourceTypeIdList(
          userId, queryDTO, assignDTO.getExcludeIdList());
    }
    List<Integer> idList = assignDTO.getIdList();
    ArrayList<Integer> userIdList = new ArrayList<Integer>();
    userIdList.add(userId);
    List<UserResource> userResourceList = this.getUserResourceList(
        projectId, resourceTypeId, controlLevel, idList, userIdList);
    this.userResourceDao.insertBatch(userResourceList);
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public void assignResourcePermission(AssignToManyUserDTO assignDTO,
                                       HttpServletRequest request)
      throws YakSecurityException {
    this.checkParam(assignDTO);
    List<Integer> userIdList = assignDTO.getUserIdList();
    Integer projectId = assignDTO.getProjectId();
    Integer resourceTypeId = assignDTO.getResourceTypeId();
    Integer resourceId = assignDTO.getResourceId();
    int controlLevel = assignDTO.getControlLevel();
    UserResourceQueryDTO queryDTO = new UserResourceQueryDTO(
        controlLevel, projectId, resourceTypeId, resourceId);
    this.userResourceDao.deleteWithoutUserIdList(
        queryDTO, assignDTO.getExcludeUserIdList());
    ArrayList<ResourceDTO> resourceDTOList = new ArrayList<ResourceDTO>();
    if (resourceId == null) {
      ResourceExtend resourceExtend =
          this.resourceExtendBeanTool.getResourceExtendImpl();
      List<ResourceDTO> list =
          resourceExtend.getResourceList(projectId, resourceTypeId);
      if (list != null) {
        resourceDTOList.addAll(list);
      }
    } else {
      resourceDTOList.add(
          new ResourceDTO(projectId, resourceTypeId, resourceId));
    }
    this.userResourceDao.insertBatch(
        this.buildUserResourceList(controlLevel, userIdList, resourceDTOList));
  }

  private void deleteOldRelationBeforeBatchAssign(Integer projectId,
                                                  Integer resourceTypeId,
                                                  boolean flag,
                                                  int controlLevel,
                                                  List<Integer> idList) {
    UserResourceQueryDTO queryDTO =
        new UserResourceQueryDTO(controlLevel, projectId, resourceTypeId);
    if (flag) {
      if (projectId == null) {
        this.userResourceDao.deleteByProjectIdList(idList, queryDTO);
      } else if (resourceTypeId == null) {
        this.userResourceDao.deleteByResourceTypeIdList(idList, queryDTO);
      } else {
        this.userResourceDao.deleteByResourceIdList(idList, queryDTO);
      }
    } else {
      this.userResourceDao.deleteByUserIdList(idList, queryDTO);
    }
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public void batchAssignResourcePermission(BatchAssignDTO assignDTO,
                                            HttpServletRequest request)
      throws YakSecurityException {
    this.checkParam(assignDTO);
    List<Integer> userIdList = assignDTO.getUserIdList();
    List<Integer> idList = assignDTO.getIdList();
    int controlLevel = assignDTO.getControlLevel();
    boolean assignFlag = assignDTO.getAssignFlag();
    Integer projectId = assignDTO.getProjectId();
    Integer resourceTypeId = assignDTO.getResourceTypeId();
    this.deleteOldRelationBeforeBatchAssign(projectId, resourceTypeId,
                                            assignFlag, controlLevel, idList);
    this.userResourceDao.insertBatch(this.getUserResourceList(
        projectId, resourceTypeId, controlLevel, idList, userIdList));
  }

  private void checkParam(BatchAssignDTO assignDTO)
      throws YakSecurityException {
    if (assignDTO.getUserIdList() == null) {
      throw new YakSecurityException(ResultCode.USER_ID_CANNOT_BE_NULL);
    }
    if (assignDTO.getAssignFlag() == null) {
      throw new YakSecurityException(
          ResultCode.RESOURCE_ASSIGN_BATCH_FLAG_CANNOT_BE_NULL);
    }
    if (assignDTO.getProjectId() == null &&
        assignDTO.getResourceTypeId() != null) {
      throw new YakSecurityException(ResultCode.RESOURCE_ASSIGN_ERROR_2);
    }
    if (ControlLevelCode.getByType(assignDTO.getControlLevel()) == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_INVALID_CONTROL_LEVEL);
    }
  }

  private void checkParam(AssignToManyUserDTO assignDTO)
      throws YakSecurityException {
    this.checkParam(assignDTO.getControlLevel(), assignDTO.getProjectId(),
                    assignDTO.getResourceTypeId(), assignDTO.getResourceId());
  }

  @Override
  public PagingData<MByUVO> getManageByUserPage(MByUQueryDTO queryDTO) {
    Map<Integer, Dept> deptMap = this.deptService.getAllDeptMap();
    PagingData<UserBriefVO> userPage =
        this.userService.getUserBriefPage(new UserBriefQueryDTO(queryDTO));
    List result = Collections.synchronizedList(new ArrayList());
    boolean isOn = this.getViewPermissionControlStatus();
    userPage.getBizData().parallelStream().forEach(userBriefVO -> {
      MByUVO dataVo = CopyBeanUtil.copy(userBriefVO, MByUVO.class);
      dataVo.setUserId(userBriefVO.getId());
      dataVo.setDeptList(this.deptService.getDeptBriefListFromDeptMapByChildId(
          deptMap, userBriefVO.getDeptId()));
      dataVo.setAdminResourceCnt(
          this.userResourceDao.selectCountByUserIdAndControlLevel(
              userBriefVO.getId(), ControlLevelCode.ADMIN));
      if (isOn) {
        dataVo.setViewResourceCnt(
            this.userResourceDao.selectCountByUserIdAndControlLevel(
                userBriefVO.getId(), ControlLevelCode.VIEW));
      }
      result.add(dataVo);
    });
    return new PagingData<MByUVO>(result, userPage.getPagination());
  }

  @Override
  public PagingData<MByRVO> getManageByResourcePage(MByRQueryDTO queryDTO)
      throws YakSecurityException {
    this.checkParam(queryDTO);
    boolean isOn = this.getViewPermissionControlStatus();
    PagingData<MByRVO> result =
        queryDTO.getShowLevel().equals(ShowLevelCode.PROJECT.getType())
            ? this.dealProjectLevel(queryDTO, isOn)
            : (queryDTO.getShowLevel().equals(
                   ShowLevelCode.RESOURCE_TYPE.getType())
                   ? this.dealResourceTypeLevel(queryDTO, isOn)
                   : this.dealResourceLevel(queryDTO, isOn));
    return result;
  }

  private void checkParam(Integer showLevel, Integer projectId,
                          Integer resourceTypeId) throws YakSecurityException {
    if (ShowLevelCode.getByType(showLevel) == null) {
      throw new YakSecurityException(ResultCode.RESOURCE_INVALID_SHOW_LEVEL);
    }
    if (showLevel >= ShowLevelCode.RESOURCE_TYPE.getType()) {
      if (projectId == null) {
        throw new YakSecurityException(ResultCode.RESOURCE_SHOW_LEVEL_ERROR);
      }
      ProjectBriefVO projectBriefVO =
          this.projectService.getProjectBriefByProjectId(projectId);
      if (projectBriefVO == null) {
        throw new YakSecurityException(ResultCode.PROJECT_NOT_EXISTS);
      }
    }
    if (showLevel >= ShowLevelCode.RESOURCE.getType()) {
      if (resourceTypeId == null) {
        throw new YakSecurityException(ResultCode.RESOURCE_SHOW_LEVEL_ERROR_2);
      }
      ResourceTypeVO resourceTypeVO =
          this.resourceTypeService.getResourceTypeByResourceTypeId(
              resourceTypeId);
      if (resourceTypeVO == null) {
        throw new YakSecurityException(ResultCode.RESOURCE_TYPE_NOT_EXISTS);
      }
    }
  }

  private void checkParam(MByRQueryDTO queryDTO) throws YakSecurityException {
    this.checkParam(queryDTO.getShowLevel(), queryDTO.getProjectId(),
                    queryDTO.getResourceTypeId());
  }

  private int getAdminOrViewUserCnt(UserResourceQueryDTO queryDTO) {
    List<Integer> userIdList =
        this.userResourceDao.selectUserIdListGroupByUserId(queryDTO);
    ResourceExtend resourceExtend =
        this.resourceExtendBeanTool.getResourceExtendImpl();
    int total = resourceExtend.getResourceCnt(queryDTO.getProjectId(),
                                              queryDTO.getResourceTypeId());
    int cnt = userIdList.size();
    for (Integer userId : userIdList) {
      if (total == this.userResourceDao.selectCountByUserId(userId, queryDTO))
        continue;
      --cnt;
    }
    return cnt;
  }

  private PagingData<MByRVO> dealProjectLevel(MByRQueryDTO queryDTO,
                                              boolean isOn) {
    ProjectBriefQueryDTO projectBriefQueryDTO =
        new ProjectBriefQueryDTO(queryDTO);
    PagingData<ProjectBriefVO> projectPage =
        this.projectService.getProjectBriefPage(projectBriefQueryDTO);
    List result = Collections.synchronizedList(new ArrayList());
    projectPage.getBizData().parallelStream().forEach(projectBriefVO -> {
      MByRVO data = new MByRVO();
      data.setProjectId(projectBriefVO.getId());
      data.setProjectCode(projectBriefVO.getProjectCode());
      data.setProjectName(projectBriefVO.getProjectName());
      Integer projectId = projectBriefVO.getId();
      UserResourceQueryDTO queryDTO2 =
          new UserResourceQueryDTO(ControlLevelCode.ADMIN.getType(), projectId);
      data.setAdminUserCnt(this.getAdminOrViewUserCnt(queryDTO2));
      if (isOn) {
        queryDTO2 = new UserResourceQueryDTO(ControlLevelCode.VIEW.getType(),
                                             projectId);
        data.setViewUserCnt(this.getAdminOrViewUserCnt(queryDTO2));
      }
      result.add(data);
    });
    return new PagingData<MByRVO>(result, projectPage.getPagination());
  }

  private PagingData<MByRVO> dealResourceTypeLevel(MByRQueryDTO queryDTO,
                                                   boolean isOn) {
    List result = Collections.synchronizedList(new ArrayList());
    ResourceTypeQueryDTO resourceTypeQueryDTO =
        new ResourceTypeQueryDTO(queryDTO);
    PagingData<ResourceTypeVO> resourceTypePage =
        this.resourceTypeService.getResourceTypePage(resourceTypeQueryDTO);
    ProjectBriefVO projectBriefVO =
        this.projectService.getProjectBriefByProjectId(queryDTO.getProjectId());
    resourceTypePage.getBizData().parallelStream().forEach(resourceTypeVO -> {
      MByRVO data = new MByRVO();
      data.setResourceTypeId(resourceTypeVO.getId());
      data.setResourceTypeName(resourceTypeVO.getTypeName());
      data.setProjectId(queryDTO.getProjectId());
      data.setProjectName(projectBriefVO.getProjectName());
      UserResourceQueryDTO queryDTO2 = new UserResourceQueryDTO(
          ControlLevelCode.ADMIN.getType(), queryDTO.getProjectId(),
          resourceTypeVO.getId());
      data.setAdminUserCnt(this.getAdminOrViewUserCnt(queryDTO2));
      if (isOn) {
        queryDTO2 = new UserResourceQueryDTO(ControlLevelCode.VIEW.getType(),
                                             queryDTO.getProjectId(),
                                             resourceTypeVO.getId());
        data.setViewUserCnt(this.getAdminOrViewUserCnt(queryDTO2));
      }
      result.add(data);
    });
    return new PagingData<MByRVO>(result, resourceTypePage.getPagination());
  }

  private PagingData<MByRVO> dealResourceLevel(MByRQueryDTO queryDTO,
                                               boolean isOn) {
    ResourceExtend resourceExtend =
        this.resourceExtendBeanTool.getResourceExtendImpl();
    PagingData<ResourceDTO> page = resourceExtend.getResourcePage(
        queryDTO.getProjectId(), queryDTO.getResourceTypeId(),
        queryDTO.getName(), queryDTO.getPage(), queryDTO.getSize());
    if (page == null) {
      return new PagingData<MByRVO>();
    }
    ArrayList<MByRVO> list = new ArrayList<MByRVO>();
    Integer projectId = queryDTO.getProjectId();
    Integer resourceTypeId = queryDTO.getResourceTypeId();
    ResourceTypeVO resourceTypeVO =
        this.resourceTypeService.getResourceTypeByResourceTypeId(
            resourceTypeId);
    for (ResourceDTO resourceDTO : page.getBizData()) {
      MByRVO data = new MByRVO();
      data.setResourceTypeId(resourceTypeVO.getId());
      data.setResourceTypeName(resourceTypeVO.getTypeName());
      data.setProjectId(queryDTO.getProjectId());
      data.setResourceId(resourceDTO.getResourceId());
      data.setResourceName(resourceDTO.getResourceName());
      UserResourceQueryDTO queryDTO2 =
          new UserResourceQueryDTO(ControlLevelCode.ADMIN.getType(), projectId,
                                   resourceTypeId, resourceDTO.getResourceId());
      data.setAdminUserCnt(
          this.userResourceDao.selectCountGroupByUserId(queryDTO2));
      if (isOn) {
        queryDTO2 = new UserResourceQueryDTO(ControlLevelCode.VIEW.getType(),
                                             projectId, resourceTypeId,
                                             resourceDTO.getResourceId());
        data.setViewUserCnt(
            this.userResourceDao.selectCountGroupByUserId(queryDTO2));
      }
      list.add(data);
    }
    return new PagingData<MByRVO>(list, page.getPagination());
  }
}
