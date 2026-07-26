package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import io.yak.framework.security.service.DeptService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "部门管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/dept")
public class DeptController {

  private final DeptService deptService;

  /**
   * 创建部门管理接口。
   *
   * @param deptService 部门服务
   */
  public DeptController(DeptService deptService) {
    this.deptService = deptService;
  }

  /**
   * 查询完整部门树。
   *
   * @return 部门树
   */
  @Operation(summary = "查询完整部门树")
  @GetMapping("/tree")
  public Result<DeptTreeVO> tree() {
    return Result.buildSucc(
            deptService.buildDeptTree());
  }

  /**
   * 导入部门树。
   *
   * @param deptDTOList 部门信息列表
   * @return 导入结果
   */
  @Operation(summary = "导入部门树")
  @PostMapping("/import")
  public Result<Void> importDept(
          @RequestBody
                  List<DeptDTO> deptDTOList) {

    deptService.saveDept(deptDTOList);

    return Result.buildSucc(null);
  }
}