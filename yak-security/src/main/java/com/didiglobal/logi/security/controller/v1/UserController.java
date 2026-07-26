/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.didiglobal.logi.security.controller.v1;

import com.alibaba.fastjson.JSON;
import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.PagingResult;
import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.user.UserDTO;
import com.didiglobal.logi.security.common.dto.user.UserQueryDTO;
import com.didiglobal.logi.security.common.vo.role.AssignInfoVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.common.vo.user.UserVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.service.UserService;
import com.didiglobal.logi.security.util.HttpRequestUtil;
import com.google.common.collect.Lists;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-user\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u7528\u6237\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/user"})
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value={"/{type}/{value}/check"})
    @ApiOperation(value="\u83b7\u53d6\u7528\u6237\u8be6\u60c5", notes="\u6839\u636e\u7528\u6237id\u83b7\u53d6\u7528\u6237\u8be6\u60c5")
    @ApiImplicitParam(name="type", value="\u7528\u6237id", dataType="int", required=true)
    public Result<Void> check(@PathVariable Integer type, @PathVariable String value) {
        return this.userService.check(type, value);
    }

    @GetMapping
    @ApiOperation(value="\u6279\u91cf\u83b7\u53d6\u7528\u6237\u8be6\u60c5", notes="\u6839\u636e\u7528\u6237id\u83b7\u53d6\u7528\u6237\u8be6\u60c5")
    @ApiImplicitParam(name="ids", value="\u7528\u6237ids", dataType="string", example="[1,2,3]", required=true)
    public Result<List<UserVO>> detailList(@RequestParam(value="ids") String ids) {
        List idList = Lists.newArrayList();
        try {
            idList = JSON.parseArray((String)ids, Integer.class);
        } catch (Exception e) {
            return Result.buildParamIllegal("\u4f20\u5165\u7684\u53c2\u6570\u4e0d\u5c5e\u4e8ejson\u6570\u7ec4");
        }
        return this.userService.getUserDetailByUserIds(idList);
    }

    @GetMapping(value={"/{id}"})
    @ApiOperation(value="\u83b7\u53d6\u7528\u6237\u8be6\u60c5", notes="\u6839\u636e\u7528\u6237id\u83b7\u53d6\u7528\u6237\u8be6\u60c5")
    @ApiImplicitParam(name="id", value="\u7528\u6237id", dataType="int", required=true)
    public Result<UserVO> detail(@PathVariable Integer id) {
        try {
            UserVO userVo = this.userService.getUserDetailByUserId(id);
            return Result.success(userVo);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/page"})
    @ApiOperation(value="\u67e5\u8be2\u7528\u6237\u5217\u8868", notes="\u5206\u9875\u548c\u6761\u4ef6\u67e5\u8be2")
    public PagingResult<UserVO> page(@RequestBody UserQueryDTO queryDTO) {
        PagingData<UserVO> pageUser = this.userService.getUserPage(queryDTO);
        return PagingResult.success(pageUser);
    }

    @GetMapping(value={"/list/dept/{deptId}"})
    @ApiOperation(value="\u6839\u636e\u90e8\u95e8id\u83b7\u53d6\u7528\u6237list", notes="\u6839\u636e\u90e8\u95e8id\u83b7\u53d6\u7528\u6237\u7b80\u8981\u4fe1\u606flist")
    @ApiImplicitParam(name="deptId", value="\u90e8\u95e8id", dataType="int", required=true)
    public Result<List<UserBriefVO>> listByDeptId(@PathVariable Integer deptId) {
        List<UserBriefVO> userBriefVOList = this.userService.getUserBriefListByDeptId(deptId);
        return Result.success(userBriefVOList);
    }

    @GetMapping(value={"/list/role/{roleId}"})
    @ApiOperation(value="\u6839\u636e\u89d2\u8272id\u83b7\u53d6\u7528\u6237list", notes="\u6839\u636e\u89d2\u8272id\u83b7\u53d6\u7528\u6237\u7b80\u8981\u4fe1\u606flist")
    @ApiImplicitParam(name="roleId", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<List<UserBriefVO>> listByRoleId(@PathVariable Integer roleId) {
        List<UserBriefVO> userBriefVOList = this.userService.getUserBriefListByRoleId(roleId);
        return Result.success(userBriefVOList);
    }

    @GetMapping(value={"/assign/list/{userId}"})
    @ApiOperation(value="\u7528\u6237\u7ba1\u7406/\u5206\u914d\u89d2\u8272/\u5217\u8868", notes="\u67e5\u8be2\u6240\u6709\u89d2\u8272\u5217\u8868\uff0c\u5e76\u6839\u636e\u7528\u6237id\uff0c\u6807\u8bb0\u8be5\u7528\u6237\u62e5\u6709\u54ea\u4e9b\u89d2\u8272")
    @ApiImplicitParam(name="userId", value="\u7528\u6237id", dataType="int", required=true)
    public Result<List<AssignInfoVO>> assignList(@PathVariable Integer userId) {
        try {
            List<AssignInfoVO> assignInfoVOList = this.userService.getAssignDataByUserId(userId);
            return Result.success(assignInfoVOList);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @GetMapping(value={"/list/{name}"})
    @ApiOperation(value="\u6839\u636e\u8d26\u6237\u540d\u6216\u7528\u6237\u5b9e\u540d\u67e5\u8be2", notes="\u83b7\u53d6\u7528\u6237\u7b80\u8981\u4fe1\u606flist\uff0c\u4f1a\u5206\u522b\u4ee5\u8d26\u6237\u540d\u548c\u5b9e\u540d\u53bb\u6a21\u7cca\u67e5\u8be2\uff0c\u8fd4\u56de\u4e24\u8005\u7684\u5e76\u96c6")
    @ApiImplicitParam(name="name", value="\u8d26\u6237\u540d\u6216\u7528\u6237\u5b9e\u540d\uff08\u4e3anull\uff0c\u5219\u83b7\u53d6\u5168\u90e8\u7528\u6237\uff09", dataType="String")
    public Result<List<UserBriefVO>> listByName(@PathVariable(required=false) String name) {
        List<UserBriefVO> userBriefVOList = this.userService.getUserBriefListByUsernameOrRealName(name);
        return Result.success(userBriefVOList);
    }

    @PutMapping(value={"/add"})
    @ResponseBody
    @ApiOperation(value="\u7528\u6237\u65b0\u589e\u63a5\u53e3\uff0c\u6682\u65f6\u6ca1\u6709\u8003\u8651\u6743\u9650", notes="")
    public Result<Void> add(HttpServletRequest request, @RequestBody UserDTO param) {
        return this.userService.addUser(param, HttpRequestUtil.getOperator(request));
    }

    @PostMapping(value={"/edit"})
    @ResponseBody
    @ApiOperation(value="\u7f16\u8f91\u7528\u6237\u63a5\u53e3\uff0c\u6682\u65f6\u6ca1\u6709\u8003\u8651\u6743\u9650", notes="")
    public Result<Void> edit(HttpServletRequest request, @RequestBody UserDTO param) {
        return this.userService.editUser(param, HttpRequestUtil.getOperator(request));
    }

    @DeleteMapping(value={"/{id}"})
    @ApiOperation(value="\u5220\u9664\u7528\u6237", notes="\u6839\u636e\u7528\u6237id\u5220\u9664\u7528\u6237")
    @ApiImplicitParam(name="id", value="\u7528\u6237id", dataType="int", required=true)
    public Result<Void> del(@PathVariable Integer id) {
        return this.userService.deleteByUserId(id);
    }
}

