package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import io.yak.framework.security.common.vo.dept.DeptBriefVO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import java.util.List;
import java.util.Map;

/**
 * 部门服务接口。
 */
public interface DeptService {
  DeptTreeVO buildDeptTree();

  List<DeptBriefVO> getDeptBriefListByChildId(Long var1);

  List<Long> getDeptIdListByParentId(Long var1);

  List<Long> getDeptIdListByParentIdAndDeptName(Long var1,
                                                          String var2);

  Map<Long, Dept> getAllDeptMap();

  List<DeptBriefVO>
  getDeptBriefListFromDeptMapByChildId(Map<Long, Dept> var1, Long var2);

  void saveDept(List<DeptDTO> var1);

  List<DeptBrief> listAllDeptBrief();
}
