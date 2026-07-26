package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import io.yak.framework.security.common.vo.dept.DeptBriefVO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import java.util.List;
import java.util.Map;

public interface DeptService {
  public DeptTreeVO buildDeptTree();

  public List<DeptBriefVO> getDeptBriefListByChildId(Integer var1);

  public List<Integer> getDeptIdListByParentId(Integer var1);

  public List<Integer> getDeptIdListByParentIdAndDeptName(Integer var1,
                                                          String var2);

  public Map<Integer, Dept> getAllDeptMap();

  public List<DeptBriefVO>
  getDeptBriefListFromDeptMapByChildId(Map<Integer, Dept> var1, Integer var2);

  public void saveDept(List<DeptDTO> var1);

  public List<DeptBrief> listAllDeptBrief();
}
