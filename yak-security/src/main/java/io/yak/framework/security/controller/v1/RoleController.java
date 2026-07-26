package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.role.RoleAssignDTO;
import io.yak.framework.security.common.dto.role.RoleQueryDTO;
import io.yak.framework.security.common.dto.role.RoleSaveDTO;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import io.yak.framework.security.common.vo.role.RoleDeleteCheckVO;
import io.yak.framework.security.common.vo.role.RoleVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.util.HttpRequestUtil;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理接口。
 *
 * @author weifuwan
 */
@RestController
@RequestMapping("/yak-security/api/v1/role")
public class RoleController {

  private final RoleService roleService;

  /**
   * 创建角色管理接口。
   *
   * @param roleService 角色服务
   */
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * 根据角色 ID 查询角色详情。
   *
   * @param roleId 角色 ID
   * @return 角色详情
   */
  @GetMapping("/{id}")
  public Result<RoleVO> detail(
          @PathVariable("id") Long roleId) {

    return Result.buildSucc(
            roleService.getRoleDetailByRoleId(
                    roleId));
  }

  /**
   * 更新角色。
   *
   * @param request HTTP 请求
   * @param roleSaveDTO 角色信息
   * @return 更新结果
   */
  @PutMapping
  public Result<Void> update(
          HttpServletRequest request,
          @RequestBody RoleSaveDTO roleSaveDTO) {

    try {
      roleService.updateRole(
              roleSaveDTO,
              HttpRequestUtil.getOperator(request));

      return Result.buildSucc(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 创建角色。
   *
   * @param request HTTP 请求
   * @param roleSaveDTO 角色信息
   * @return 创建结果
   */
  @PostMapping
  public Result<Void> create(
          HttpServletRequest request,
          @RequestBody RoleSaveDTO roleSaveDTO) {

    try {
      roleService.createRole(
              roleSaveDTO,
              HttpRequestUtil.getOperator(request));

      return Result.buildSucc(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 执行角色删除前校验。
   *
   * <p>保留原有 DELETE 请求方式，避免影响现有前端调用。
   *
   * @param roleId 角色 ID
   * @return 删除校验结果
   */
  @DeleteMapping("/delete/check/{id}")
  public Result<RoleDeleteCheckVO> check(
          @PathVariable("id") Long roleId) {

    return Result.buildSucc(
            roleService.checkBeforeDelete(
                    roleId));
  }

  /**
   * 从角色中删除用户。
   *
   * @param request HTTP 请求
   * @param roleId 角色 ID
   * @param userId 用户 ID
   * @return 删除结果
   */
  @DeleteMapping("/{id}/user/{userId}")
  public Result<Void> deleteUser(
          HttpServletRequest request,
          @PathVariable("id") Long roleId,
          @PathVariable Long userId) {

    try {
      roleService.deleteUserFromRole(
              roleId,
              userId,
              HttpRequestUtil.getOperator(request));

      return Result.buildSucc(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 根据角色 ID 删除角色。
   *
   * @param request HTTP 请求
   * @param roleId 角色 ID
   * @return 删除结果
   */
  @DeleteMapping("/{id}")
  public Result<Void> delete(
          HttpServletRequest request,
          @PathVariable("id") Long roleId) {

    try {
      roleService.deleteRoleByRoleId(
              roleId,
              HttpRequestUtil.getOperator(request));

      return Result.buildSucc(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 分页查询角色。
   *
   * @param queryDTO 查询条件
   * @return 角色分页结果
   */
  @PostMapping("/page")
  public PagingResult<RoleVO> page(
          @RequestBody RoleQueryDTO queryDTO) {

    PagingData<RoleVO> pagingData =
            roleService.getRolePage(queryDTO);

    return PagingResult.success(pagingData);
  }

  /**
   * 分配角色或为角色分配用户。
   *
   * @param request HTTP 请求
   * @param assignDTO 分配参数
   * @return 分配结果
   */
  @PostMapping("/assign")
  public Result<Void> assign(
          HttpServletRequest request,
          @RequestBody RoleAssignDTO assignDTO) {

    try {
      roleService.assignRoles(
              assignDTO,
              HttpRequestUtil.getOperator(request));

      return Result.buildSucc(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 根据角色 ID 查询用户分配信息。
   *
   * @param roleId 角色 ID
   * @return 用户分配信息列表
   */
  @GetMapping("/assign/list/{roleId}")
  public Result<List<AssignInfoVO>> assignList(
          @PathVariable Long roleId) {

    return Result.buildSucc(
            roleService.getAssignInfoByRoleId(
                    roleId));
  }

  /**
   * 根据角色名称查询角色。
   *
   * @param roleName 角色名称
   * @return 角色简要信息列表
   */
  @GetMapping({"/list/{roleName}", "/list"})
  public Result<List<RoleBriefVO>> list(
          @PathVariable(required = false)
                  String roleName) {

    return Result.buildSucc(
            roleService
                    .getRoleBriefListByRoleName(
                            roleName));
  }
}