/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiImplicitParam
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.security.controller.v1;

import com.yak.security.common.Result;
import com.yak.security.common.vo.message.MessageVO;
import com.yak.security.service.MessageService;
import com.yak.security.util.HttpRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-message\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u6d88\u606f\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/message"})
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping(value={"/list/{readTag}", "/list"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u6d88\u606f", notes="\u6839\u636e\u662f\u5426\u8bfb\u5df2\u8bfb\u83b7\u53d6\u6d88\u606f")
    @ApiImplicitParam(name="readTag", value="\u6d88\u606f\u72b6\u6001\uff08true\u5df2\u8bfb\uff0cfalse\u672a\u8bfb\uff0cnull\u5168\u90e8\uff09", dataType="Boolean")
    public Result<List<MessageVO>> list(@PathVariable(required=false) Boolean readTag, HttpServletRequest request) {
        List<MessageVO> messageVOList = this.messageService.getMessageListByUserIdAndReadTag(HttpRequestUtil.getOperator(request), readTag);
        return Result.success(messageVOList);
    }

    @PutMapping(value={"/switch"})
    @ApiOperation(value="\u66f4\u6539\u6d88\u606f\u72b6\u6001", notes="\u8c03\u7528\u8be5\u63a5\u53e3\u5219\u6d88\u606f\u72b6\u6001\u88ab\u53cd\u8f6c")
    public Result<String> switched(@RequestBody @ApiParam(name="idList", value="\u6d88\u606fidList") List<Integer> idList) {
        this.messageService.changeMessageStatus(idList);
        return Result.success();
    }
}

