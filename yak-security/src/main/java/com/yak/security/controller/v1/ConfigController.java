/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiImplicitParam
 *  io.swagger.annotations.ApiImplicitParams
 *  io.swagger.annotations.ApiOperation
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.security.controller.v1;

import com.yak.security.common.PagingData;
import com.yak.security.common.PagingResult;
import com.yak.security.common.Result;
import com.yak.security.common.dto.config.ConfigDTO;
import com.yak.security.common.dto.config.ConfigQueryDTO;
import com.yak.security.common.vo.config.ConfigVO;
import com.yak.security.service.ConfigService;
import com.yak.security.util.CopyBeanUtil;
import com.yak.security.util.HttpRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-config\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u914d\u7f6e\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/config"})
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @PostMapping(value={"/list"})
    @ResponseBody
    @ApiOperation(value="\u83b7\u53d6\u914d\u7f6e\u5217\u8868\u63a5\u53e3", notes="")
    public Result<List<ConfigVO>> list(@RequestBody ConfigDTO param) {
        return Result.buildSucc(CopyBeanUtil.copyList(this.configService.queryByCondt(param), ConfigVO.class));
    }

    @PostMapping(value={"/page"})
    @ApiOperation(value="\u5206\u9875\u914d\u7f6e\u5217\u8868", notes="\u5206\u9875\u548c\u6761\u4ef6\u67e5\u8be2")
    public PagingResult<ConfigVO> page(@RequestBody ConfigQueryDTO queryDTO) {
        PagingData<ConfigVO> configVO = this.configService.pagingConfig(queryDTO);
        return PagingResult.success(configVO);
    }

    @GetMapping(value={"/group/list"})
    @ResponseBody
    @ApiOperation(value="\u83b7\u53d6\u914d\u7f6e\u7684\u6a21\u5757\u5217\u8868", notes="")
    public Result<List<String>> groups() {
        return Result.buildSucc(this.configService.listGroups());
    }

    @GetMapping(value={"/get"})
    @ResponseBody
    @ApiOperation(value="\u83b7\u53d6\u6307\u5b9a\u914d\u7f6e\u63a5\u53e3", notes="")
    @ApiImplicitParams(value={@ApiImplicitParam(paramType="query", dataType="Integer", name="id", value="\u914d\u7f6eID", required=true)})
    public Result<ConfigVO> get(@RequestParam(value="configId") Integer configId) {
        return Result.buildSucc(CopyBeanUtil.copy(this.configService.getConfigById(configId), ConfigVO.class));
    }

    @PostMapping(value={"/switch"})
    @ResponseBody
    @ApiOperation(value="\u914d\u7f6e\u5f00\u5173\u63a5\u53e3", notes="")
    public Result<Void> switchConfig(HttpServletRequest request, @RequestBody ConfigDTO param) {
        return this.configService.switchConfig(param.getId(), param.getStatus(), HttpRequestUtil.getOperator(request));
    }

    @DeleteMapping(value={"/del"})
    @ResponseBody
    @ApiOperation(value="\u5220\u9664\u914d\u7f6e\u63a5\u53e3", notes="")
    @ApiImplicitParams(value={@ApiImplicitParam(paramType="query", dataType="Integer", name="id", value="\u914d\u7f6eID", required=true)})
    public Result<Void> delete(HttpServletRequest request, @RequestParam(value="id") Integer id) {
        return this.configService.delConfig(id, HttpRequestUtil.getOperator(request));
    }

    @PutMapping(value={"/add"})
    @ResponseBody
    @ApiOperation(value="\u65b0\u5efa\u914d\u7f6e\u63a5\u53e3", notes="")
    public Result<Integer> add(HttpServletRequest request, @RequestBody ConfigDTO param) {
        return this.configService.addConfig(param, HttpRequestUtil.getOperator(request));
    }

    @PostMapping(value={"/edit"})
    @ResponseBody
    @ApiOperation(value="\u7f16\u8f91\u914d\u7f6e\u63a5\u53e3", notes="")
    public Result<Void> edit(HttpServletRequest request, @RequestBody ConfigDTO param) {
        return this.configService.editConfig(param, HttpRequestUtil.getOperator(request));
    }
}

