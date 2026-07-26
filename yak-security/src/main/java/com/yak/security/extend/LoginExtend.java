/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 */
package com.yak.security.extend;

import com.yak.security.common.Result;
import com.yak.security.common.dto.account.AccountLoginDTO;
import com.yak.security.common.vo.user.UserBriefVO;
import com.yak.security.exception.LogiSecurityException;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginExtend {
    public UserBriefVO verifyLogin(AccountLoginDTO var1, HttpServletRequest var2, HttpServletResponse var3) throws LogiSecurityException;

    public Result<Boolean> logout(HttpServletRequest var1, HttpServletResponse var2);

    public boolean interceptorCheck(HttpServletRequest var1, HttpServletResponse var2, String var3, List<String> var4) throws IOException;
}

