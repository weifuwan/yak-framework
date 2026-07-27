package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.dept.DeptSaveDTO;
import io.yak.framework.security.common.vo.dept.DeptDeleteCheckVO;
import io.yak.framework.security.common.vo.dept.DeptVO;

/**
 * 部门单节点管理服务。
 */
public interface DeptManagementService {

  /** 根据 ID 查询部门详情。 */
  DeptVO getDeptDetail(Long deptId);

  /** 新增部门。 */
  void createDept(DeptSaveDTO saveDTO);

  /** 编辑部门。 */
  void updateDept(DeptSaveDTO saveDTO);

  /** 删除部门前检查。 */
  DeptDeleteCheckVO checkBeforeDelete(Long deptId);

  /** 删除部门。 */
  void deleteDept(Long deptId);
}
