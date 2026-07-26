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
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/role"})
public class RoleController {
  @Autowired private RoleService roleService;

  @GetMapping(value = {"/{id}"})
  public Result<RoleVO> detail(@PathVariable Integer id) {
    RoleVO roleVo = this.roleService.getRoleDetailByRoleId(id);
    return Result.success(roleVo);
  }

  @PutMapping
  public Result<String> update(@RequestBody RoleSaveDTO saveDTO,
                               HttpServletRequest request) {
    try {
      this.roleService.updateRole(saveDTO, request);
      return Result.success();
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @PostMapping
  public Result<String> create(@RequestBody RoleSaveDTO saveDTO,
                               HttpServletRequest request) {
    try {
      this.roleService.createRole(saveDTO, request);
      return Result.success();
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @DeleteMapping(value = {"/delete/check/{id}"})
  public Result<RoleDeleteCheckVO> check(@PathVariable Integer id) {
    return Result.success(this.roleService.checkBeforeDelete(id));
  }

  @DeleteMapping(value = {"/{id}/user/{userId}"})
  public Result<String> deleteUser(@PathVariable Integer id,
                                   @PathVariable Integer userId,
                                   HttpServletRequest request) {
    try {
      this.roleService.deleteUserFromRole(id, userId, request);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @DeleteMapping(value = {"/{id}"})
  public Result<String> delete(@PathVariable Integer id,
                               HttpServletRequest request) {
    try {
      this.roleService.deleteRoleByRoleId(id, request);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @PostMapping(value = {"/page"})
  public PagingResult<RoleVO> page(@RequestBody RoleQueryDTO queryDTO) {
    PagingData<RoleVO> pageRole = this.roleService.getRolePage(queryDTO);
    return PagingResult.success(pageRole);
  }

  @PostMapping(value = {"/assign"})
  public Result<String> assign(@RequestBody RoleAssignDTO assignDTO,
                               HttpServletRequest request) {
    try {
      this.roleService.assignRoles(assignDTO, request);
      return Result.success();
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @GetMapping(value = {"/assign/list/{roleId}"})
  public Result<List<AssignInfoVO>> assignList(@PathVariable Integer roleId) {
    List<AssignInfoVO> assignInfoVOList =
        this.roleService.getAssignInfoByRoleId(roleId);
    return Result.success(assignInfoVOList);
  }

  @GetMapping(value = {"/list/{roleName}", "/list"})
  public Result<List<RoleBriefVO>>
  list(@PathVariable(required = false) String roleName) {
    List<RoleBriefVO> roleBriefVOList =
        this.roleService.getRoleBriefListByRoleName(roleName);
    return Result.success(roleBriefVOList);
  }
}
