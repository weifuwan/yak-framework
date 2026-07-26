package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.PermissionService;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.MathUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityPermissionServiceImpl")
public class PermissionServiceImpl implements PermissionService {
  @Autowired private PermissionDao permissionDao;
  @Autowired private RolePermissionService rolePermissionService;

  private PermissionTreeVO buildPermissionTree(Set<Long> permissionHasSet)
      throws YakSecurityException {
    List<Permission> permissionList =
        this.permissionDao.selectAllAndAscOrderByLevel();
    PermissionTreeVO root = PermissionTreeVO.builder()
                                .leaf(false)
                                .has(true)
                                .id(0)
                                .childList(new ArrayList<PermissionTreeVO>())
                                .build();
    HashMap<Long, PermissionTreeVO> parentMap =
        new HashMap<Long, PermissionTreeVO>(permissionList.size());
    parentMap.put(0, root);
    for (Permission permission : permissionList) {
      PermissionTreeVO parent;
      PermissionTreeVO permissionTreeVO =
          CopyBeanUtil.copy(permission, PermissionTreeVO.class);
      if (permission.getLeaf() != null &&
          !permission.getLeaf().booleanValue()) {
        permissionTreeVO.setChildList(new ArrayList<PermissionTreeVO>());
      }
      if ((parent = (PermissionTreeVO)parentMap.get(
               permission.getParentId())) == null) {
        throw new YakSecurityException(ResultCode.PERMISSION_DATA_ERROR);
      }
      permissionTreeVO.setHas(parent.getHas() != false &&
                              permissionHasSet.contains(permission.getId()));
      parent.getChildList().add(permissionTreeVO);
      parentMap.put(permissionTreeVO.getId(), permissionTreeVO);
    }
    return root;
  }

  @Override
  public PermissionTreeVO
  buildPermissionTreeWithHas(List<Long> permissionHasList) {
    PermissionTreeVO permissionTreeVO = null;
    try {
      permissionTreeVO =
          this.buildPermissionTree(new HashSet<Long>(permissionHasList));
    } catch (YakSecurityException e) {
      e.printStackTrace();
    }
    return permissionTreeVO;
  }

  @Override
  public PermissionTreeVO buildPermissionTree() {
    return this.buildPermissionTreeWithHas(new ArrayList<Long>());
  }

  @Override
  public PermissionTreeVO buildPermissionTreeByRoleId(Long roleId) {
    List<Long> permissionIdList =
        this.rolePermissionService.getPermissionIdListByRoleId(roleId);
    return this.buildPermissionTreeWithHas(permissionIdList);
  }

  @Override
  public void savePermission(List<PermissionDTO> permissionDTOList) {
    if (CollectionUtils.isEmpty(permissionDTOList)) {
      return;
    }
    ArrayList<Permission> permissionList = new ArrayList<Permission>();
    HashMap<PermissionDTO, Integer> permissionDTOMap =
        new HashMap<PermissionDTO, Integer>();
    LinkedList<PermissionDTO> queue = new LinkedList<PermissionDTO>();
    PermissionDTO permissionDTO = new PermissionDTO();
    permissionDTO.setChildPermissionDTOList(permissionDTOList);
    queue.offer(permissionDTO);
    int level = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        PermissionDTO dto = (PermissionDTO)queue.poll();
        if (dto == null)
          continue;
        Permission permission = CopyBeanUtil.copy(dto, Permission.class);
        permission.setLevel(level);
        permission.setParentId((Integer)permissionDTOMap.get(dto));
        permission.setLeaf(
            CollectionUtils.isEmpty(dto.getChildPermissionDTOList()));
        permission.setId(0);
        if (level > 0) {
          permission.setId((int)this.getPermissionId());
          permissionList.add(permission);
        }
        for (PermissionDTO temp : dto.getChildPermissionDTOList()) {
          permissionDTOMap.put(temp, permission.getId());
          queue.offer(temp);
        }
      }
      ++level;
    }
    this.permissionDao.insertBatch(permissionList);
  }

  private long getPermissionId() {
    return System.currentTimeMillis() % 1000L * (long)Math.pow(10.0, 5.0) +
        MathUtil.getRandomNumber(5);
  }
}
