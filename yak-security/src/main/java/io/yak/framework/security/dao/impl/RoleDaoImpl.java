package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.role.RoleQueryDTO;
import io.yak.framework.security.common.entity.role.Role;
import io.yak.framework.security.common.entity.role.RoleBrief;
import io.yak.framework.security.common.po.RolePO;
import io.yak.framework.security.dao.RoleDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.RoleMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class RoleDaoImpl extends BaseDaoImpl<RolePO> implements RoleDao {
  @Autowired private RoleMapper roleMapper;

  @Override
  public Role selectByRoleName(String roleName) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "role_name", (Object)roleName);
    return CopyBeanUtil.copy(this.roleMapper.selectOne((Wrapper)queryWrapper),
                             Role.class);
  }

  @Override
  public Role selectByRoleId(Long roleId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "id", (Object)roleId);
    return CopyBeanUtil.copy(this.roleMapper.selectOne((Wrapper)queryWrapper),
                             Role.class);
  }

  @Override
  public IPage<Role> selectPage(RoleQueryDTO queryDTO) {
    Page pageInfo =
        new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    QueryWrapper roleWrapper = this.getQueryWrapperWithAppName();
    String roleName = queryDTO.getRoleName();
    String description = queryDTO.getDescription();
    if (!StringUtils.isEmpty((Object)queryDTO.getRoleCode())) {
      roleWrapper.eq((Object) "role_code", (Object)queryDTO.getRoleCode());
    } else {
      ((QueryWrapper)((QueryWrapper)roleWrapper.like(
                          !StringUtils.isEmpty((Object)roleName),
                          (Object) "role_name", (Object)roleName))
           .like(!StringUtils.isEmpty((Object)description),
                 (Object) "description", (Object)description))
          .orderByDesc((Object) "create_time");
    }
    roleWrapper.eq(Objects.nonNull(queryDTO.getId()), (Object) "id",
                   (Object)queryDTO.getId());
    this.roleMapper.selectPage((IPage)pageInfo, (Wrapper)roleWrapper);
    return CopyBeanUtil.copyPage(pageInfo, Role.class);
  }

  @Override
  public void insert(Role role) {
    RolePO rolePO = CopyBeanUtil.copy(role, RolePO.class);
    rolePO.setAppName(this.yakSecurityProperties.getApplicationName());
    this.roleMapper.insert(rolePO);
    role.setId(rolePO.getId());
  }

  @Override
  public void deleteByRoleId(Long roleId) {
    this.roleMapper.deleteById(roleId);
  }

  @Override
  public void update(Role role) {
    this.roleMapper.updateById(CopyBeanUtil.copy(role, RolePO.class));
  }

  @Override
  public List<RoleBrief>
  selectBriefListByRoleNameAndDescOrderByCreateTime(String roleName) {
    QueryWrapper<RolePO> queryWrapper = this.wrapBriefQuery();
    ((QueryWrapper)queryWrapper.like(!StringUtils.isEmpty((Object)roleName),
                                     (Object) "role_name", (Object)roleName))
        .orderByDesc((Object) "create_time");
    return CopyBeanUtil.copyList(
        this.roleMapper.selectList((Wrapper)queryWrapper), RoleBrief.class);
  }

  @Override
  public List<RoleBrief> selectAllBrief() {
    return CopyBeanUtil.copyList(
        this.roleMapper.selectList((Wrapper)this.wrapBriefQuery()),
        RoleBrief.class);
  }

  @Override
  public List<RoleBrief> selectBriefListByRoleIdList(List<Long> roleIdList) {
    if (CollectionUtils.isEmpty(roleIdList)) {
      return new ArrayList<RoleBrief>();
    }
    QueryWrapper<RolePO> queryWrapper = this.wrapBriefQuery();
    queryWrapper.in((Object) "id", roleIdList);
    return CopyBeanUtil.copyList(
        this.roleMapper.selectList((Wrapper)queryWrapper), RoleBrief.class);
  }

  @Override
  public int selectCountByRoleNameAndNotRoleId(String roleName,
                                               Long roleId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    ((QueryWrapper)queryWrapper.eq((Object) "role_name", (Object)roleName))
        .ne(roleId != null, (Object) "id", (Object)roleId);
    return this.roleMapper.selectCount((Wrapper)queryWrapper);
  }

  private QueryWrapper<RolePO> wrapBriefQuery() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"id", "role_name"});
    return queryWrapper;
  }
}
