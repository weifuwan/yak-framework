package io.yak.framework.security.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.dto.dept.DeptSaveDTO;
import io.yak.framework.security.permission.YakPermission;
import io.yak.framework.security.web.RequiresPermission;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ManagementControllerPermissionTest {

  @Test
  void departmentControllerDeclaresReadAndActionPermissions() throws Exception {
    assertControllerPermission(
        DeptController.class,
        SecurityPermissionCode.Department.READ,
        SecurityPermissionCode.Department.MENU_CODE);

    assertActionPermission(
        DeptController.class.getDeclaredMethod("create", DeptSaveDTO.class),
        SecurityPermissionCode.Department.CREATE,
        SecurityPermissionCode.Department.MENU_CODE,
        true);
    assertActionPermission(
        DeptController.class.getDeclaredMethod("update", DeptSaveDTO.class),
        SecurityPermissionCode.Department.EDIT,
        SecurityPermissionCode.Department.MENU_CODE,
        true);
    assertActionPermission(
        DeptController.class.getDeclaredMethod("checkBeforeDelete", Long.class),
        SecurityPermissionCode.Department.DELETE,
        SecurityPermissionCode.Department.MENU_CODE,
        false);
    assertActionPermission(
        DeptController.class.getDeclaredMethod("delete", Long.class),
        SecurityPermissionCode.Department.DELETE,
        SecurityPermissionCode.Department.MENU_CODE,
        true);
    assertActionPermission(
        DeptController.class.getDeclaredMethod("importDept", List.class),
        SecurityPermissionCode.Department.IMPORT,
        SecurityPermissionCode.Department.MENU_CODE,
        true);
  }

  @Test
  void permissionControllerBindsDeclaredActionsToPermissionMenu() throws Exception {
    assertControllerPermission(
        PermissionController.class,
        SecurityPermissionCode.Permission.READ,
        SecurityPermissionCode.Permission.MENU_CODE);

    assertActionPermission(
        PermissionController.class.getDeclaredMethod("importPermission", List.class),
        SecurityPermissionCode.Permission.IMPORT,
        SecurityPermissionCode.Permission.MENU_CODE,
        true);
    assertActionPermission(
        PermissionController.class.getDeclaredMethod("deletePermission", Long.class),
        SecurityPermissionCode.Permission.DELETE,
        SecurityPermissionCode.Permission.MENU_CODE,
        true);
  }

  @Test
  void projectControllerKeepsReadVisibilityAndRootOnlyAdministration() {
    assertControllerPermission(
        ProjectController.class,
        SecurityPermissionCode.Project.READ,
        SecurityPermissionCode.Project.MENU_CODE);

    List.of(
        "switchStatus",
        "updateStatus",
        "update",
        "create",
        "deleteCheck",
        "delete",
        "replaceProjectOwners",
        "replaceProjectUsers",
        "addProjectOwner",
        "deleteProjectOwner",
        "addProjectUser",
        "deleteProjectUser",
        "unassigned")
        .forEach(methodName -> assertMethodPermission(
            ProjectController.class,
            methodName,
            SecurityPermissionCode.ROOT));
  }

  private void assertControllerPermission(
      Class<?> controllerType,
      String expectedPermission,
      String expectedMenuCode) {
    RequiresPermission requiresPermission =
        controllerType.getAnnotation(RequiresPermission.class);
    YakPermission yakPermission = controllerType.getAnnotation(YakPermission.class);

    assertThat(requiresPermission).isNotNull();
    assertThat(requiresPermission.value()).isEqualTo(expectedPermission);
    assertThat(yakPermission).isNotNull();
    assertThat(yakPermission.code()).isEqualTo(expectedPermission);
    assertThat(yakPermission.menuCode()).isEqualTo(expectedMenuCode);
  }

  private void assertActionPermission(
      Method method,
      String expectedPermission,
      String expectedMenuCode,
      boolean declared) {
    RequiresPermission requiresPermission =
        method.getAnnotation(RequiresPermission.class);
    YakPermission yakPermission = method.getAnnotation(YakPermission.class);

    assertThat(requiresPermission).isNotNull();
    assertThat(requiresPermission.value()).isEqualTo(expectedPermission);

    if (!declared) {
      assertThat(yakPermission).isNull();
      return;
    }

    assertThat(yakPermission).isNotNull();
    assertThat(yakPermission.code()).isEqualTo(expectedPermission);
    assertThat(yakPermission.menuCode()).isEqualTo(expectedMenuCode);
  }

  private void assertMethodPermission(
      Class<?> controllerType,
      String methodName,
      String expectedPermission) {
    Method method = Arrays.stream(controllerType.getDeclaredMethods())
        .filter(candidate -> candidate.getName().equals(methodName))
        .findFirst()
        .orElseThrow(() -> new AssertionError(
            "Missing controller method: " + methodName));

    RequiresPermission requiresPermission =
        method.getAnnotation(RequiresPermission.class);
    assertThat(requiresPermission).isNotNull();
    assertThat(requiresPermission.value()).isEqualTo(expectedPermission);
  }
}
