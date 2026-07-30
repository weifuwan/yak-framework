package io.yak.framework.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.common.po.MenuPO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.dao.mapper.MenuMapper;
import io.yak.framework.security.dao.mapper.PermissionMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class PermissionMenuRelationServiceTest {

  @Test
  void mergeCapabilityTreeIgnoresMenusWithoutRequiredPermissionCode() {
    PermissionMapper permissionMapper = mock(PermissionMapper.class);
    MenuMapper menuMapper = mock(MenuMapper.class);

    MenuPO departmentMenu = new MenuPO();
    departmentMenu.setId(10L);
    departmentMenu.setMenuCode("system-departments");
    departmentMenu.setMenuName("部门管理");
    departmentMenu.setActive(Boolean.TRUE);
    departmentMenu.setRequiredPermissionCode(null);

    when(menuMapper.selectList(any()))
        .thenReturn(Collections.singletonList(departmentMenu));

    PermissionTreeVO action = PermissionTreeVO.builder()
        .id(20L)
        .permissionCode("security:department:create")
        .permissionName("新增部门")
        .menuCode("system-departments")
        .active(Boolean.TRUE)
        .leaf(Boolean.TRUE)
        .build();
    PermissionTreeVO permissionRoot = PermissionTreeVO.builder()
        .id(0L)
        .leaf(Boolean.FALSE)
        .childList(new ArrayList<>(Collections.singletonList(action)))
        .build();

    PermissionTreeVO menuNode = PermissionTreeVO.builder()
        .id(-10L)
        .permissionCode("menu:system-departments")
        .permissionName("部门管理")
        .active(Boolean.TRUE)
        .leaf(Boolean.TRUE)
        .childList(new ArrayList<>())
        .build();
    PermissionTreeVO menuTree = PermissionTreeVO.builder()
        .id(-1L)
        .permissionCode("menu")
        .permissionName("菜单与操作权限")
        .active(Boolean.TRUE)
        .leaf(Boolean.FALSE)
        .childList(new ArrayList<>(Arrays.asList(menuNode)))
        .build();

    PermissionMenuRelationService service =
        new PermissionMenuRelationService(permissionMapper, menuMapper);

    assertThatCode(() -> service.mergeCapabilityTree(
        permissionRoot,
        menuTree,
        Collections.emptyList()))
        .doesNotThrowAnyException();

    assertThat(menuNode.getChildList())
        .extracting(PermissionTreeVO::getPermissionCode)
        .containsExactly("security:department:create");
  }
}
