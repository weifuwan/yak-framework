package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.po.PermissionPO;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.PermissionMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PermissionDaoImpl
    extends BaseDaoImpl<PermissionPO> implements PermissionDao {
  @Autowired private PermissionMapper permissionMapper;

  @Override
  public List<Permission> selectAllAndAscOrderByLevel() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.orderByAsc((Object) "level");
    return CopyBeanUtil.copyList(
        this.permissionMapper.selectList((Wrapper)queryWrapper),
        Permission.class);
  }

  @Override
  public void insertBatch(List<Permission> permissionList) {
    if (CollectionUtils.isEmpty(permissionList)) {
      return;
    }
    List<PermissionPO> permissionPOList =
        CopyBeanUtil.copyList(permissionList, PermissionPO.class);
    for (PermissionPO permissionPO : permissionPOList) {
      permissionPO.setAppName(this.yakSecurityProperties.getAppName());
      this.permissionMapper.insert(permissionPO);
    }
  }
}
