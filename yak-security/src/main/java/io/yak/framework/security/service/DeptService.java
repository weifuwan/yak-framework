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
 *
 * @author weifuwan
 */
public interface DeptService {

  /**
   * 构建完整部门树。
   *
   * @return 部门树
   */
  DeptTreeVO buildDeptTree();

  /**
   * 根据子部门 ID 查询从根部门到当前部门的简要信息。
   *
   * @param deptId 子部门 ID
   * @return 部门层级路径
   */
  List<DeptBriefVO> getDeptBriefListByChildId(
          Long deptId);

  /**
   * 查询指定部门及其所有子部门 ID。
   *
   * <p>当部门 ID 为空时，返回全部部门 ID。
   *
   * @param deptId 父部门 ID
   * @return 部门 ID 列表
   */
  List<Long> getDeptIdListByParentId(
          Long deptId);

  /**
   * 根据父部门 ID 和部门名称查询部门 ID。
   *
   * <p>查询范围包含指定部门及其所有子部门。
   *
   * @param deptId 父部门 ID
   * @param deptName 部门名称
   * @return 部门 ID 列表
   */
  List<Long> getDeptIdListByParentIdAndDeptName(
          Long deptId,
          String deptName);

  /**
   * 查询全部部门并转换为映射。
   *
   * @return 以部门 ID 为键的部门映射
   */
  Map<Long, Dept> getAllDeptMap();

  /**
   * 从部门映射中查询指定部门的完整层级路径。
   *
   * @param deptMap 部门映射
   * @param deptId 子部门 ID
   * @return 从根部门到当前部门的部门路径
   */
  List<DeptBriefVO> getDeptBriefListFromDeptMapByChildId(
          Map<Long, Dept> deptMap,
          Long deptId);

  /**
   * 批量保存部门树。
   *
   * @param deptDTOList 部门树数据
   */
  void saveDept(
          List<DeptDTO> deptDTOList);

  /**
   * 查询全部部门简要信息。
   *
   * @return 部门简要信息列表
   */
  List<DeptBrief> listAllDeptBrief();
}