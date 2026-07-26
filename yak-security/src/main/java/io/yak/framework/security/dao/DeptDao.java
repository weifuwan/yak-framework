package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import java.util.List;

public interface DeptDao {
  public List<Dept> selectAllAndAscOrderByLevel();

  public List<Integer> selectIdListByLikeDeptName(String var1);

  public DeptBrief selectBriefByDeptId(Integer var1);

  public List<Integer> selectAllDeptIdList();

  public List<Integer> selectIdListByParentId(Integer var1);

  public void insertBatch(List<Dept> var1);

  public List<DeptBrief> selectAllDeptBriefList();
}
