package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.role.RoleAssignDTO;
import io.yak.framework.security.common.dto.role.RoleQueryDTO;
import io.yak.framework.security.common.dto.role.RoleSaveDTO;
import io.yak.framework.security.common.entity.BaseEntity;
import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.entity.role.Role;
import io.yak.framework.security.common.entity.role.RoleBrief;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.enums.message.MessageCode;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import io.yak.framework.security.common.vo.role.RoleDeleteCheckVO;
import io.yak.framework.security.common.vo.role.RoleVO;
import io.yak.framework.security.common.vo.user.UserBasicVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.dao.RoleDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.MessageService;
import io.yak.framework.security.service.OplogService;
import io.yak.framework.security.service.PermissionService;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserRoleService;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.HttpRequestUtil;
import io.yak.framework.security.util.JsonUtils;
import io.yak.framework.security.util.MathUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service(value = "yakSecurityRoleServiceImpl")
/**
 * 角色与权限编排服务。写操作以事务为边界同步角色、权限关系、通知和审计记录。
 */
public class RoleServiceImpl implements RoleService {
  @Autowired private RoleDao roleDao;
  @Autowired private PermissionService permissionService;
  @Autowired private MessageService messageService;
  @Autowired private OplogService oplogService;
  @Autowired private UserService userService;
  @Autowired private RolePermissionService rolePermissionService;
  @Autowired private UserRoleService userRoleService;

  @Override
  public RoleBriefVO getRoleBriefByRoleId(Long roleId) {
    Role role = this.roleDao.selectByRoleId(roleId);
    return CopyBeanUtil.copy(role, RoleBriefVO.class);
  }

  @Override
  public RoleVO getRoleDetailByRoleId(Long roleId) {
    Role role = this.roleDao.selectByRoleId(roleId);
    if (role == null) {
      return null;
    }
    PermissionTreeVO permissionTreeVO =
        this.permissionService.buildPermissionTreeByRoleId(role.getId());
    RoleVO roleVo = CopyBeanUtil.copy(role, RoleVO.class);
    roleVo.setPermissionTreeVO(permissionTreeVO);
    roleVo.setCreateTime(role.getCreateTime());
    roleVo.setUpdateTime(role.getUpdateTime());
    List<Long> userIdList =
        this.userRoleService.getUserIdListByRoleId(roleId);
    List<UserBriefVO> userBriefVOList =
        this.userService.getUserBriefListByUserIdList(userIdList);
    ArrayList<String> userNames = CollectionUtils.isEmpty(userBriefVOList)
                                      ? new ArrayList<String>()
                                      : userBriefVOList.stream()
                                            .map(UserBriefVO::getUserName)
                                            .collect(Collectors.toList());
    roleVo.setAuthedUserCnt(userIdList.size());
    roleVo.setAuthedUsers(userNames);
    return roleVo;
  }

  @Override
  public PagingData<RoleVO> getRolePage(RoleQueryDTO queryDTO) {
    IPage<Role> pageInfo = this.roleDao.selectPage(queryDTO);
    List<Long> roleIds = pageInfo.getRecords()
                                .stream()
                                .map(BaseEntity::getId)
                                .collect(Collectors.toList());
    List<UserRole> userRoles = this.userRoleService.getByRoleIds(roleIds);
    List<Long> userIds = userRoles.stream()
                                .map(UserRole::getUserId)
                                .distinct()
                                .collect(Collectors.toList());
    List<UserBasicVO> userBasicVOS =
        this.userService.getUserBasicListByUserIdList(userIds);
    Map<Long, String> userId2usernameMap = userBasicVOS.stream().collect(
        Collectors.toMap(UserBasicVO::getId, UserBasicVO::getUserName));
    Map roleId2UserIdListMap = userRoles.stream().collect(Collectors.groupingBy(
        UserRole::getRoleId,
        Collectors.mapping(UserRole::getUserId, Collectors.toList())));
    Map roleId2UserNameListMap =
        userRoles.stream().collect(Collectors.groupingBy(
            UserRole::getRoleId,
            Collectors.mapping(i
                               -> (String)userId2usernameMap.get(i.getUserId()),
                               Collectors.toList())));
    ArrayList<RoleVO> roleVOList = new ArrayList<RoleVO>();
    for (Role role : pageInfo.getRecords()) {
      RoleVO roleVO = CopyBeanUtil.copy(role, RoleVO.class);
      List userIdList = roleId2UserIdListMap.getOrDefault(
          role.getId(), Collections.emptyList());
      roleVO.setAuthedUserCnt(userIdList.size());
      roleVO.setAuthedUsers(roleId2UserNameListMap.getOrDefault(
          role.getId(), Collections.emptyList()));
      roleVO.setCreateTime(role.getCreateTime());
      roleVO.setUpdateTime(role.getUpdateTime());
      roleVOList.add(roleVO);
    }
    return new PagingData<RoleVO>(roleVOList, pageInfo);
  }

