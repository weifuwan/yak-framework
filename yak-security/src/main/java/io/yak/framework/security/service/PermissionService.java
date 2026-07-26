package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import java.util.List;

/**
 * 权限服务接口。
 */
public interface PermissionService {
  PermissionTreeVO buildPermissionTreeWithHas(List<Long> var1);

  PermissionTreeVO buildPermissionTree();

  PermissionTreeVO buildPermissionTreeByRoleId(Long var1);

  void savePermission(List<PermissionDTO> var1);
}
