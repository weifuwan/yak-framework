/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.didiglobal.logi.security.controller.v1;

import com.didiglobal.logi.security.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-common\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-common\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/common"})
public class CommonController {
    @GetMapping(value={"/heart"})
    @ApiOperation(value="http\u8bf7\u6c42\u6d4b\u8bd5", notes="http\u8bf7\u6c42\u6d4b\u8bd5")
    public Result<String> health() {
        return Result.success("\u4e00\u4e2a\u666e\u901a\u7684\u8bf7\u6c42\u54cd\u5e94\u4e86\u666e\u901a\u7684\u7ed3\u679c");
    }
}

