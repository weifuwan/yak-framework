/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiImplicitParam
 *  io.swagger.annotations.ApiOperation
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.didiglobal.logi.security.controller.v1;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.PagingResult;
import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.project.ProjectQueryDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectSaveDTO;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVO;
import com.didiglobal.logi.security.common.vo.project.ProjectDeleteCheckVO;
import com.didiglobal.logi.security.common.vo.project.ProjectVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import com.didiglobal.logi.security.service.ProjectService;
import com.didiglobal.logi.security.util.HttpRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="logi-security-project\u76f8\u5173API\u63a5\u53e3", tags={"logi-security-\u9879\u76ee\u76f8\u5173API\u63a5\u53e3"})
@RequestMapping(value={"/logi-security/api/v1/project"})
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping(value={"/{id}"})
    @ApiOperation(value="\u83b7\u53d6\u9879\u76ee\u8be6\u60c5", notes="\u6839\u636e\u9879\u76eeid\u83b7\u53d6\u9879\u76ee\u8be6\u60c5")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<ProjectVO> detail(@PathVariable Integer id) {
        try {
            ProjectVO projectVO = this.projectService.getProjectDetailByProjectId(id);
            return Result.success(projectVO);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @GetMapping(value={"/{id}/exist"})
    @ApiOperation(value="\u6821\u9a8c\u9879\u76ee\u662f\u5426\u5b58\u5728", notes="\u6821\u9a8c\u9879\u76ee\u662f\u5426\u5b58\u5728")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<Boolean> checkExist(@PathVariable Integer id) {
        return Result.buildSucc(this.projectService.checkProjectExist(id));
    }

    @PutMapping(value={"/switch/{id}"})
    @ApiOperation(value="\u66f4\u6539\u9879\u76ee\u8fd0\u884c\u72b6\u6001", notes="\u8c03\u7528\u8be5\u63a5\u53e3\u5219\u9879\u76ee\u8fd0\u884c\u72b6\u6001\u88ab\u53cd\u8f6c")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<String> switched(@PathVariable Integer id, HttpServletRequest request) {
        this.projectService.changeProjectStatus(id, HttpRequestUtil.getOperator(request));
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value="\u66f4\u65b0\u9879\u76ee", notes="\u6839\u636e\u9879\u76eeid\u66f4\u65b0\u9879\u76ee\u4fe1\u606f")
    public Result<String> update(@RequestBody ProjectSaveDTO saveDTO, HttpServletRequest request) {
        try {
            this.projectService.updateProject(saveDTO, HttpRequestUtil.getOperator(request));
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
        return Result.success();
    }

    @PostMapping
    @ApiOperation(value="\u521b\u5efa\u9879\u76ee", notes="\u521b\u5efa\u9879\u76ee")
    public Result<ProjectVO> create(@RequestBody ProjectSaveDTO saveDTO, HttpServletRequest request) {
        try {
            ProjectVO projectVO = this.projectService.createProject(saveDTO, HttpRequestUtil.getOperator(request));
            return Result.success(projectVO);
        } catch (LogiSecurityException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @GetMapping(value={"/delete/check/{id}"})
    @ApiOperation(value="\u5220\u9664\u9879\u76ee\u524d\u7684\u68c0\u67e5", notes="\u68c0\u67e5\u662f\u5426\u6709\u670d\u52a1\u5f15\u7528\u4e86\u8be5\u9879\u76ee\u3001\u662f\u5426\u6709\u5177\u4f53\u8d44\u6e90\u6302\u4e0a\u4e86\u8be5\u9879\u76ee")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<ProjectDeleteCheckVO> deleteCheck(@PathVariable Integer id) {
        ProjectDeleteCheckVO deleteCheckVO = this.projectService.checkBeforeDelete(id);
        return Result.success(deleteCheckVO);
    }

    @DeleteMapping(value={"/{id}"})
    @ApiOperation(value="\u5220\u9664\u9879\u76ee", notes="\u6839\u636e\u9879\u76eeid\u5220\u9664\u9879\u76ee")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<String> delete(@PathVariable Integer id, HttpServletRequest request) {
        this.projectService.deleteProjectByProjectId(id, HttpRequestUtil.getOperator(request));
        return Result.success();
    }

    @PostMapping(value={"/page"})
    @ApiOperation(value="\u5206\u9875\u67e5\u8be2\u9879\u76ee\u5217\u8868", notes="\u5206\u9875\u548c\u6761\u4ef6\u67e5\u8be2")
    public PagingResult<ProjectVO> page(@RequestBody ProjectQueryDTO queryDTO) {
        PagingData<ProjectVO> pageProject = this.projectService.getProjectPage(queryDTO);
        return PagingResult.success(pageProject);
    }

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u9879\u76ee\u7b80\u8981\u4fe1\u606f", notes="\u83b7\u53d6\u5168\u90e8\u9879\u76ee\u7b80\u8981\u4fe1\u606f\uff08\u53ea\u8fd4\u56deid\u3001\u9879\u76ee\u540d\uff09")
    public Result<List<ProjectBriefVO>> list() {
        List<ProjectBriefVO> projectBriefVOList = this.projectService.getProjectBriefList();
        return Result.success(projectBriefVOList);
    }

    @PutMapping(value={"/{id}/owner/{ownerId}"})
    @ApiOperation(value="\u4ece\u89d2\u8272\u4e2d\u589e\u52a0\u8be5\u9879\u76ee\u4e0b\u7684\u8d1f\u8d23\u4eba", notes="\u4ece\u89d2\u8272\u4e2d\u589e\u52a0\u8be5\u9879\u76ee\u4e0b\u7684\u7528\u6237")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> addProjectOwner(@PathVariable Integer id, @PathVariable Integer ownerId, HttpServletRequest request) {
        try {
            this.projectService.addProjectOwner(id, ownerId, HttpRequestUtil.getOperator(request));
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @DeleteMapping(value={"/{id}/owner/{ownerId}"})
    @ApiOperation(value="\u4ece\u9879\u76ee\u4e2d\u5220\u9664\u8be5\u9879\u76ee\u4e0b\u7684\u8d1f\u8d23\u4eba", notes="\u4ece\u9879\u76ee\u4e2d\u5220\u9664\u8be5\u9879\u76ee\u4e0b\u7684\u8d1f\u8d23\u4eba")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> deleteProjectOwner(@PathVariable Integer id, @PathVariable Integer ownerId, HttpServletRequest request) {
        try {
            this.projectService.delProjectOwner(id, ownerId, HttpRequestUtil.getOperator(request));
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @PutMapping(value={"/{id}/user/{userId}"})
    @ApiOperation(value="\u4ece\u89d2\u8272\u4e2d\u589e\u52a0\u8be5\u9879\u76ee\u4e0b\u7684\u7528\u6237", notes="\u4ece\u89d2\u8272\u4e2d\u589e\u52a0\u8be5\u9879\u76ee\u4e0b\u7684\u7528\u6237")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> addProjectUser(@PathVariable Integer id, @PathVariable Integer userId, HttpServletRequest request) {
        try {
            this.projectService.addProjectUser(id, userId, HttpRequestUtil.getOperator(request));
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @DeleteMapping(value={"/{id}/user/{userId}"})
    @ApiOperation(value="\u4ece\u9879\u76ee\u4e2d\u5220\u9664\u8be5\u9879\u76ee\u4e0b\u7684\u7528\u6237", notes="\u4ece\u9879\u76ee\u4e2d\u5220\u9664\u8be5\u9879\u76ee\u4e0b\u7684\u7528\u6237")
    @ApiImplicitParam(name="id", value="\u89d2\u8272id", dataType="int", required=true)
    public Result<String> deleteProjectUser(@PathVariable Integer id, @PathVariable Integer userId, HttpServletRequest request) {
        try {
            this.projectService.delProjectUser(id, userId, HttpRequestUtil.getOperator(request));
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
        return Result.success();
    }

    @GetMapping(value={"/unassigned"})
    @ApiOperation(value="\u83b7\u53d6\u9879\u76ee\u672a\u5206\u914d\u7684\u7528\u6237\u5217\u8868", notes="\u83b7\u53d6\u9879\u76ee\u672a\u5206\u914d\u7684\u7528\u6237\u5217\u8868")
    @ApiImplicitParam(name="id", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<List<UserBriefVO>> unassigned(@RequestParam(value="id") Integer id) {
        try {
            return this.projectService.unassignedByProjectId(id);
        } catch (LogiSecurityException e) {
            return Result.fail(e);
        }
    }

    @GetMapping(value={"/user/{userId}"})
    @ApiOperation(value="\u83b7\u53d6\u7528\u6237\u7ed1\u5b9a\u7684\u9879\u76ee\u5217\u8868", notes="\u83b7\u53d6\u7528\u6237\u7ed1\u5b9a\u7684\u9879\u76ee\u5217\u8868")
    @ApiImplicitParam(name="userId", value="\u9879\u76eeid", dataType="int", required=true)
    public Result<List<ProjectBriefVO>> getProjectBriefByUserId(@PathVariable(value="userId") Integer userId) {
        return this.projectService.getProjectBriefByUserId(userId);
    }
}

