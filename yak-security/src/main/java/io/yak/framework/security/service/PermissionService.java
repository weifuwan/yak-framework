package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import java.util.List;

public interface PermissionService {
  public PermissionTreeVO buildPermissionTreeWithHas(List<Long> var1);

  public PermissionTreeVO buildPermissionTree();

  public PermissionTreeVO buildPermissionTreeByRoleId(Integer var1);

  public void savePermission(List<PermissionDTO> var1);
}