  @Override
  @Transactional(transactionManager = "yakSecurityTransactionManager", rollbackFor = {Exception.class})
  public void createRole(RoleSaveDTO roleSaveDTO, HttpServletRequest request)
      throws YakSecurityException {
    this.checkParam(roleSaveDTO, false);
    Role role = CopyBeanUtil.copy(roleSaveDTO, Role.class);
    UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(
        HttpRequestUtil.getOperator(request));
    if (userBriefVO != null) {
      role.setLastReviser(userBriefVO.getUserName());
    }
    role.setRoleCode("r" + MathUtil.getRandomNumber(7));
    this.roleDao.insert(role);
    this.rolePermissionService.saveRolePermission(
        role.getId(), roleSaveDTO.getPermissionIdList());
    this.oplogService.saveOplog(new OplogDTO(
        HttpRequestUtil.getOperator(request), "\u65b0\u589e", "Role",
        roleSaveDTO.getRoleName(), JsonUtils.toJson(roleSaveDTO)));
  }

  @Override
  @Transactional(transactionManager = "yakSecurityTransactionManager", rollbackFor = {Exception.class})
  public void deleteRoleByRoleId(Long roleId, HttpServletRequest request)
      throws YakSecurityException {
    Role role = this.roleDao.selectByRoleId(roleId);
    if (role == null) {
      return;
    }
    List<Long> userIdList =
        this.userRoleService.getUserIdListByRoleId(roleId);
    if (!userIdList.isEmpty()) {
      throw new YakSecurityException(ResultCode.ROLE_USER_AUTHED);
    }
    this.rolePermissionService.deleteRolePermissionByRoleId(roleId);
    this.roleDao.deleteByRoleId(roleId);
    OplogDTO oplogDTO =
        new OplogDTO(HttpRequestUtil.getOperator(request), "\u5220\u9664",
                     "Role", role.getRoleName(), JsonUtils.toJson(role));
    this.oplogService.saveOplog(oplogDTO);
  }

  @Override
  public void deleteUserFromRole(Long roleId, Long userId,
                                 HttpServletRequest request)
      throws YakSecurityException {
    Role role = this.roleDao.selectByRoleId(roleId);
    if (role == null) {
      return;
    }
    this.userRoleService.deleteByUserIdOrRoleId(userId, roleId);
    Role updateLastReviserById = new Role();
    updateLastReviserById.setId(roleId);
    String operator = HttpRequestUtil.getOperator(request);
    UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(operator);
    if (userBriefVO != null) {
      updateLastReviserById.setLastReviser(userBriefVO.getUserName());
    }
    this.roleDao.update(updateLastReviserById);
  }

  @Override
  @Transactional(transactionManager = "yakSecurityTransactionManager", rollbackFor = {Exception.class})
  public void updateRole(RoleSaveDTO saveDTO, HttpServletRequest request)
      throws YakSecurityException {
    if (this.roleDao.selectByRoleId(saveDTO.getId()) == null) {
      throw new YakSecurityException(ResultCode.ROLE_NOT_EXISTS);
    }
    this.checkParam(saveDTO, true);
    Role role = CopyBeanUtil.copy(saveDTO, Role.class);
    UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(
        HttpRequestUtil.getOperator(request));
    if (userBriefVO != null) {
      role.setLastReviser(userBriefVO.getUserName());
    }
    this.roleDao.update(role);
    this.rolePermissionService.updateRolePermission(
        role.getId(), saveDTO.getPermissionIdList());
    this.oplogService.saveOplog(
        new OplogDTO(HttpRequestUtil.getOperator(request), "\u7f16\u8f91",
                     "Role", saveDTO.getRoleName(), JsonUtils.toJson(saveDTO)));
  }

