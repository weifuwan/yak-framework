package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.service.PermissionService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理接口。
 *
 * @author weifuwan
 */
@RestController
@RequestMapping("/yak-security/api/v1/permission")
public class PermissionController {

  private final PermissionService permissionService;

  /**
   * 创建权限管理接口。
   *
   * @param permissionService 权限服务
   */
  public PermissionController(
          PermissionService permissionService) {

    this.permissionService = permissionService;
  }

  /**
   * 查询完整权限树。
   *
   * @return 权限树
   */
  @GetMapping("/tree")
  public Result<PermissionTreeVO> tree() {
    return Result.buildSucc(
            permissionService
                    .buildPermissionTree());
  }

  /**
   * 导入权限树。
   *
   * @param permissionDTOList 权限信息列表
   * @return 导入结果
   */
  @PostMapping("/import")
  public Result<Void> importPermission(
          @RequestBody
                  List<PermissionDTO> permissionDTOList) {

    permissionService.savePermission(
            permissionDTOList);

    return Result.buildSucc(null);
  }

  /** 删除权限及其角色关联。 */
  @DeleteMapping("/{permissionId}")
  public Result<Void> deletePermission(
          @PathVariable Long permissionId) {
    permissionService.deletePermissionById(permissionId);
    return Result.buildSucc(null);
  }
}
