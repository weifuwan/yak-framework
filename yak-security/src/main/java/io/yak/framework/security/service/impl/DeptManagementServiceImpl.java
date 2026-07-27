package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.dept.DeptSaveDTO;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.user.UserBrief;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.dept.DeptDeleteCheckVO;
import io.yak.framework.security.common.vo.dept.DeptVO;
import io.yak.framework.security.dao.DeptDao;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.DeptManagementService;
import io.yak.framework.security.util.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 部门单节点管理服务实现。
 */
@Service("yakSecurityDeptManagementServiceImpl")
public class DeptManagementServiceImpl
        implements DeptManagementService {

  private static final Long ROOT_DEPT_ID = 0L;
  private static final int DEPT_ID_RANDOM_LENGTH = 5;
  private static final long DEPT_ID_FACTOR = 100_000L;
  private static final int MAX_ID_RETRY_COUNT = 100;

  private final DeptDao deptDao;
  private final UserDao userDao;

  public DeptManagementServiceImpl(
          DeptDao deptDao,
          UserDao userDao) {
    this.deptDao = deptDao;
    this.userDao = userDao;
  }

  @Override
  public DeptVO getDeptDetail(Long deptId) {
    Dept dept = getRequiredDept(deptId);
    List<Long> childIds =
            deptDao.selectIdListByParentId(dept.getId());
    List<UserBrief> users = getUsersByDeptId(dept.getId());

    DeptVO result = new DeptVO();
    result.setId(dept.getId());
    result.setDeptName(dept.getDeptName());
    result.setDescription(dept.getDescription());
    result.setParentId(normalizeParentId(dept.getParentId()));
    result.setLeaf(dept.getLeaf());
    result.setLevel(dept.getLevel());
    result.setChildDeptCount(
            childIds == null ? 0 : childIds.size());
    result.setUserCount(users.size());
    return result;
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void createDept(DeptSaveDTO saveDTO) {
    if (saveDTO == null) {
      throw new IllegalArgumentException("部门信息不能为空");
    }

    String deptName = normalizeDeptName(saveDTO.getDeptName());
    Long parentId = normalizeParentId(saveDTO.getParentId());
    Dept parent = getParentDept(parentId);

    checkDuplicateName(parentId, deptName, null);

    Dept dept = new Dept();
    dept.setId(generateUniqueDeptId());
    dept.setDeptName(deptName);
    dept.setDescription(normalizeDescription(saveDTO.getDescription()));
    dept.setParentId(parentId);
    dept.setLeaf(Boolean.TRUE);
    dept.setLevel(parent == null ? 1 : safeLevel(parent) + 1);

    if (deptDao.insert(dept) != 1) {
      throw new IllegalStateException("新增部门失败");
    }

    markParentAsGroup(parentId);
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void updateDept(DeptSaveDTO saveDTO) {
    if (saveDTO == null || saveDTO.getId() == null) {
      throw new IllegalArgumentException("部门信息和部门 ID 不能为空");
    }

    Dept current = getRequiredDept(saveDTO.getId());
    String deptName = normalizeDeptName(saveDTO.getDeptName());
    Long newParentId = normalizeParentId(saveDTO.getParentId());
    Long oldParentId = normalizeParentId(current.getParentId());

    List<Dept> allDepartments = safeDepartments(
            deptDao.selectAllAndAscOrderByLevel());
    Set<Long> subtreeIds = collectSubtreeIds(
            allDepartments,
            current.getId());

    if (Objects.equals(current.getId(), newParentId)
            || subtreeIds.contains(newParentId)) {
      throw new YakSecurityException(
              ResultCode.DEPT_PARENT_INVALID);
    }

    Dept newParent = getParentDept(newParentId);
    checkDuplicateName(
            newParentId,
            deptName,
            current.getId());

    int oldLevel = safeLevel(current);
    int newLevel = newParent == null
            ? 1
            : safeLevel(newParent) + 1;
    int levelDelta = newLevel - oldLevel;

    Dept update = new Dept();
    update.setId(current.getId());
    update.setDeptName(deptName);
    update.setDescription(
            normalizeDescription(saveDTO.getDescription()));
    update.setParentId(newParentId);
    update.setLeaf(resolveLeaf(current));
    update.setLevel(newLevel);

    if (deptDao.update(update) != 1) {
      throw new IllegalStateException("编辑部门失败");
    }

    if (levelDelta != 0) {
      updateDescendantLevels(
              allDepartments,
              subtreeIds,
              current.getId(),
              levelDelta);
    }

    if (!Objects.equals(oldParentId, newParentId)) {
      refreshParentLeaf(oldParentId);
      markParentAsGroup(newParentId);
    }
  }

  @Override
  public DeptDeleteCheckVO checkBeforeDelete(Long deptId) {
    Dept dept = getRequiredDept(deptId);
    List<Long> childIds = safeIds(
            deptDao.selectIdListByParentId(dept.getId()));
    List<UserBrief> users = getUsersByDeptId(dept.getId());

    DeptDeleteCheckVO result = new DeptDeleteCheckVO();
    result.setDeptId(dept.getId());

    List<String> childNames = new ArrayList<>();
    for (Long childId : childIds) {
      Dept child = deptDao.selectByDeptId(childId);
      if (child != null && StringUtils.hasText(child.getDeptName())) {
        childNames.add(child.getDeptName());
      }
    }

    List<String> userNames = new ArrayList<>();
    for (UserBrief user : users) {
      if (user != null) {
        userNames.add(formatUserName(user));
      }
    }

    result.setChildDeptNameList(childNames);
    result.setUserNameList(userNames);
    result.setDeletable(
            childNames.isEmpty() && userNames.isEmpty());
    return result;
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void deleteDept(Long deptId) {
    Dept dept = getRequiredDept(deptId);
    DeptDeleteCheckVO check = checkBeforeDelete(deptId);

    if (!CollectionUtils.isEmpty(
            check.getChildDeptNameList())) {
      throw new YakSecurityException(
              ResultCode.DEPT_HAS_CHILDREN);
    }

    if (!CollectionUtils.isEmpty(check.getUserNameList())) {
      throw new YakSecurityException(
              ResultCode.DEPT_HAS_USERS);
    }

    if (deptDao.deleteById(deptId) != 1) {
      throw new IllegalStateException("删除部门失败");
    }

    refreshParentLeaf(
            normalizeParentId(dept.getParentId()));
  }

  private Dept getRequiredDept(Long deptId) {
    if (deptId == null
            || Objects.equals(ROOT_DEPT_ID, deptId)) {
      throw new YakSecurityException(
              ResultCode.DEPT_NOT_EXISTS);
    }

    Dept dept = deptDao.selectByDeptId(deptId);
    if (dept == null) {
      throw new YakSecurityException(
              ResultCode.DEPT_NOT_EXISTS);
    }
    return dept;
  }

  private Dept getParentDept(Long parentId) {
    if (Objects.equals(ROOT_DEPT_ID, parentId)) {
      return null;
    }

    Dept parent = deptDao.selectByDeptId(parentId);
    if (parent == null) {
      throw new YakSecurityException(
              ResultCode.DEPT_PARENT_NOT_EXISTS);
    }
    return parent;
  }

  private void checkDuplicateName(
          Long parentId,
          String deptName,
          Long excludeDeptId) {
    if (deptDao.existsByParentIdAndDeptName(
            parentId,
            deptName,
            excludeDeptId)) {
      throw new YakSecurityException(
              ResultCode.DEPT_NAME_ALREADY_EXISTS);
    }
  }

  private String normalizeDeptName(String deptName) {
    if (!StringUtils.hasText(deptName)) {
      throw new YakSecurityException(
              ResultCode.DEPT_NAME_CANNOT_BE_BLANK);
    }
    return deptName.trim();
  }

  private String normalizeDescription(String description) {
    return description == null ? "" : description.trim();
  }

  private Long normalizeParentId(Long parentId) {
    return parentId == null ? ROOT_DEPT_ID : parentId;
  }

  private int safeLevel(Dept dept) {
    return dept.getLevel() == null ? 0 : dept.getLevel();
  }

  private boolean resolveLeaf(Dept dept) {
    if (Boolean.FALSE.equals(dept.getLeaf())) {
      return false;
    }
    return safeIds(
            deptDao.selectIdListByParentId(dept.getId()))
            .isEmpty();
  }

  private void markParentAsGroup(Long parentId) {
    if (Objects.equals(ROOT_DEPT_ID, parentId)) {
      return;
    }

    Dept parent = new Dept();
    parent.setId(parentId);
    parent.setLeaf(Boolean.FALSE);
    deptDao.update(parent);
  }

  private void refreshParentLeaf(Long parentId) {
    if (Objects.equals(ROOT_DEPT_ID, parentId)) {
      return;
    }

    Dept parent = deptDao.selectByDeptId(parentId);
    if (parent == null) {
      return;
    }

    Dept update = new Dept();
    update.setId(parentId);
    update.setLeaf(
            safeIds(
                    deptDao.selectIdListByParentId(parentId))
                    .isEmpty());
    deptDao.update(update);
  }

  private void updateDescendantLevels(
          List<Dept> allDepartments,
          Set<Long> subtreeIds,
          Long currentDeptId,
          int levelDelta) {

    for (Dept dept : allDepartments) {
      if (dept == null
              || dept.getId() == null
              || Objects.equals(currentDeptId, dept.getId())
              || !subtreeIds.contains(dept.getId())) {
        continue;
      }

      Dept update = new Dept();
      update.setId(dept.getId());
      update.setLevel(safeLevel(dept) + levelDelta);
      deptDao.update(update);
    }
  }

  private Set<Long> collectSubtreeIds(
          List<Dept> departments,
          Long rootDeptId) {

    Map<Long, List<Long>> childMap = new HashMap<>();
    for (Dept dept : departments) {
      if (dept == null || dept.getId() == null) {
        continue;
      }
      Long parentId = normalizeParentId(dept.getParentId());
      childMap.computeIfAbsent(
              parentId,
              ignored -> new ArrayList<>())
              .add(dept.getId());
    }

    Set<Long> result = new LinkedHashSet<>();
    Deque<Long> queue = new ArrayDeque<>();
    queue.add(rootDeptId);

    while (!queue.isEmpty()) {
      Long current = queue.removeFirst();
      if (!result.add(current)) {
        continue;
      }
      List<Long> children = childMap.get(current);
      if (children != null) {
        queue.addAll(children);
      }
    }
    return result;
  }

  private List<UserBrief> getUsersByDeptId(Long deptId) {
    List<UserBrief> users =
            userDao.selectBriefListByDeptIdList(
                    Collections.singletonList(deptId));
    return users == null ? new ArrayList<>() : users;
  }

  private String formatUserName(UserBrief user) {
    String userName = user.getUserName();
    String realName = user.getRealName();

    if (StringUtils.hasText(realName)
            && StringUtils.hasText(userName)) {
      return realName.trim() + "（" + userName.trim() + "）";
    }
    if (StringUtils.hasText(realName)) {
      return realName.trim();
    }
    if (StringUtils.hasText(userName)) {
      return userName.trim();
    }
    return String.valueOf(user.getId());
  }

  private List<Long> safeIds(List<Long> ids) {
    return ids == null ? new ArrayList<>() : ids;
  }

  private List<Dept> safeDepartments(List<Dept> departments) {
    return departments == null ? new ArrayList<>() : departments;
  }

  private long generateUniqueDeptId() {
    Set<Long> generated = new HashSet<>();
    for (int index = 0;
         index < MAX_ID_RETRY_COUNT;
         index++) {
      long deptId = System.currentTimeMillis() % 1_000L
              * DEPT_ID_FACTOR
              + MathUtil.getRandomNumber(
              DEPT_ID_RANDOM_LENGTH);

      if (!Objects.equals(ROOT_DEPT_ID, deptId)
              && generated.add(deptId)
              && deptDao.selectByDeptId(deptId) == null) {
        return deptId;
      }
    }
    throw new IllegalStateException("生成部门 ID 失败");
  }
}
