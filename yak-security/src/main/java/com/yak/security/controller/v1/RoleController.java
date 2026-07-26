/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiImplicitParam
 *  io.swagger.annotations.ApiOperation
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.security.controller.v1;

import com.yak.security.common.PagingData;
import com.yak.security.common.PagingResult;
import com.yak.security.common.Result;
import com.yak.security.common.dto.role.RoleAssignDTO;
import com.yak.security.common.dto.role.RoleQueryDTO;
import com.yak.security.common.dto.role.RoleSaveDTO;
import com.yak.security.common.vo.role.AssignInfoVO;
import com.yak.security.common.vo.role.RoleBriefVO;
import com.yak.security.common.vo.role.RoleDeleteCheckVO;
import com.yak.security.common.vo.role.RoleVO;
import com.yak.security.exception.LogiSecurityException;
import com.yak.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
@Api(value="logi-security-role\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u89d2\u8272\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/role"})
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping(value={"/{id}"})
    @ApiOperation(value="\u83b7\u53d6\u89d2\u8272\u8be6\u60c5", notes="\u6839\u636e\u89d2\u8272id\u6216\u89d2\u8272code\u83b7\u53d6\u89d2\u8272\u8be6\u60c5")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<RoleVO> detail(@PathVariable Integer id) {
        RoleVO roleVo = this.roleService.getRoleDetailByRoleId(id);
        return Result.success(roleVo);
    }

    @PutMapping
    @ApiOperation(value="\u66f4\u65b0\u89d2\u8272\u4fe1\u606f", notes="\u6839\u636e\u89d2\u8272id\u66f4\u65b0\u89d2\u8272\u4fe1\u606f")
    public Result<String> update(@RequestBody RoleSaveDTO saveDTO, HttpServletRequest request) {
        try {
            this.roleService.updateRole(saveDTO, request);
            return Result.success();
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @PostMapping
    @ApiOperation(value="\u521b\u5efa\u89d2\u8272", notes="\u521b\u5efa\u89d2\u8272")
    public Result<String> create(@RequestBody RoleSaveDTO saveDTO, HttpServletRequest request) {
        try {
            this.roleService.createRole(saveDTO, request);
            return Result.success();
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @DeleteMapping(value={"/delete/check/{id}"})
    @ApiOperation(value="\u5220\u9664\u89d2\u8272\u524d\u7684\u68c0\u67e5", notes="\u5224\u65ad\u8be5\u89d2\u8272\u662f\u5426\u5df2\u7ecf\u5206\u914d\u7ed9\u7528\u6237\uff0c\u5982\u6709\u5206\u914d\u7ed9\u7528\u6237\uff0c\u5219\u8fd4\u56de\u7528\u6237\u7684\u4fe1\u606flist")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<RoleDeleteCheckVO> check(@PathVariable Integer id) {
        return Result.success(this.roleService.checkBeforeDelete(id));
    }

    @DeleteMapping(value={"/{id}/user/{userId}"})
    @ApiOperation(value="\u4ece\u89d2\u8272\u4e2d\u5220\u9664\u8be5\u89d2\u8272\u4e0b\u7684\u7528\u6237", notes="\u4ece\u89d2\u8272\u4e2d\u5220\u9664\u8be5\u89d2\u8272\u4e0b\u7684\u7528\u6237")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> deleteUser(@PathVariable Integer id, @PathVariable Integer userId, HttpServletRequest request) {
        try {
            this.roleService.deleteUserFromRole(id, userId, request);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @DeleteMapping(value={"/{id}"})
    @ApiOperation(value="\u5220\u9664\u89d2\u8272", notes="\u6839\u636e\u89d2\u8272id\u5220\u9664\u89d2\u8272")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> delete(@PathVariable Integer id, HttpServletRequest request) {
        try {
            this.roleService.deleteRoleByRoleId(id, request);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @PostMapping(value={"/page"})
    @ApiOperation(value="\u5206\u9875\u67e5\u8be2\u89d2\u8272\u5217\u8868", notes="\u5206\u9875\u548c\u6761\u4ef6\u67e5\u8be2")
    public PagingResult<RoleVO> page(@RequestBody RoleQueryDTO queryDTO) {
        PagingData<RoleVO> pageRole = this.roleService.getRolePage(queryDTO);
        return PagingResult.success(pageRole);
    }

    @PostMapping(value={"/assign"})
    @ApiOperation(value="\u5206\u914d\u89d2\u8272", notes="\u5206\u914d\u4e00\u4e2a\u89d2\u8272\u7ed9\u591a\u4e2a\u7528\u6237\u6216\u5206\u914d\u591a\u4e2a\u89d2\u8272\u7ed9\u4e00\u4e2a\u7528\u6237")
    public Result<String> assign(@RequestBody RoleAssignDTO assignDTO, HttpServletRequest request) {
        try {
            this.roleService.assignRoles(assignDTO, request);
            return Result.success();
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @GetMapping(value={"/assign/list/{roleId}"})
    @ApiOperation(value="\u89d2\u8272\u7ba1\u7406/\u5206\u914d\u7528\u6237/\u5217\u8868", notes="\u67e5\u8be2\u6240\u6709\u7528\u6237\u5217\u8868\uff0c\u5e76\u6839\u636e\u89d2\u8272id\uff0c\u6807\u8bb0\u54ea\u4e9b\u7528\u6237\u62e5\u6709\u8be5\u89d2\u8272")
    @ApiImplicitParam(name="roleId", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<List<AssignInfoVO>> assignList(@PathVariable Integer roleId) {
        List<AssignInfoVO> assignInfoVOList = this.roleService.getAssignInfoByRoleId(roleId);
        return Result.success(assignInfoVOList);
    }

    @GetMapping(value={"/list/{roleName}", "/list"})
    @ApiOperation(value="\u6839\u636e\u89d2\u8272\u540d\u6a21\u7cca\u67e5\u8be2", notes="\u7528\u6237\u7ba1\u7406/\u5217\u8868\u67e5\u8be2\u6761\u4ef6/\u5206\u914d\u89d2\u8272\u6846\uff0c\u8fd9\u91cc\u4f1a\u7528\u5230\u6b64\u63a5\u53e3")
    @ApiImplicitParam(name="roleName", value="\u89d2\u8272\u540d\uff08\u4e3anull\uff0c\u67e5\u8be2\u5168\u90e8\uff09", dataType="String")
    public Result<List<RoleBriefVO>> list(@PathVariable(required=false) String roleName) {
        List<RoleBriefVO> roleBriefVOList = this.roleService.getRoleBriefListByRoleName(roleName);
        return Result.success(roleBriefVOList);
    }
}

