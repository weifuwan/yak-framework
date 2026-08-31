package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.web.RequiresPermission;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectControllerPermissionTest {

  @Test
  void projectReadsUseProjectReadPermission() {
    RequiresPermission required =
            ProjectController.class.getAnnotation(RequiresPermission.class);
    assertNotNull(required);
    assertEquals(SecurityPermissionCode.Project.READ, required.value());
  }

  @Test
  void projectMutationsRequireRootPermission() {
    for (String methodName : ListHolder.MUTATIONS) {
      Method method = Arrays.stream(ProjectController.class.getDeclaredMethods())
              .filter(candidate -> candidate.getName().equals(methodName))
              .findFirst()
              .orElseThrow();
      RequiresPermission required = method.getAnnotation(RequiresPermission.class);
      assertNotNull(required, methodName);
      assertEquals(SecurityPermissionCode.ROOT, required.value(), methodName);
    }
  }

  private static final class ListHolder {
    private static final String[] MUTATIONS = {
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
      "deleteProjectUser"
    };
  }
}
