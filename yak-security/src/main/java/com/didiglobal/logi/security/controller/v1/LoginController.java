/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.didiglobal.logi.security.controller.v1;

import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.account.AccountLoginDTO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-account\u767b\u5f55\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u767b\u5f55\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/account"})
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping(value={"/login"})
    @ApiOperation(value="\u767b\u5f55\u68c0\u67e5", notes="\u68c0\u67e5SSO\u8fd4\u56de\u7684Code")
    public Result<UserBriefVO> login(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountLoginDTO loginDTO) {
        try {
            UserBriefVO userBriefVO = this.loginService.verifyLogin(loginDTO, request, response);
            return Result.success(userBriefVO);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/logout"})
    @ApiOperation(value="\u767b\u51fa", notes="\u68c0\u67e5SSO\u8fd4\u56de\u7684Code")
    public Result<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        return this.loginService.logout(request, response);
    }
}

