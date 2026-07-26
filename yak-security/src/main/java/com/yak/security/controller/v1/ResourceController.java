/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.security.controller.v1;

import com.yak.security.common.PagingData;
import com.yak.security.common.PagingResult;
import com.yak.security.common.Result;
import com.yak.security.common.dto.resource.AssignToManyUserDTO;
import com.yak.security.common.dto.resource.AssignToOneUserDTO;
import com.yak.security.common.dto.resource.BatchAssignDTO;
import com.yak.security.common.dto.resource.ControlLevelQueryDTO;
import com.yak.security.common.dto.resource.MByRDataQueryDTO;
import com.yak.security.common.dto.resource.MByRQueryDTO;
import com.yak.security.common.dto.resource.MByUDataQueryDTO;
import com.yak.security.common.dto.resource.MByUQueryDTO;
import com.yak.security.common.enums.resource.ControlLevelCode;
import com.yak.security.common.vo.resource.MByRDataVO;
import com.yak.security.common.vo.resource.MByRVO;
import com.yak.security.common.vo.resource.MByUDataVO;
import com.yak.security.common.vo.resource.MByUVO;
import com.yak.security.common.vo.resource.ResourceTypeVO;
import com.yak.security.exception.LogiSecurityException;
import com.yak.security.service.ResourceTypeService;
import com.yak.security.service.UserResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-resource\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u8d44\u6e90\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/resource"})
public class ResourceController {
    @Autowired
    private UserResourceService userResourceService;
    @Autowired
    private ResourceTypeService resourceTypeService;

