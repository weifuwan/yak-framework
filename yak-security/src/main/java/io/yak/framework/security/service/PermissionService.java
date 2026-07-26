package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import java.util.List;

/**
 * 权限服务接口。
 */
public interface PermissionService {
  /**
   * 构建包含选中状态的权限树。
   */
  PermissionTreeVO buildPermissionTreeWithHas(List<Long> var1);

  /**
   * 构建权限树。
   */
  PermissionTreeVO buildPermissionTree();

  /**
   * 根据角色 ID 构建权限树。
   */
  PermissionTreeVO buildPermissionTreeByRoleId(Long var1);

  /**
   * 保存权限信息。
   */
  void savePermission(List<PermissionDTO> var1);
}
