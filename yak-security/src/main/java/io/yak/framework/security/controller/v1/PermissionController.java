package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/permission"})
public class PermissionController {
  @Autowired private PermissionService permissionService;

  @GetMapping(value = {"/tree"})
  public Result<PermissionTreeVO> tree() {
    PermissionTreeVO permissionTreeVO =
        this.permissionService.buildPermissionTree();
    return Result.success(permissionTreeVO);
  }

  @PostMapping(value = {"/import"})
  public Result<String> imports(@RequestBody @ApiParam(
      name = "permissionDTOList",
      value = "\u6743\u9650\u4fe1\u606fList") List<PermissionDTO> list) {
    this.permissionService.savePermission(list);
    return Result.success();
  }
}
