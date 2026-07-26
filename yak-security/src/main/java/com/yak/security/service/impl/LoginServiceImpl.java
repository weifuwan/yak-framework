/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.yak.security.service.impl;

import com.yak.security.common.Result;
import com.yak.security.common.dto.account.AccountLoginDTO;
import com.yak.security.common.vo.user.UserBriefVO;
import com.yak.security.exception.LogiSecurityException;
import com.yak.security.extend.LoginExtendBeanTool;
import com.yak.security.service.LoginService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl
implements LoginService {
    @Autowired
    private LoginExtendBeanTool loginExtendBeanTool;

    @Override
    public UserBriefVO verifyLogin(AccountLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) throws LogiSecurityException {
        return this.loginExtendBeanTool.getLoginExtendImpl().verifyLogin(loginDTO, request, response);
    }

    @Override
    public Result<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        return this.loginExtendBeanTool.getLoginExtendImpl().logout(request, response);
    }

    @Override
    public boolean interceptorCheck(HttpServletRequest request, HttpServletResponse response, String requestMappingValue, List<String> whiteMappingValues) throws IOException {
        return this.loginExtendBeanTool.getLoginExtendImpl().interceptorCheck(request, response, requestMappingValue, whiteMappingValues);
    }
}

