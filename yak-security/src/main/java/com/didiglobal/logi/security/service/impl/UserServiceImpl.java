/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.google.common.collect.Lists
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.didiglobal.logi.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.user.UserBriefQueryDTO;
import com.didiglobal.logi.security.common.dto.user.UserDTO;
import com.didiglobal.logi.security.common.dto.user.UserQueryDTO;
import com.didiglobal.logi.security.common.entity.BaseEntity;
import com.didiglobal.logi.security.common.entity.user.User;
import com.didiglobal.logi.security.common.entity.user.UserBrief;
import com.didiglobal.logi.security.common.enums.ResultCode;
import com.didiglobal.logi.security.common.enums.user.UserCheckType;
import com.didiglobal.logi.security.common.po.UserPO;
import com.didiglobal.logi.security.common.po.UserProjectPO;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVO;
import com.didiglobal.logi.security.common.vo.role.AssignInfoVO;
import com.didiglobal.logi.security.common.vo.role.RoleBriefVO;
import com.didiglobal.logi.security.common.vo.user.UserBasicVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.common.vo.user.UserVO;
import com.didiglobal.logi.security.dao.ProjectDao;
import com.didiglobal.logi.security.dao.UserDao;
import com.didiglobal.logi.security.dao.UserProjectDao;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.service.DeptService;
import com.didiglobal.logi.security.service.PermissionService;
import com.didiglobal.logi.security.service.RolePermissionService;
import com.didiglobal.logi.security.service.RoleService;
import com.didiglobal.logi.security.service.UserRoleService;
import com.didiglobal.logi.security.service.UserService;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service(value="logiSecurityUserServiceImpl")
public class UserServiceImpl
implements UserService {
    private static final Pattern P_USER_NAME = Pattern.compile("^[0-9a-zA-Z_]{5,50}$");
    private static final Pattern P_USER_PHONE = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
    private static final Pattern P_USER_MAIL = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserProjectDao userProjectDao;
    @Autowired
    private ProjectDao projectDao;

    @Override
    public Result<Void> check(Integer type, String value) {
        if (UserCheckType.USER_NAME.getCode() == type.intValue()) {
            return this.userNameCheck(value);
        }
        if (UserCheckType.USER_PHONE.getCode() == type.intValue()) {
            return this.userPhoneCheck(value);
        }
        if (UserCheckType.USER_MAIL.getCode() == type.intValue()) {
            return this.userMailCheck(value);
        }
        return Result.fail("\u6821\u9a8c\u7c7b\u578b\u53c2\u6570\u4e0d\u6b63\u786e");
    }

    @Override
    public PagingData<UserVO> getUserPage(UserQueryDTO queryDTO) {
        List<Integer> userIdList = null;
        if (queryDTO.getRoleId() != null && CollectionUtils.isEmpty(userIdList = this.userRoleService.getUserIdListByRoleId(queryDTO.getRoleId()))) {
            return new PagingData<UserVO>((IPage<?>)new Page((long)queryDTO.getPage(), (long)queryDTO.getSize()));
        }
        IPage<User> pageInfo = this.userDao.selectPageByUserIdList(queryDTO, userIdList);
        ArrayList userVOList = Lists.newArrayList();
        List<Integer> userIds = pageInfo.getRecords().stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<UserProjectPO> userProjectList = this.userProjectDao.selectProjectListByUserIdList(userIds);
        List<Integer> projectIds = userProjectList.stream().map(UserProjectPO::getProjectId).distinct().collect(Collectors.toList());
        List<ProjectBriefVO> projects = CopyBeanUtil.copyList(this.projectDao.selectProjectBriefByProjectIds(projectIds), ProjectBriefVO.class);
        Map<Integer, ProjectBriefVO> projectId2ProjectMap = projects.stream().collect(Collectors.toMap(ProjectBriefVO::getId, i -> i));
        Map userId2ProjectListMap = userProjectList.stream().filter(i -> projectId2ProjectMap.containsKey(i.getProjectId())).collect(Collectors.groupingBy(UserProjectPO::getUserId, Collectors.mapping(i -> (ProjectBriefVO)projectId2ProjectMap.get(i.getProjectId()), Collectors.toSet())));
        Map<Integer, List<RoleBriefVO>> userId2RoleListMap = this.roleService.getRoleBriefListByUserIds(userIds);
        List userList = pageInfo.getRecords();
        for (User user : userList) {
            UserVO userVo = CopyBeanUtil.copy(user, UserVO.class);
            userVo.setRoleList(userId2RoleListMap.getOrDefault(user.getId(), Collections.emptyList()));
            userVo.setUpdateTime(user.getUpdateTime());
            userVo.setCreateTime(user.getCreateTime());
            this.privacyProcessing(userVo);
            userVOList.add(userVo);
            userVo.setProjectList(Lists.newArrayList((Iterable)userId2ProjectListMap.getOrDefault(user.getId(), Collections.emptySet())));
        }
        return new PagingData<UserVO>(userVOList, pageInfo);
    }

    @Override
    public PagingData<UserBriefVO> getUserBriefPage(UserBriefQueryDTO queryDTO) {
        List<Integer> deptIdList = this.deptService.getDeptIdListByParentIdAndDeptName(queryDTO.getDeptId(), queryDTO.getDeptName());
        IPage<UserBrief> pageInfo = this.userDao.selectBriefPageByDeptIdList(queryDTO, deptIdList);
        List<UserBriefVO> userBriefVOList = CopyBeanUtil.copyList(pageInfo.getRecords(), UserBriefVO.class);
        return new PagingData<UserBriefVO>(userBriefVOList, pageInfo);
    }

    @Override
    public UserVO getUserDetailByUserId(Integer userId) throws LogiSecurityException {
        User user = this.userDao.selectByUserId(userId);
        if (user == null) {
            throw new LogiSecurityException(ResultCode.USER_NOT_EXISTS);
        }
        UserVO userVo = CopyBeanUtil.copy(user, UserVO.class);
        List<RoleBriefVO> roleBriefVOList = this.roleService.getRoleBriefListByUserId(userId);
        userVo.setRoleList(roleBriefVOList);
        List<Integer> roleIdList = roleBriefVOList.stream().map(RoleBriefVO::getId).collect(Collectors.toList());
        List<Integer> hasPermissionIdList = this.rolePermissionService.getPermissionIdListByRoleIdList(roleIdList);
        userVo.setPermissionTreeVO(this.permissionService.buildPermissionTreeWithHas(hasPermissionIdList));
        List<ProjectBriefVO> projectBriefVOS = this.userProjectDao.selectProjectIdListByUserIdList(Collections.singletonList(userVo.getId())).stream().map(this.projectDao::selectByProjectId).map(project -> CopyBeanUtil.copy(project, ProjectBriefVO.class)).filter(Objects::nonNull).collect(Collectors.toList());
        userVo.setProjectList(projectBriefVOS);
        userVo.setUpdateTime(user.getUpdateTime());
        userVo.setCreateTime(user.getCreateTime());
        return userVo;
    }

    @Override
    public List<UserBasicVO> getUserBasicListByUserIdList(List<Integer> userIds) {
        return CopyBeanUtil.copyList(this.userDao.selectBriefListByUserIdList(userIds), UserBasicVO.class);
    }

    @Override
    public Result<List<UserVO>> getUserDetailByUserIds(List<Integer> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            return Result.buildSucc(Lists.newArrayList());
        }
        List userVOS = ids.stream().distinct().map(this.userDao::selectByUserId).filter(Objects::nonNull).map(user -> {
            UserVO userVO = CopyBeanUtil.copy(user, UserVO.class);
            userVO.setUpdateTime(user.getUpdateTime());
            userVO.setCreateTime(user.getCreateTime());
            return userVO;
        }).collect(Collectors.toList());
        List<UserProjectPO> userProjectList = this.userProjectDao.selectProjectListByUserIdList(ids);
        List<Integer> projectIds = userProjectList.stream().map(UserProjectPO::getProjectId).distinct().collect(Collectors.toList());
        List<ProjectBriefVO> projects = CopyBeanUtil.copyList(this.projectDao.selectProjectBriefByProjectIds(projectIds), ProjectBriefVO.class);
        Map<Integer, ProjectBriefVO> projectId2ProjectMap = projects.stream().collect(Collectors.toMap(ProjectBriefVO::getId, i -> i));
        Map userId2ProjectListMap = userProjectList.stream().collect(Collectors.groupingBy(UserProjectPO::getUserId, Collectors.mapping(i -> (ProjectBriefVO)projectId2ProjectMap.get(i.getProjectId()), Collectors.toSet())));
        for (UserVO userVO : userVOS) {
            List<RoleBriefVO> roleBriefVOList = this.roleService.getRoleBriefListByUserId(userVO.getId());
            userVO.setRoleList(roleBriefVOList);
            List<Integer> roleIdList = roleBriefVOList.stream().map(RoleBriefVO::getId).collect(Collectors.toList());
            List<Integer> hasPermissionIdList = this.rolePermissionService.getPermissionIdListByRoleIdList(roleIdList);
            userVO.setPermissionTreeVO(this.permissionService.buildPermissionTreeWithHas(hasPermissionIdList));
            userVO.setProjectList(Lists.newArrayList((Iterable)userId2ProjectListMap.get(userVO.getId())));
        }
        return Result.buildSucc(userVOS);
    }

    @Override
    public Result<Void> deleteByUserId(Integer userId) {
        if (userId == null) {
            return Result.fail("userId is null!");
        }
        boolean success = this.userDao.deleteByUserId(userId);
        if (success) {
            this.userRoleService.deleteByUserIdOrRoleId(userId, null);
        }
        return success ? Result.success() : Result.fail();
    }

    @Override
    public UserBriefVO getUserBriefByUserName(String userName) {
        if (StringUtils.isEmpty((Object)userName)) {
            return null;
        }
        User user = this.userDao.selectByUsername(userName);
        return CopyBeanUtil.copy(user, UserBriefVO.class);
    }

    @Override
    public User getUserByUserName(String userName) {
        return this.userDao.selectByUsername(userName);
    }

    @Override
    public List<UserBriefVO> getUserBriefListByUserIdList(List<Integer> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<UserBriefVO>();
        }
        List<UserBrief> userBriefList = this.userDao.selectBriefListByUserIdList(userIdList);
        List<UserBriefVO> userBriefVOS = CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
        for (UserBriefVO userBriefVO : userBriefVOS) {
            User user = this.userDao.selectByUserId(userBriefVO.getId());
            userBriefVO.setEmail(user.getEmail());
            userBriefVO.setPhone(user.getPhone());
            List<String> roles = this.roleService.getRoleBriefListByUserId(userBriefVO.getId()).stream().map(RoleBriefVO::getRoleName).collect(Collectors.toList());
            userBriefVO.setRoleList(roles);
        }
        return userBriefVOS;
    }

    @Override
    public List<UserBriefVO> getUserBriefListByUsernameOrRealName(String name) {
        List<UserBrief> userBriefList = this.userDao.selectBriefListByNameAndDescOrderByCreateTime(name);
        return CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
    }

    @Override
    public List<UserBriefVO> getAllUserBriefListOrderByCreateTime(boolean isAsc) {
        List<UserBrief> userBriefList = this.userDao.selectBriefListOrderByCreateTime(isAsc);
        return CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
    }

    @Override
    public List<Integer> getUserIdListByUsernameOrRealName(String name) {
        return this.userDao.selectUserIdListByUsernameOrRealName(name);
    }

    @Override
    public List<UserBriefVO> getAllUserBriefList() {
        List<UserBrief> userBriefList = this.userDao.selectAllBriefList();
        return CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
    }

    @Override
    public List<UserBriefVO> getUserBriefListByDeptId(Integer deptId) {
        List<Integer> deptIdList = this.deptService.getDeptIdListByParentId(deptId);
        List<UserBrief> userBriefList = this.userDao.selectBriefListByDeptIdList(deptIdList);
        return CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
    }

    @Override
    public List<AssignInfoVO> getAssignDataByUserId(Integer userId) throws LogiSecurityException {
        if (userId == null) {
            throw new LogiSecurityException(ResultCode.USER_ID_CANNOT_BE_NULL);
        }
        List<RoleBriefVO> roleBriefVOList = this.roleService.getAllRoleBriefList();
        HashSet<Integer> hasRoleIdSet = new HashSet<Integer>(this.userRoleService.getRoleIdListByUserId(userId));
        ArrayList<AssignInfoVO> list = new ArrayList<AssignInfoVO>();
        for (RoleBriefVO roleBriefVO : roleBriefVOList) {
            AssignInfoVO data = new AssignInfoVO();
            data.setName(roleBriefVO.getRoleName());
            data.setId(roleBriefVO.getId());
            data.setHas(hasRoleIdSet.contains(roleBriefVO.getId()));
            list.add(data);
        }
        return list;
    }

    @Override
    public List<UserBriefVO> getUserBriefListByRoleId(Integer roleId) {
        List<Integer> userIdList = this.userRoleService.getUserIdListByRoleId(roleId);
        List<UserBrief> userBriefList = this.userDao.selectBriefListByUserIdList(userIdList);
        return CopyBeanUtil.copyList(userBriefList, UserBriefVO.class);
    }

    @Override
    public Result<Void> addUser(UserDTO userDTO, String operator) {
        if (null != this.userDao.selectByUsername(userDTO.getUserName())) {
            return Result.fail(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        try {
            UserPO userPO = CopyBeanUtil.copy(userDTO, UserPO.class);
            if (this.userDao.addUser(userPO) > 0) {
                this.userRoleService.updateUserRoleByUserId(userPO.getId(), userDTO.getRoleIds());
            }
        } catch (Exception e) {
            return Result.fail(ResultCode.USER_ACCOUNT_INSERT_FAIL);
        }
        return Result.success();
    }

    @Override
    public Result<Void> editUser(UserDTO userDTO, String operator) {
        User user = this.userDao.selectByUsername(userDTO.getUserName());
        if (null == user) {
            return Result.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        try {
            UserPO userPO = CopyBeanUtil.copy(userDTO, UserPO.class);
            userPO.setId(user.getId());
            if (this.userDao.editUser(userPO) > 0) {
                this.userRoleService.updateUserRoleByUserId(userPO.getId(), userDTO.getRoleIds());
            }
        } catch (Exception e) {
            return Result.fail(ResultCode.USER_ACCOUNT_UPDATE_FAIL);
        }
        return Result.success();
    }

    private void privacyProcessing(UserVO userVo) {
        String phone = userVo.getPhone();
        userVo.setPhone(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    }

    private Result<Void> userNameCheck(String userName) {
        if (!P_USER_NAME.matcher(userName).matches()) {
            return Result.fail(ResultCode.USER_NAME_FORMAT_ERROR);
        }
        if (null != this.userDao.selectByUsername(userName)) {
            return Result.fail(ResultCode.USER_NAME_EXISTS);
        }
        return Result.success();
    }

    private Result<Void> userPhoneCheck(String userPhone) {
        if (!P_USER_PHONE.matcher(userPhone).matches()) {
            return Result.fail(ResultCode.USER_NAME_FORMAT_ERROR);
        }
        if (null != this.userDao.selectByUserPhone(userPhone)) {
            return Result.fail(ResultCode.USER_PHONE_EXIST);
        }
        return Result.success();
    }

    private Result<Void> userMailCheck(String userMail) {
        if (!P_USER_MAIL.matcher(userMail).matches()) {
            return Result.fail(ResultCode.USER_EMAIL_FORMAT_ERROR);
        }
        if (null != this.userDao.selectByUserMail(userMail)) {
            return Result.fail(ResultCode.USER_EMAIL_EXIST);
        }
        return Result.success();
    }
}

