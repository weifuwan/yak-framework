/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.didiglobal.logi.security.controller.v1;

import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.permission.PermissionDTO;
import com.didiglobal.logi.security.common.vo.permission.PermissionTreeVO;
import com.didiglobal.logi.security.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-permission\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u6743\u9650\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/permission"})
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping(value={"/tree"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u6743\u9650", notes="\u4ee5\u6811\u7684\u5f62\u5f0f\u8fd4\u56de\u6240\u6709\u6743\u9650")
    public Result<PermissionTreeVO> tree() {
        PermissionTreeVO permissionTreeVO = this.permissionService.buildPermissionTree();
        return Result.success(permissionTreeVO);
    }

    @PostMapping(value={"/import"})
    @ApiOperation(value="\u6743\u9650\u4fe1\u606f\u5bfc\u5165", notes="\u6743\u9650\u4fe1\u606f\u5bfc\u5165")
    public Result<String> imports(@RequestBody @ApiParam(name="permissionDTOList", value="\u6743\u9650\u4fe1\u606fList") List<PermissionDTO> list) {
        this.permissionService.savePermission(list);
        return Result.success();
    }
}

