package io.yak.framework.security.service.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.service.PermissionCache;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserProjectServiceImplTest {

  @Test
  void invalidatesUsersWhenProjectMembershipIsAdded() {
    UserProjectDao userProjectDao = mock(UserProjectDao.class);
    PermissionCache permissionCache = mock(PermissionCache.class);
    UserProjectServiceImpl service = new UserProjectServiceImpl(
            userProjectDao,
            permissionCache);

    service.saveUserProject(9L, List.of(7L, 8L, 7L));

    verify(userProjectDao).insertBatch(anyList());
    verify(permissionCache).invalidateUser(7L);
    verify(permissionCache).invalidateUser(8L);
  }

  @Test
  void invalidatesExistingUsersWhenProjectMembershipIsCleared() {
    UserProjectDao userProjectDao = mock(UserProjectDao.class);
    PermissionCache permissionCache = mock(PermissionCache.class);
    when(userProjectDao.selectUserIdListByProjectId(9L, 0))
            .thenReturn(List.of(7L, 8L));
    UserProjectServiceImpl service = new UserProjectServiceImpl(
            userProjectDao,
            permissionCache);

    service.deleteUserProjectByProjectId(9L);

    verify(userProjectDao).deleteByProjectIdAndUserType(9L, 0);
    verify(permissionCache).invalidateUser(7L);
    verify(permissionCache).invalidateUser(8L);
  }
}
