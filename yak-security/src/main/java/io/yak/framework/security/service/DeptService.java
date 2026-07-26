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
  /**
   * 构建部门树。
   */
  DeptTreeVO buildDeptTree();

  /**
   * 根据子部门 ID 查询部门简要信息。
   */
  List<DeptBriefVO> getDeptBriefListByChildId(Long var1);

  /**
   * 根据父部门 ID 查询部门 ID 集合。
   */
  List<Long> getDeptIdListByParentId(Long var1);

  /**
   * 根据父部门 ID 和部门名称查询部门 ID 集合。
   */
  List<Long> getDeptIdListByParentIdAndDeptName(Long var1,
                                                          String var2);

  /**
   * 查询全部部门并转换为映射。
   */
  Map<Long, Dept> getAllDeptMap();

  /**
   * 从部门映射中根据子部门 ID 查询部门简要信息。
   */
  List<DeptBriefVO>
  getDeptBriefListFromDeptMapByChildId(Map<Long, Dept> var1, Long var2);

  /**
   * 保存部门信息。
   */
  void saveDept(List<DeptDTO> var1);

  /**
   * 查询全部部门简要实体。
   */
  List<DeptBrief> listAllDeptBrief();
}
