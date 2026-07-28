package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.permission.YakPermission;
import io.yak.framework.security.web.RequiresPermission;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** 验证内置管理接口不会在重构时丢失操作权限边界。 */
class SecurityControllerPermissionTest {

  @Test
  void userControllerDeclaresReadAndWritePermissions() {
    assertControllerPermission(
            UserController.class,
            SecurityPermissionCode.User.READ);
    assertMethodPermission(
            UserController.class,
            "add",
            SecurityPermissionCode.User.CREATE);
    assertMethodPermission(
            UserController.class,
            "edit",
            SecurityPermissionCode.User.UPDATE);
    assertMethodPermission(
            UserController.class,
            "resetPassword",
            SecurityPermissionCode.User.RESET_PASSWORD);
    assertMethodPermission(
            UserController.class,
            "delete",
            SecurityPermissionCode.User.DELETE);
  }

  @Test
  void roleControllerDeclaresReadAndWritePermissions() {
    assertControllerPermission(
            RoleController.class,
            SecurityPermissionCode.Role.READ);
    assertMethodPermission(
            RoleController.class,
            "create",
            SecurityPermissionCode.Role.CREATE);
    assertMethodPermission(
            RoleController.class,
            "update",
            SecurityPermissionCode.Role.UPDATE);
    assertMethodPermission(
            RoleController.class,
            "assign",
            SecurityPermissionCode.Role.ASSIGN);
    assertMethodPermission(
            RoleController.class,
            "deleteUser",
            SecurityPermissionCode.Role.ASSIGN);
    assertMethodPermission(
            RoleController.class,
            "check",
            SecurityPermissionCode.Role.DELETE);
    assertMethodPermission(
            RoleController.class,
            "delete",
            SecurityPermissionCode.Role.DELETE);
  }

  @Test
  void permissionControllerDeclaresReadAndWritePermissions() {
    assertControllerPermission(
            PermissionController.class,
            SecurityPermissionCode.Permission.READ);
    assertMethodPermission(
            PermissionController.class,
            "importPermission",
            SecurityPermissionCode.Permission.IMPORT);
    assertMethodPermission(
            PermissionController.class,
            "deletePermission",
            SecurityPermissionCode.Permission.DELETE);
  }

  private static void assertControllerPermission(
          Class<?> controllerType,
          String expectedCode) {

    RequiresPermission required =
            controllerType.getAnnotation(RequiresPermission.class);
    YakPermission declaration =
            controllerType.getAnnotation(YakPermission.class);

    assertNotNull(required, controllerType.getSimpleName());
    assertNotNull(declaration, controllerType.getSimpleName());
    assertEquals(expectedCode, required.value());
    assertEquals(expectedCode, declaration.code());
  }

  private static void assertMethodPermission(
          Class<?> controllerType,
          String methodName,
          String expectedCode) {

    Method method = Arrays.stream(controllerType.getDeclaredMethods())
            .filter(candidate -> candidate.getName().equals(methodName))
            .findFirst()
            .orElseThrow(() -> new AssertionError(
                    "Missing controller method: " + methodName));

    RequiresPermission required =
            method.getAnnotation(RequiresPermission.class);
    assertNotNull(required, methodName);
    assertEquals(expectedCode, required.value());
  }
}
