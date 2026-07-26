/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiImplicitParam
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.security.controller.v1;

import com.yak.security.common.PagingData;
import com.yak.security.common.PagingResult;
import com.yak.security.common.Result;
import com.yak.security.common.dto.oplog.OplogQueryDTO;
import com.yak.security.common.vo.oplog.OplogVO;
import com.yak.security.service.OplogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-oplog\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u64cd\u4f5c\u65e5\u5fd7\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/oplog"})
public class OplogController {
    @Autowired
    private OplogService oplogService;

    @PostMapping(value={"/page"})
    @ApiOperation(value="\u67e5\u8be2\u64cd\u4f5c\u65e5\u5fd7\u5217\u8868", notes="\u5206\u9875\u548c\u6761\u4ef6\u67e5\u8be2")
    public PagingResult<OplogVO> page(@RequestBody OplogQueryDTO queryDTO) {
        PagingData<OplogVO> pageOplog = this.oplogService.getOplogPage(queryDTO);
        return PagingResult.success(pageOplog);
    }

    @GetMapping(value={"/{id}"})
    @ApiOperation(value="\u83b7\u53d6\u64cd\u4f5c\u65e5\u5fd7\u8be6\u60c5", notes="\u6839\u636e\u64cd\u4f5c\u65e5\u5fd7id\u83b7\u53d6\u64cd\u4f5c\u65e5\u5fd7\u8be6\u60c5")
    @ApiImplicitParam(name="id", value="\u64cd\u4f5c\u65e5\u5fd7id", dataType="int", required=true)
    public Result<OplogVO> get(@PathVariable Integer id) {
        OplogVO oplogVO = this.oplogService.getOplogDetailByOplogId(id);
        return Result.success(oplogVO);
    }

    @GetMapping(value={"/type/list"})
    @ResponseBody
    @ApiOperation(value="\u83b7\u53d6\u65e5\u5fd7\u7684\u6a21\u5757\u5217\u8868", notes="")
    public Result<List<String>> types() {
        return Result.buildSucc(this.oplogService.listTargetType());
    }
}

