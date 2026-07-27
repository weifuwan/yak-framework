package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.common.Result;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.dto.dept.DeptSaveDTO;
import io.yak.framework.security.common.vo.dept.DeptDeleteCheckVO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import io.yak.framework.security.common.vo.dept.DeptVO;
import io.yak.framework.security.service.DeptManagementService;
import io.yak.framework.security.service.DeptService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门管理接口。
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "部门管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/dept")
public class DeptController {

  private final DeptService deptService;
  private final DeptManagementService deptManagementService;

  public DeptController(
          DeptService deptService,
          DeptManagementService deptManagementService) {
    this.deptService = deptService;
    this.deptManagementService = deptManagementService;
  }

  /** 查询完整部门树。 */
  @Operation(summary = "查询完整部门树")
  @GetMapping("/tree")
  public Result<DeptTreeVO> tree() {
    return Result.success(deptService.buildDeptTree());
  }

  /** 根据部门 ID 查询部门详情。 */
  @Operation(summary = "根据部门 ID 查询部门详情")
  @GetMapping("/{id}")
  public Result<DeptVO> detail(
          @PathVariable("id") Long deptId) {
    return Result.success(
            deptManagementService.getDeptDetail(deptId));
  }

  /** 新增部门。 */
  @Operation(summary = "新增部门")
  @PostMapping
  public Result<Void> create(
          @RequestBody DeptSaveDTO saveDTO) {
    deptManagementService.createDept(saveDTO);
    return Result.success(null);
  }

  /** 编辑部门。 */
  @Operation(summary = "编辑部门")
  @PutMapping
  public Result<Void> update(
          @RequestBody DeptSaveDTO saveDTO) {
    deptManagementService.updateDept(saveDTO);
    return Result.success(null);
  }

  /** 执行部门删除前检查。 */
  @Operation(summary = "执行部门删除前检查")
  @DeleteMapping("/delete/check/{id}")
  public Result<DeptDeleteCheckVO> checkBeforeDelete(
          @PathVariable("id") Long deptId) {
    return Result.success(
            deptManagementService.checkBeforeDelete(deptId));
  }

  /** 删除部门。 */
  @Operation(summary = "删除部门")
  @DeleteMapping("/{id}")
  public Result<Void> delete(
          @PathVariable("id") Long deptId) {
    deptManagementService.deleteDept(deptId);
    return Result.success(null);
  }

  /** 导入部门树。 */
  @Operation(summary = "导入部门树")
  @PostMapping("/import")
  public Result<Void> importDept(
          @RequestBody List<DeptDTO> deptDTOList) {
    deptService.saveDept(deptDTOList);
    return Result.success(null);
  }
}
