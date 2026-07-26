package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import java.util.List;

public interface DeptDao {
  public List<Dept> selectAllAndAscOrderByLevel();

  public List<Long> selectIdListByLikeDeptName(String var1);

  public DeptBrief selectBriefByDeptId(Long deptId);

  public List<Long> selectAllDeptIdList();

  public List<Long> selectIdListByParentId(Long parentId);

  public void insertBatch(List<Dept> var1);

  public List<DeptBrief> selectAllDeptBriefList();
}
