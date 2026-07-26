/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.didiglobal.logi.security.service.impl;

import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.account.AccountLoginDTO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.extend.LoginExtendBeanTool;
import com.didiglobal.logi.security.service.LoginService;
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

