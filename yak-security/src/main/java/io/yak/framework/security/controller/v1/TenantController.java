package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.common.Result;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.dto.tenant.TenantMemberAssignDTO;
import io.yak.framework.security.common.dto.tenant.TenantSaveDTO;
import io.yak.framework.security.common.vo.tenant.TenantMemberVO;
import io.yak.framework.security.common.vo.tenant.TenantVO;
import io.yak.framework.security.permission.YakPermission;
import io.yak.framework.security.service.TenantService;
import io.yak.framework.security.web.RequiresPermission;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 租户管理接口。 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "租户管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/tenant")
@RequiresPermission(SecurityPermissionCode.Tenant.READ)
@YakPermission(
    code = SecurityPermissionCode.Tenant.READ,
    name = "查看租户管理",
    group = SecurityPermissionCode.GROUP_NAME,
    groupCode = SecurityPermissionCode.GROUP_CODE,
    menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
    description = "查看租户、外部目录映射及成员关系")
public class TenantController {

  private final TenantService tenantService;

  public TenantController(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @Operation(summary = "查询全部租户")
  @GetMapping
  public Result<List<TenantVO>> list() {
    return Result.success(tenantService.list());
  }

  @Operation(summary = "查询租户详情")
  @GetMapping("/{id}")
  public Result<TenantVO> detail(
      @PathVariable("id") Long tenantId) {
    return Result.success(
        tenantService.detail(tenantId));
  }

  @Operation(summary = "新增租户")
  @PostMapping
  @RequiresPermission(
      SecurityPermissionCode.Tenant.CREATE)
  @YakPermission(
      code = SecurityPermissionCode.Tenant.CREATE,
      name = "新增租户",
      group = SecurityPermissionCode.GROUP_NAME,
      groupCode = SecurityPermissionCode.GROUP_CODE,
      menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
      description = "创建应用内部的业务租户")
  public Result<TenantVO> create(
      @RequestBody TenantSaveDTO tenantSaveDTO) {
    return Result.success(
        tenantService.create(tenantSaveDTO));
  }

  @Operation(summary = "更新租户")
  @PutMapping
  @RequiresPermission(
      SecurityPermissionCode.Tenant.UPDATE)
  @YakPermission(
      code = SecurityPermissionCode.Tenant.UPDATE,
      name = "编辑租户",
      group = SecurityPermissionCode.GROUP_NAME,
      groupCode = SecurityPermissionCode.GROUP_CODE,
      menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
      description = "修改租户资料和启用状态")
  public Result<TenantVO> update(
      @RequestBody TenantSaveDTO tenantSaveDTO) {
    return Result.success(
        tenantService.update(tenantSaveDTO));
  }

  @Operation(summary = "按外部目录标识同步租户")
  @PutMapping("/external")
  @RequiresPermission(
      SecurityPermissionCode.Tenant.SYNC)
  @YakPermission(
      code = SecurityPermissionCode.Tenant.SYNC,
      name = "同步外部租户",
      group = SecurityPermissionCode.GROUP_NAME,
      groupCode = SecurityPermissionCode.GROUP_CODE,
      menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
      description = "根据外部系统和外部租户标识幂等同步租户")
  public Result<TenantVO> upsertExternal(
      @RequestBody TenantSaveDTO tenantSaveDTO) {
    return Result.success(
        tenantService.upsertExternal(
            tenantSaveDTO));
  }

  @Operation(summary = "查询租户成员")
  @GetMapping("/{id}/members")
  public Result<List<TenantMemberVO>> members(
      @PathVariable("id") Long tenantId) {
    return Result.success(
        tenantService.listMembers(tenantId));
  }

  @Operation(summary = "全量更新租户成员")
  @PutMapping("/{id}/members")
  @RequiresPermission(
      SecurityPermissionCode.Tenant.ASSIGN)
  @YakPermission(
      code = SecurityPermissionCode.Tenant.ASSIGN,
      name = "分配租户成员",
      group = SecurityPermissionCode.GROUP_NAME,
      groupCode = SecurityPermissionCode.GROUP_CODE,
      menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
      description = "全量维护租户成员、管理员和默认租户关系")
  public Result<Void> replaceMembers(
      @PathVariable("id") Long tenantId,
      @RequestBody TenantMemberAssignDTO assignDTO) {
    tenantService.replaceMembers(
        tenantId,
        assignDTO);
    return Result.success(null);
  }

  @Operation(summary = "删除租户")
  @DeleteMapping("/{id}")
  @RequiresPermission(
      SecurityPermissionCode.Tenant.DELETE)
  @YakPermission(
      code = SecurityPermissionCode.Tenant.DELETE,
      name = "删除租户",
      group = SecurityPermissionCode.GROUP_NAME,
      groupCode = SecurityPermissionCode.GROUP_CODE,
      menuCode = SecurityPermissionCode.Tenant.MENU_CODE,
      description = "删除没有成员关系的租户")
  public Result<Void> delete(
      @PathVariable("id") Long tenantId) {
    tenantService.delete(tenantId);
    return Result.success(null);
  }
}
