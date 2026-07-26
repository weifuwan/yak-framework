package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import io.yak.framework.security.service.DeptService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/dept"})
public class DeptController {
  @Autowired private DeptService deptService;

  @GetMapping(value = {"/tree"})
  public Result<DeptTreeVO> tree() {
    DeptTreeVO deptTreeVO = this.deptService.buildDeptTree();
    return Result.success(deptTreeVO);
  }

  @PostMapping(value = {"/import"})
  public Result<String> imports(@RequestBody @ApiParam(
      name = "deptDTOList",
      value = "\u90e8\u95e8\u4fe1\u606fList") List<DeptDTO> deptDTOList) {
    this.deptService.saveDept(deptDTOList);
    return Result.success();
  }
}