  @Override
  @Transactional(transactionManager = "yakSecurityTransactionManager", rollbackFor = {Exception.class})
  public void assignRoles(RoleAssignDTO assignDTO, HttpServletRequest request)
      throws YakSecurityException {
    String operator = HttpRequestUtil.getOperator(request);
    if (assignDTO.getFlag() == null) {
      throw new YakSecurityException(ResultCode.ROLE_ASSIGN_FLAG_IS_NULL);
    }
    if (Boolean.TRUE.equals(assignDTO.getFlag())) {
      Long userId = assignDTO.getId();
      List<Long> oldRoleIdList =
          this.userRoleService.getRoleIdListByUserId(userId);
      this.userRoleService.updateUserRoleByUserId(userId,
                                                  assignDTO.getIdList());
      UserBriefVO userBriefVO =
          this.userService.getUserBriefByUserName(operator);
      Long oplogId = this.oplogService.saveOplog(new OplogDTO(
          operator, "\u7f16\u8f91", "Role", userBriefVO.getUserName(),
          "\u7ed9\u7528\u6237\u5206\u914d\u89d2\u8272\uff0c" +
              JsonUtils.toJson(assignDTO)));
      this.packAndSaveMessage(oplogId, oldRoleIdList, assignDTO);
    } else {
      Long roleId = assignDTO.getId();
      List<Long> oldUserIdList =
          this.userRoleService.getUserIdListByRoleId(roleId);
      this.userRoleService.updateUserRoleByRoleId(roleId,
                                                  assignDTO.getIdList());
      Role updateLastReviserById = new Role();
      updateLastReviserById.setId(roleId);
      UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(
          HttpRequestUtil.getOperator(request));
      if (userBriefVO != null) {
        updateLastReviserById.setLastReviser(userBriefVO.getUserName());
      }
      this.roleDao.update(updateLastReviserById);
      Role role = this.roleDao.selectByRoleId(assignDTO.getId());
      Long oplogId = this.oplogService.saveOplog(
          new OplogDTO(operator, "\u7f16\u8f91", "Role", role.getRoleName(),
                       "\u7ed9\u89d2\u8272\u5206\u914d\u7528\u6237\uff0c" +
                           JsonUtils.toJson(assignDTO)));
      this.packAndSaveMessage(oplogId, oldUserIdList, assignDTO);
    }
  }

  @Override
  public List<RoleBriefVO> getRoleBriefListByRoleName(String roleName) {
    List<RoleBrief> roleBriefList =
        this.roleDao.selectBriefListByRoleNameAndDescOrderByCreateTime(
            roleName);
    return CopyBeanUtil.copyList(roleBriefList, RoleBriefVO.class);
  }

  @Override
  public RoleDeleteCheckVO checkBeforeDelete(Long roleId) {
    if (roleId == null) {
      return null;
    }
    RoleDeleteCheckVO roleDeleteCheckVO = new RoleDeleteCheckVO();
    roleDeleteCheckVO.setRoleId(roleId);
    List<Long> userIdList =
        this.userRoleService.getUserIdListByRoleId(roleId);
    if (!CollectionUtils.isEmpty(userIdList)) {
      List<UserBriefVO> list =
          this.userService.getUserBriefListByUserIdList(userIdList);
      List<String> usernameList = list.stream()
                                      .map(UserBriefVO::getUserName)
                                      .collect(Collectors.toList());
      roleDeleteCheckVO.setUserNameList(usernameList);
    }
    return roleDeleteCheckVO;
  }

  @Override
  public List<RoleBriefVO> getAllRoleBriefList() {
    List<RoleBrief> roleBriefList = this.roleDao.selectAllBrief();
    return CopyBeanUtil.copyList(roleBriefList, RoleBriefVO.class);
  }

  @Override
  public List<RoleBriefVO> getRoleBriefListByUserId(Long userId) {
    List<Long> roleIdList =
        this.userRoleService.getRoleIdListByUserId(userId);
    if (CollectionUtils.isEmpty(roleIdList)) {
      return new ArrayList<RoleBriefVO>();
    }
    List<RoleBrief> roleBriefList =
        this.roleDao.selectBriefListByRoleIdList(roleIdList);
    return CopyBeanUtil.copyList(roleBriefList, RoleBriefVO.class);
  }

  @Override
  public Map<Long, List<RoleBriefVO>>
  getRoleBriefListByUserIds(List<Long> userId) {
    List<UserRole> userRoleList =
        this.userRoleService.getRoleIdListByUserIds(userId);
    List<Long> roleIds = userRoleList.stream()
                                .map(UserRole::getRoleId)
                                .collect(Collectors.toList());
    List<RoleBriefVO> roleBriefs = CopyBeanUtil.copyList(
        this.roleDao.selectBriefListByRoleIdList(roleIds), RoleBriefVO.class);
    Map<Long, RoleBriefVO> roleId2RoleMap = roleBriefs.stream().collect(
        Collectors.toMap(RoleBriefVO::getId, i -> i));
    return userRoleList.stream().collect(Collectors.groupingBy(
        UserRole::getUserId,
        Collectors.mapping(i
                           -> (RoleBriefVO)roleId2RoleMap.get(i.getRoleId()),
                           Collectors.toList())));
  }

  @Override
  public List<AssignInfoVO> getAssignInfoByRoleId(Long roleId) {
    if (roleId == null) {
      return new ArrayList<AssignInfoVO>();
    }
    List<UserBriefVO> userBriefVOList = this.userService.getAllUserBriefList();
    List<Long> userIdList =
        this.userRoleService.getUserIdListByRoleId(roleId);
    HashSet<Long> hasRoleUserIdSet = new HashSet<Long>(userIdList);
    ArrayList<AssignInfoVO> result = new ArrayList<AssignInfoVO>();
    for (UserBriefVO userBriefVO : userBriefVOList) {
      AssignInfoVO assignInfoVO = new AssignInfoVO();
      assignInfoVO.setHas(hasRoleUserIdSet.contains(userBriefVO.getId()));
      assignInfoVO.setName(userBriefVO.getUserName());
      assignInfoVO.setId(userBriefVO.getId());
      result.add(assignInfoVO);
    }
    return result;
  }

