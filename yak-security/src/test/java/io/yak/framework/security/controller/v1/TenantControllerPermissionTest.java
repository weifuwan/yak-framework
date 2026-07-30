package io.yak.framework.security.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.dto.tenant.TenantMemberAssignDTO;
import io.yak.framework.security.common.dto.tenant.TenantSaveDTO;
import io.yak.framework.security.permission.YakPermission;
import io.yak.framework.security.web.RequiresPermission;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class TenantControllerPermissionTest {

  @Test
  void tenantControllerDeclaresStablePermissions()
      throws Exception {
    RequiresPermission controllerPermission =
        TenantController.class.getAnnotation(
            RequiresPermission.class);
    YakPermission controllerDeclaration =
        TenantController.class.getAnnotation(
            YakPermission.class);

    assertThat(controllerPermission.value())
        .isEqualTo(
            SecurityPermissionCode.Tenant.READ);
    assertThat(controllerDeclaration.menuCode())
        .isEqualTo(
            SecurityPermissionCode.Tenant.MENU_CODE);

    assertPermission(
        TenantController.class.getDeclaredMethod(
            "create",
            TenantSaveDTO.class),
        SecurityPermissionCode.Tenant.CREATE);
    assertPermission(
        TenantController.class.getDeclaredMethod(
            "update",
            TenantSaveDTO.class),
        SecurityPermissionCode.Tenant.UPDATE);
    assertPermission(
        TenantController.class.getDeclaredMethod(
            "upsertExternal",
            TenantSaveDTO.class),
        SecurityPermissionCode.Tenant.SYNC);
    assertPermission(
        TenantController.class.getDeclaredMethod(
            "replaceMembers",
            Long.class,
            TenantMemberAssignDTO.class),
        SecurityPermissionCode.Tenant.ASSIGN);
    assertPermission(
        TenantController.class.getDeclaredMethod(
            "delete",
            Long.class),
        SecurityPermissionCode.Tenant.DELETE);
  }

  private void assertPermission(
      Method method,
      String permissionCode) {
    RequiresPermission requiresPermission =
        method.getAnnotation(
            RequiresPermission.class);
    YakPermission yakPermission =
        method.getAnnotation(
            YakPermission.class);

    assertThat(requiresPermission.value())
        .isEqualTo(permissionCode);
    assertThat(yakPermission.code())
        .isEqualTo(permissionCode);
    assertThat(yakPermission.menuCode())
        .isEqualTo(
            SecurityPermissionCode.Tenant.MENU_CODE);
  }
}
