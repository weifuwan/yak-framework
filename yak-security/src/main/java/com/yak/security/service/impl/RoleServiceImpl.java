/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.yak.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yak.security.common.PagingData;
import com.yak.security.common.dto.message.MessageDTO;
import com.yak.security.common.dto.oplog.OplogDTO;
import com.yak.security.common.dto.role.RoleAssignDTO;
import com.yak.security.common.dto.role.RoleQueryDTO;
import com.yak.security.common.dto.role.RoleSaveDTO;
import com.yak.security.common.entity.BaseEntity;
import com.yak.security.common.entity.UserRole;
import com.yak.security.common.entity.role.Role;
import com.yak.security.common.entity.role.RoleBrief;
import com.yak.security.common.enums.ResultCode;
import com.yak.security.common.enums.message.MessageCode;
import com.yak.security.common.vo.permission.PermissionTreeVO;
import com.yak.security.common.vo.role.AssignInfoVO;
import com.yak.security.common.vo.role.RoleBriefVO;
import com.yak.security.common.vo.role.RoleDeleteCheckVO;
import com.yak.security.common.vo.role.RoleVO;
import com.yak.security.common.vo.user.UserBasicVO;
import com.yak.security.common.vo.user.UserBriefVO;
import com.yak.security.dao.RoleDao;
import com.yak.security.exception.LogiSecurityException;
import com.yak.security.service.MessageService;
import com.yak.security.service.OplogService;
import com.yak.security.service.PermissionService;
import com.yak.security.service.RolePermissionService;
import com.yak.security.service.RoleService;
import com.yak.security.service.UserRoleService;
import com.yak.security.service.UserService;
import com.yak.security.util.CopyBeanUtil;
import com.yak.security.util.HttpRequestUtil;
import com.yak.security.util.MathUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service(value="logiSecurityRoleServiceImpl")
public class RoleServiceImpl
implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OplogService oplogService;
    @Autowired
    private UserService userService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public RoleBriefVO getRoleBriefByRoleId(Integer roleId) {
        Role role = this.roleDao.selectByRoleId(roleId);
        return CopyBeanUtil.copy(role, RoleBriefVO.class);
    }

    @Override
    public RoleVO getRoleDetailByRoleId(Integer roleId) {
        Role role = this.roleDao.selectByRoleId(roleId);
        if (role == null) {
            return null;
        }
        PermissionTreeVO permissionTreeVO = this.permissionService.buildPermissionTreeByRoleId(role.getId());
        RoleVO roleVo = CopyBeanUtil.copy(role, RoleVO.class);
        roleVo.setPermissionTreeVO(permissionTreeVO);
        roleVo.setCreateTime(role.getCreateTime());
        roleVo.setUpdateTime(role.getUpdateTime());
        List<Integer> userIdList = this.userRoleService.getUserIdListByRoleId(roleId);
        List<UserBriefVO> userBriefVOList = this.userService.getUserBriefListByUserIdList(userIdList);
        ArrayList<String> userNames = CollectionUtils.isEmpty(userBriefVOList) ? new ArrayList<String>() : userBriefVOList.stream().map(UserBriefVO::getUserName).collect(Collectors.toList());
        roleVo.setAuthedUserCnt(userIdList.size());
        roleVo.setAuthedUsers(userNames);
        return roleVo;
    }

    @Override
    public PagingData<RoleVO> getRolePage(RoleQueryDTO queryDTO) {
        IPage<Role> pageInfo = this.roleDao.selectPage(queryDTO);
        List<Integer> roleIds = pageInfo.getRecords().stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<UserRole> userRoles = this.userRoleService.getByRoleIds(roleIds);
        List<Integer> userIds = userRoles.stream().map(UserRole::getUserId).distinct().collect(Collectors.toList());
        List<UserBasicVO> userBasicVOS = this.userService.getUserBasicListByUserIdList(userIds);
        Map<Integer, String> userId2usernameMap = userBasicVOS.stream().collect(Collectors.toMap(UserBasicVO::getId, UserBasicVO::getUserName));
        Map roleId2UserIdListMap = userRoles.stream().collect(Collectors.groupingBy(UserRole::getRoleId, Collectors.mapping(UserRole::getUserId, Collectors.toList())));
        Map roleId2UserNameListMap = userRoles.stream().collect(Collectors.groupingBy(UserRole::getRoleId, Collectors.mapping(i -> (String)userId2usernameMap.get(i.getUserId()), Collectors.toList())));
        ArrayList<RoleVO> roleVOList = new ArrayList<RoleVO>();
        for (Role role : pageInfo.getRecords()) {
            RoleVO roleVO = CopyBeanUtil.copy(role, RoleVO.class);
            List userIdList = roleId2UserIdListMap.getOrDefault(role.getId(), Collections.emptyList());
            roleVO.setAuthedUserCnt(userIdList.size());
            roleVO.setAuthedUsers(roleId2UserNameListMap.getOrDefault(role.getId(), Collections.emptyList()));
            roleVO.setCreateTime(role.getCreateTime());
            roleVO.setUpdateTime(role.getUpdateTime());
            roleVOList.add(roleVO);
        }
        return new PagingData<RoleVO>(roleVOList, pageInfo);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void createRole(RoleSaveDTO roleSaveDTO, HttpServletRequest request) throws LogiSecurityException {
        this.checkParam(roleSaveDTO, false);
        Role role = CopyBeanUtil.copy(roleSaveDTO, Role.class);
        UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(HttpRequestUtil.getOperator(request));
        if (userBriefVO != null) {
            role.setLastReviser(userBriefVO.getUserName());
        }
        role.setRoleCode("r" + MathUtil.getRandomNumber(7));
        this.roleDao.insert(role);
        this.rolePermissionService.saveRolePermission(role.getId(), roleSaveDTO.getPermissionIdList());
        this.oplogService.saveOplog(new OplogDTO(HttpRequestUtil.getOperator(request), "\u65b0\u589e", "Role", roleSaveDTO.getRoleName(), JSON.toJSONString((Object)roleSaveDTO)));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void deleteRoleByRoleId(Integer roleId, HttpServletRequest request) throws LogiSecurityException {
        Role role = this.roleDao.selectByRoleId(roleId);
        if (role == null) {
            return;
        }
        List<Integer> userIdList = this.userRoleService.getUserIdListByRoleId(roleId);
        if (!userIdList.isEmpty()) {
            throw new LogiSecurityException(ResultCode.ROLE_USER_AUTHED);
        }
        this.rolePermissionService.deleteRolePermissionByRoleId(roleId);
        this.roleDao.deleteByRoleId(roleId);
        OplogDTO oplogDTO = new OplogDTO(HttpRequestUtil.getOperator(request), "\u5220\u9664", "Role", role.getRoleName(), JSON.toJSONString((Object)role));
        this.oplogService.saveOplog(oplogDTO);
    }

    @Override
    public void deleteUserFromRole(Integer roleId, Integer userId, HttpServletRequest request) throws LogiSecurityException {
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
    @Transactional(rollbackFor={Exception.class})
    public void updateRole(RoleSaveDTO saveDTO, HttpServletRequest request) throws LogiSecurityException {
        if (this.roleDao.selectByRoleId(saveDTO.getId()) == null) {
            throw new LogiSecurityException(ResultCode.ROLE_NOT_EXISTS);
        }
        this.checkParam(saveDTO, true);
        Role role = CopyBeanUtil.copy(saveDTO, Role.class);
        UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(HttpRequestUtil.getOperator(request));
        if (userBriefVO != null) {
            role.setLastReviser(userBriefVO.getUserName());
        }
        this.roleDao.update(role);
        this.rolePermissionService.updateRolePermission(role.getId(), saveDTO.getPermissionIdList());
        this.oplogService.saveOplog(new OplogDTO(HttpRequestUtil.getOperator(request), "\u7f16\u8f91", "Role", saveDTO.getRoleName(), JSON.toJSONString((Object)saveDTO)));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void assignRoles(RoleAssignDTO assignDTO, HttpServletRequest request) throws LogiSecurityException {
        String operator = HttpRequestUtil.getOperator(request);
        if (assignDTO.getFlag() == null) {
            throw new LogiSecurityException(ResultCode.ROLE_ASSIGN_FLAG_IS_NULL);
        }
        if (Boolean.TRUE.equals(assignDTO.getFlag())) {
            Integer userId = assignDTO.getId();
            List<Integer> oldRoleIdList = this.userRoleService.getRoleIdListByUserId(userId);
            this.userRoleService.updateUserRoleByUserId(userId, assignDTO.getIdList());
            UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(operator);
            Integer oplogId = this.oplogService.saveOplog(new OplogDTO(operator, "\u7f16\u8f91", "Role", userBriefVO.getUserName(), "\u7ed9\u7528\u6237\u5206\u914d\u89d2\u8272\uff0c" + JSON.toJSONString((Object)assignDTO)));
            this.packAndSaveMessage(oplogId, oldRoleIdList, assignDTO);
        } else {
            Integer roleId = assignDTO.getId();
            List<Integer> oldUserIdList = this.userRoleService.getUserIdListByRoleId(roleId);
            this.userRoleService.updateUserRoleByRoleId(roleId, assignDTO.getIdList());
            Role updateLastReviserById = new Role();
            updateLastReviserById.setId(roleId);
            UserBriefVO userBriefVO = this.userService.getUserBriefByUserName(HttpRequestUtil.getOperator(request));
            if (userBriefVO != null) {
                updateLastReviserById.setLastReviser(userBriefVO.getUserName());
            }
            this.roleDao.update(updateLastReviserById);
            Role role = this.roleDao.selectByRoleId(assignDTO.getId());
            Integer oplogId = this.oplogService.saveOplog(new OplogDTO(operator, "\u7f16\u8f91", "Role", role.getRoleName(), "\u7ed9\u89d2\u8272\u5206\u914d\u7528\u6237\uff0c" + JSON.toJSONString((Object)assignDTO)));
            this.packAndSaveMessage(oplogId, oldUserIdList, assignDTO);
        }
    }

    @Override
    public List<RoleBriefVO> getRoleBriefListByRoleName(String roleName) {
        List<RoleBrief> roleBriefList = this.roleDao.selectBriefListByRoleNameAndDescOrderByCreateTime(roleName);
        return CopyBeanUtil.copyList(roleBriefList, RoleBriefVO.class);
    }

    @Override
    public RoleDeleteCheckVO checkBeforeDelete(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        RoleDeleteCheckVO roleDeleteCheckVO = new RoleDeleteCheckVO();
        roleDeleteCheckVO.setRoleId(roleId);
        List<Integer> userIdList = this.userRoleService.getUserIdListByRoleId(roleId);
        if (!CollectionUtils.isEmpty(userIdList)) {
            List<UserBriefVO> list = this.userService.getUserBriefListByUserIdList(userIdList);
            List<String> usernameList = list.stream().map(UserBriefVO::getUserName).collect(Collectors.toList());
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
    public List<RoleBriefVO> getRoleBriefListByUserId(Integer userId) {
        List<Integer> roleIdList = this.userRoleService.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new ArrayList<RoleBriefVO>();
        }
        List<RoleBrief> roleBriefList = this.roleDao.selectBriefListByRoleIdList(roleIdList);
        return CopyBeanUtil.copyList(roleBriefList, RoleBriefVO.class);
    }

    @Override
    public Map<Integer, List<RoleBriefVO>> getRoleBriefListByUserIds(List<Integer> userId) {
        List<UserRole> userRoleList = this.userRoleService.getRoleIdListByUserIds(userId);
        List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleBriefVO> roleBriefs = CopyBeanUtil.copyList(this.roleDao.selectBriefListByRoleIdList(roleIds), RoleBriefVO.class);
        Map<Integer, RoleBriefVO> roleId2RoleMap = roleBriefs.stream().collect(Collectors.toMap(RoleBriefVO::getId, i -> i));
        return userRoleList.stream().collect(Collectors.groupingBy(UserRole::getUserId, Collectors.mapping(i -> (RoleBriefVO)roleId2RoleMap.get(i.getRoleId()), Collectors.toList())));
    }

    @Override
    public List<AssignInfoVO> getAssignInfoByRoleId(Integer roleId) {
        if (roleId == null) {
            return new ArrayList<AssignInfoVO>();
        }
        List<UserBriefVO> userBriefVOList = this.userService.getAllUserBriefList();
        List<Integer> userIdList = this.userRoleService.getUserIdListByRoleId(roleId);
        HashSet<Integer> hasRoleUserIdSet = new HashSet<Integer>(userIdList);
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

    private void packAndSaveMessage(Integer oplogId, List<Integer> oldIdList, RoleAssignDTO roleAssignDTO) {
        List<Integer> newIdList = roleAssignDTO.getIdList();
        ArrayList<Integer> removeIdList = new ArrayList<Integer>();
        ArrayList<Integer> addIdList = new ArrayList<Integer>();
        Set<Integer> set = MathUtil.getIntersection(oldIdList, newIdList);
        for (Integer oldId : oldIdList) {
            if (set.contains(oldId)) continue;
            removeIdList.add(oldId);
        }
        for (Integer newId : newIdList) {
            if (set.contains(newId)) continue;
            addIdList.add(newId);
        }
        if (Boolean.TRUE.equals(roleAssignDTO.getFlag())) {
            ArrayList<Integer> userIdList = new ArrayList<Integer>();
            userIdList.add(roleAssignDTO.getId());
            this.saveRoleAssignMessage(oplogId, userIdList, removeIdList, userIdList, addIdList);
        } else {
            ArrayList<Integer> roleIdList = new ArrayList<Integer>();
            roleIdList.add(roleAssignDTO.getId());
            this.saveRoleAssignMessage(oplogId, removeIdList, roleIdList, addIdList, roleIdList);
        }
    }

    private void saveRoleAssignMessage(Integer oplogId, List<Integer> removeUserIdList, List<Integer> removeRoleIdList, List<Integer> addUserIdList, List<Integer> addRoleIdList) {
        String content;
        MessageDTO messageDTO;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);
        String addRoleInfo = this.spliceRoleNameByRoleIdList(addRoleIdList);
        String removeRoleInfo = this.spliceRoleNameByRoleIdList(removeRoleIdList);
        ArrayList<MessageDTO> messageDTOList = new ArrayList<MessageDTO>();
        if (!StringUtils.isEmpty((Object)addRoleInfo)) {
            for (Integer userId : addUserIdList) {
                messageDTO = new MessageDTO(userId, oplogId);
                content = String.format(MessageCode.ROLE_ADD_MESSAGE.getContent(), time, addRoleInfo);
                messageDTO.setContent(content);
                messageDTO.setTitle(MessageCode.ROLE_ADD_MESSAGE.getTitle());
                messageDTOList.add(messageDTO);
            }
        }
        if (!StringUtils.isEmpty((Object)removeRoleInfo)) {
            for (Integer userId : removeUserIdList) {
                messageDTO = new MessageDTO(userId, oplogId);
                content = String.format(MessageCode.ROLE_REMOVE_MESSAGE.getContent(), time, removeRoleInfo);
                messageDTO.setContent(content);
                messageDTO.setTitle(MessageCode.ROLE_REMOVE_MESSAGE.getTitle());
                messageDTOList.add(messageDTO);
            }
        }
        this.messageService.saveMessages(messageDTOList);
    }

    private String spliceRoleNameByRoleIdList(List<Integer> roleIdList) {
        List<RoleBrief> roleBriefList = this.roleDao.selectBriefListByRoleIdList(roleIdList);
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

    private void checkParam(RoleSaveDTO saveDTO, boolean isUpdate) throws LogiSecurityException {
        if (StringUtils.isEmpty((Object)saveDTO.getRoleName())) {
            throw new LogiSecurityException(ResultCode.ROLE_NAME_CANNOT_BE_BLANK);
        }
        if (CollectionUtils.isEmpty(saveDTO.getPermissionIdList())) {
            throw new LogiSecurityException(ResultCode.ROLE_PERMISSION_CANNOT_BE_NULL);
        }
        Integer roleId = isUpdate ? saveDTO.getId() : null;
        int count = this.roleDao.selectCountByRoleNameAndNotRoleId(saveDTO.getRoleName(), roleId);
        if (count > 0) {
            throw new LogiSecurityException(ResultCode.ROLE_NAME_ALREADY_EXISTS);
        }
    }
}

