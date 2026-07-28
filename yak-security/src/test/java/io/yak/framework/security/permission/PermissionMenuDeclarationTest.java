package io.yak.framework.security.permission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.dao.PermissionDao;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class PermissionMenuDeclarationTest {

  @Test
  void definitionPersistsMenuCodeForActionPermission() {
    PermissionDao permissionDao = Mockito.mock(PermissionDao.class);
    PermissionRegistrationService service =
        new PermissionRegistrationService(permissionDao);

    service.synchronize(List.of(
        PermissionDefinition.of(
            "order",
            "订单管理",
            PermissionDefinition.Item.ofMenu(
                "order:create",
                "新增订单",
                "创建订单",
                "order-list"))));

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Permission>> captor =
        ArgumentCaptor.forClass(List.class);
    verify(permissionDao).synchronizeDeclared(captor.capture());

    assertThat(captor.getValue())
        .filteredOn(permission ->
            "order:create".equals(permission.getPermissionCode()))
        .singleElement()
        .extracting(Permission::getMenuCode)
        .isEqualTo("order-list");
  }
}
