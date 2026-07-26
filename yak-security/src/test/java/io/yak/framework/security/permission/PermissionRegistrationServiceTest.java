package io.yak.framework.security.permission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.dao.PermissionDao;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class PermissionRegistrationServiceTest {

  @Test
  void convertsGroupsAndLeavesForSynchronization() {
    PermissionDao dao = Mockito.mock(PermissionDao.class);
    PermissionRegistrationService service = new PermissionRegistrationService(dao);

    service.synchronize(List.of(PermissionDefinition.of("job", "作业管理",
        PermissionDefinition.Item.of("job:create", "创建作业"))));

    ArgumentCaptor<List<Permission>> captor = ArgumentCaptor.forClass(List.class);
    verify(dao).synchronizeDeclared(captor.capture());
    assertThat(captor.getValue()).extracting(Permission::getPermissionCode)
        .containsExactly("job", "job:create");
    Permission leaf = captor.getValue().get(1);
    assertThat(leaf.getParentCode()).isEqualTo("job");
    assertThat(leaf.getPermissionName()).isEqualTo("创建作业");
    assertThat(leaf.getActive()).isTrue();
    assertThat(leaf.getDeclared()).isTrue();
  }

  @Test
  void rejectsConflictingCodes() {
    PermissionDao dao = Mockito.mock(PermissionDao.class);
    PermissionRegistrationService service = new PermissionRegistrationService(dao);

    assertThatThrownBy(() -> service.synchronize(List.of(
        PermissionDefinition.of("job", "作业管理", "job:create"),
        PermissionDefinition.of("job:create", "冲突分组", "job:create:child"))))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("job:create");
    org.mockito.Mockito.verify(dao, org.mockito.Mockito.never()).synchronizeDeclared(anyList());
  }
}