  private void packAndSaveMessage(Long oplogId, List<Long> oldIdList,
                                  RoleAssignDTO roleAssignDTO) {
    List<Long> newIdList = roleAssignDTO.getIdList();
    ArrayList<Long> removeIdList = new ArrayList<Long>();
    ArrayList<Long> addIdList = new ArrayList<Long>();
    Set<Long> set = MathUtil.getIntersection(oldIdList, newIdList);
    for (Long oldId : oldIdList) {
      if (set.contains(oldId))
        continue;
      removeIdList.add(oldId);
    }
    for (Long newId : newIdList) {
      if (set.contains(newId))
        continue;
      addIdList.add(newId);
    }
    if (Boolean.TRUE.equals(roleAssignDTO.getFlag())) {
      ArrayList<Long> userIdList = new ArrayList<Long>();
      userIdList.add(roleAssignDTO.getId());
      this.saveRoleAssignMessage(oplogId, userIdList, removeIdList, userIdList,
                                 addIdList);
    } else {
      ArrayList<Long> roleIdList = new ArrayList<Long>();
      roleIdList.add(roleAssignDTO.getId());
      this.saveRoleAssignMessage(oplogId, removeIdList, roleIdList, addIdList,
                                 roleIdList);
    }
  }

  private void saveRoleAssignMessage(Long oplogId,
                                     List<Long> removeUserIdList,
                                     List<Long> removeRoleIdList,
                                     List<Long> addUserIdList,
                                     List<Long> addRoleIdList) {
    String content;
    MessageDTO messageDTO;
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
    Date date = new Date(System.currentTimeMillis());
    String time = formatter.format(date);
    String addRoleInfo = this.spliceRoleNameByRoleIdList(addRoleIdList);
    String removeRoleInfo = this.spliceRoleNameByRoleIdList(removeRoleIdList);
    ArrayList<MessageDTO> messageDTOList = new ArrayList<MessageDTO>();
    if (!StringUtils.isEmpty((Object)addRoleInfo)) {
      for (Long userId : addUserIdList) {
        messageDTO = new MessageDTO(userId, oplogId);
        content = String.format(MessageCode.ROLE_ADD_MESSAGE.getContent(), time,
                                addRoleInfo);
        messageDTO.setContent(content);
        messageDTO.setTitle(MessageCode.ROLE_ADD_MESSAGE.getTitle());
        messageDTOList.add(messageDTO);
      }
    }
    if (!StringUtils.isEmpty((Object)removeRoleInfo)) {
      for (Long userId : removeUserIdList) {
        messageDTO = new MessageDTO(userId, oplogId);
        content = String.format(MessageCode.ROLE_REMOVE_MESSAGE.getContent(),
                                time, removeRoleInfo);
        messageDTO.setContent(content);
        messageDTO.setTitle(MessageCode.ROLE_REMOVE_MESSAGE.getTitle());
        messageDTOList.add(messageDTO);
      }
    }
    this.messageService.saveMessages(messageDTOList);
  }

  private String spliceRoleNameByRoleIdList(List<Long> roleIdList) {
    List<RoleBrief> roleBriefList =
        this.roleDao.selectBriefListByRoleIdList(roleIdList);
    if (roleBriefList.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < roleBriefList.size() - 1; ++i) {
      sb.append(roleBriefList.get(i).getRoleName()).append(",");
    }
    sb.append(roleBriefList.get(roleBriefList.size() - 1).getRoleName());
    return sb.toString();
  }

  private void checkParam(RoleSaveDTO saveDTO, boolean isUpdate)
      throws YakSecurityException {
    if (StringUtils.isEmpty((Object)saveDTO.getRoleName())) {
      throw new YakSecurityException(ResultCode.ROLE_NAME_CANNOT_BE_BLANK);
    }
    if (CollectionUtils.isEmpty(saveDTO.getPermissionIdList())) {
      throw new YakSecurityException(ResultCode.ROLE_PERMISSION_CANNOT_BE_NULL);
    }
    Long roleId = isUpdate ? saveDTO.getId() : null;
    int count = this.roleDao.selectCountByRoleNameAndNotRoleId(
        saveDTO.getRoleName(), roleId);
    if (count > 0) {
      throw new YakSecurityException(ResultCode.ROLE_NAME_ALREADY_EXISTS);
    }
  }
}
