package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.role.RoleQueryDTO;
import io.yak.framework.security.common.entity.role.Role;
import io.yak.framework.security.common.entity.role.RoleBrief;
import java.util.List;

public interface RoleDao {
  public Role selectByRoleName(String var1);

  public Role selectByRoleId(Integer var1);

  public IPage<Role> selectPage(RoleQueryDTO var1);

  public void insert(Role var1);

  public void deleteByRoleId(Integer var1);

  public void update(Role var1);

  public List<RoleBrief>
  selectBriefListByRoleNameAndDescOrderByCreateTime(String var1);

  public List<RoleBrief> selectAllBrief();

  public List<RoleBrief> selectBriefListByRoleIdList(List<Long> var1);

  public int selectCountByRoleNameAndNotRoleId(String var1, Integer var2);
}