    @GetMapping(value={"/type/list"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u8d44\u6e90\u7c7b\u578b", notes="\u83b7\u53d6\u6240\u6709\u8d44\u6e90\u7c7b\u578b")
    public Result<List<ResourceTypeVO>> typeList() {
        List<ResourceTypeVO> resourceTypeVOList = this.resourceTypeService.getAllResourceTypeList();
        return Result.success(resourceTypeVOList);
    }

    @PostMapping(value={"/type/import"})
    @ApiOperation(value="\u5bfc\u5165\u8d44\u6e90\u7c7b\u578b", notes="\u6279\u91cf\u5bfc\u5165\u8d44\u6e90\u7c7b\u578b\u540d")
    public Result<String> typeImport(@RequestBody @ApiParam(name="list", value="\u8d44\u6e90\u7c7b\u578b\u540dList") List<String> list) {
        this.resourceTypeService.saveResourceType(list);
        return Result.success();
    }

    @GetMapping(value={"/vpc/status"})
    @ApiOperation(value="\u83b7\u53d6\u8d44\u6e90\u67e5\u770b\u6743\u9650\u63a7\u5236\u72b6\u6001", notes="true\u5f00\u542f\u3001false\u5173\u95ed\uff0cvpc\uff08ViewPermissionControl\uff09")
    public Result<Boolean> vpcStatus() {
        boolean isOn = this.userResourceService.getViewPermissionControlStatus();
        return Result.success(isOn);
    }

    @PutMapping(value={"/vpc/switch"})
    @ApiOperation(value="\u66f4\u6539\u8d44\u6e90\u67e5\u770b\u6743\u9650\u63a7\u5236\u72b6\u6001", notes="\u8c03\u7528\u8be5\u63a5\u53e3\u5219\u8d44\u6e90\u67e5\u770b\u6743\u9650\u63a7\u5236\u72b6\u6001\u88ab\u53cd\u8f6c")
    public Result<String> vpcSwitch() {
        this.userResourceService.changeResourceViewControlStatus();
        return Result.success();
    }

    @PostMapping(value={"/mbu/list"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u7528\u6237\u7ba1\u7406/\u5206\u914d\u8d44\u6e90/\u6570\u636e\u5217\u8868", notes="\u83b7\u53d6\u6570\u636e\uff08\u9879\u76ee\u3001\u7c7b\u522b\u3001\u8d44\u6e90\uff09list")
    public Result<List<MByUDataVO>> mbuList(@RequestBody MByUDataQueryDTO queryDTO) {
        try {
            List<MByUDataVO> resultList = this.userResourceService.getManagerByUserDataList(queryDTO);
            return Result.success(resultList);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/mbr/list"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u8d44\u6e90\u7ba1\u7406/\u5206\u914d\u7528\u6237/\u6570\u636e\u5217\u8868", notes="\u83b7\u53d6\u7528\u6237list")
    public Result<List<MByRDataVO>> mbrList(@RequestBody MByRDataQueryDTO queryDTO) {
        try {
            List<MByRDataVO> resultList = this.userResourceService.getManagerByResourceDataList(queryDTO);
            return Result.success(resultList);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/mbr/page"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u8d44\u6e90\u7ba1\u7406/\u5217\u8868\u4fe1\u606f", notes="\u6309\u8d44\u6e90\u7ba1\u7406\u7684\u5217\u8868\u4fe1\u606f\uff0cmbr\uff08ManageByResource\uff09")
    public PagingResult<MByRVO> mbrPage(@RequestBody MByRQueryDTO queryDTO) {
        try {
            PagingData<MByRVO> pagingData = this.userResourceService.getManageByResourcePage(queryDTO);
            return PagingResult.success(pagingData);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return PagingResult.fail(e);
        }
    }

    @PostMapping(value={"/mbu/page"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u7528\u6237\u7ba1\u7406/\u5217\u8868\u4fe1\u606f", notes="\u6309\u7528\u6237\u7ba1\u7406\u7684\u5217\u8868\u4fe1\u606f\uff0cmbu\uff08ManageByUser\uff09")
    public PagingResult<MByUVO> mbuPage(@RequestBody MByUQueryDTO queryDTO) {
        PagingData<MByUVO> pagingData = this.userResourceService.getManageByUserPage(queryDTO);
        return PagingResult.success(pagingData);
    }

    @PostMapping(value={"/permission/mbr/assign"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u8d44\u6e90\u7ba1\u7406/\u5206\u914d\u7528\u6237", notes="1\u4e2a\u9879\u76ee\u62161\u4e2a\u8d44\u6e90\u7c7b\u522b\u62161\u4e2a\u5177\u4f53\u8d44\u6e90\u7684\u6743\u9650\u5206\u914d\u7ed9N\u4e2a\u7528\u6237")
    public Result<String> mbrAssign(@RequestBody AssignToManyUserDTO assignDTO, HttpServletRequest request) {
        try {
            this.userResourceService.assignResourcePermission(assignDTO, request);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
        return Result.success();
    }

    @PostMapping(value={"/permission/mbu/assign"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6309\u7528\u6237\u7ba1\u7406/\u5206\u914d\u8d44\u6e90", notes="N\u4e2a\u9879\u76ee\u6216N\u4e2a\u8d44\u6e90\u7c7b\u522b\u6216N\u4e2a\u5177\u4f53\u8d44\u6e90\u7684\u6743\u9650\u5206\u914d\u7ed91\u4e2a\u7528\u6237")
    public Result<String> mbuAssign(@RequestBody AssignToOneUserDTO assignDTO) {
        try {
            this.userResourceService.assignResourcePermission(assignDTO);
            return Result.success();
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/permission/assign/batch"})
    @ApiOperation(value="\u8d44\u6e90\u6743\u9650\u7ba1\u7406/\u6279\u91cf\u5206\u914d\u7528\u6237\u548c\u6279\u91cf\u5206\u914d\u8d44\u6e90", notes="\u6279\u91cf\u5206\u914d\u7528\u6237\uff1a\u5206\u914d\u4e4b\u524d\u5148\u5220\u9664N\u8d44\u6e90\u5148\u524d\u5df2\u5206\u914d\u7684\u7528\u6237\u3001\u6279\u91cf\u5206\u914d\u8d44\u6e90\uff1a\u5206\u914d\u4e4b\u524d\u5148\u5220\u9664N\u7528\u6237\u5df2\u62e5\u6709\u7684\u8d44\u6e90\u6743\u9650")
    public Result<String> batchAssign(@RequestBody BatchAssignDTO assignDTO, HttpServletRequest request) {
        try {
            this.userResourceService.batchAssignResourcePermission(assignDTO, request);
            return Result.success();
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @PostMapping(value={"/control/level"})
    @ApiOperation(value="\u83b7\u53d6\u7528\u6237\u62e5\u6709\u8d44\u6e90\u7684\u6743\u9650\u7c7b\u522b", notes="0 \u65e0\u6743\u9650\u30011 \u67e5\u770b\u6743\u9650\u30012 \u7ba1\u7406\u6743\u9650")
    public Result<Integer> getControlLevel(@RequestBody ControlLevelQueryDTO queryDTO) {
        try {
            ControlLevelCode controlLevel = this.userResourceService.getControlLevel(queryDTO);
            return Result.success(controlLevel.getType());
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }
}

