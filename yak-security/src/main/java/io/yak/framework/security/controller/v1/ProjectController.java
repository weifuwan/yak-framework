package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.project.ProjectQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectSaveDTO;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.project.ProjectDeleteCheckVO;
import io.yak.framework.security.common.vo.project.ProjectVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.ProjectService;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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
@RequestMapping(value = {"/yak-security/api/v1/project"})
public class ProjectController {
  @Autowired private ProjectService projectService;

  @GetMapping(value = {"/{id}"})
  public Result<ProjectVO> detail(@PathVariable Integer id) {
    try {
      ProjectVO projectVO = this.projectService.getProjectDetailByProjectId(id);
      return Result.success(projectVO);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @GetMapping(value = {"/{id}/exist"})
  public Result<Boolean> checkExist(@PathVariable Integer id) {
    return Result.buildSucc(this.projectService.checkProjectExist(id));
  }

  @PutMapping(value = {"/switch/{id}"})
  public Result<String> switched(@PathVariable Integer id,
                                 HttpServletRequest request) {
    this.projectService.changeProjectStatus(
        id, HttpRequestUtil.getOperator(request));
    return Result.success();
  }

  @PutMapping
  public Result<String> update(@RequestBody ProjectSaveDTO saveDTO,
                               HttpServletRequest request) {
    try {
      this.projectService.updateProject(saveDTO,
                                        HttpRequestUtil.getOperator(request));
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
    return Result.success();
  }

  @PostMapping
  public Result<ProjectVO> create(@RequestBody ProjectSaveDTO saveDTO,
                                  HttpServletRequest request) {
    try {
      ProjectVO projectVO = this.projectService.createProject(
          saveDTO, HttpRequestUtil.getOperator(request));
      return Result.success(projectVO);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @GetMapping(value = {"/delete/check/{id}"})
  public Result<ProjectDeleteCheckVO> deleteCheck(@PathVariable Integer id) {
    ProjectDeleteCheckVO deleteCheckVO =
        this.projectService.checkBeforeDelete(id);
    return Result.success(deleteCheckVO);
  }

  @DeleteMapping(value = {"/{id}"})
  public Result<String> delete(@PathVariable Integer id,
                               HttpServletRequest request) {
    this.projectService.deleteProjectByProjectId(
        id, HttpRequestUtil.getOperator(request));
    return Result.success();
  }

  @PostMapping(value = {"/page"})
  public PagingResult<ProjectVO> page(@RequestBody ProjectQueryDTO queryDTO) {
    PagingData<ProjectVO> pageProject =
        this.projectService.getProjectPage(queryDTO);
    return PagingResult.success(pageProject);
  }

  @GetMapping(value = {"/list"})
  public Result<List<ProjectBriefVO>> list() {
    List<ProjectBriefVO> projectBriefVOList =
        this.projectService.getProjectBriefList();
    return Result.success(projectBriefVOList);
  }

  @PutMapping(value = {"/{id}/owner/{ownerId}"})
  public Result<String> addProjectOwner(@PathVariable Integer id,
                                        @PathVariable Integer ownerId,
                                        HttpServletRequest request) {
    try {
      this.projectService.addProjectOwner(id, ownerId,
                                          HttpRequestUtil.getOperator(request));
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @DeleteMapping(value = {"/{id}/owner/{ownerId}"})
  public Result<String> deleteProjectOwner(@PathVariable Integer id,
                                           @PathVariable Integer ownerId,
                                           HttpServletRequest request) {
    try {
      this.projectService.delProjectOwner(id, ownerId,
                                          HttpRequestUtil.getOperator(request));
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @PutMapping(value = {"/{id}/user/{userId}"})
  public Result<String> addProjectUser(@PathVariable Integer id,
                                       @PathVariable Integer userId,
                                       HttpServletRequest request) {
    try {
      this.projectService.addProjectUser(id, userId,
                                         HttpRequestUtil.getOperator(request));
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @DeleteMapping(value = {"/{id}/user/{userId}"})
  public Result<String> deleteProjectUser(@PathVariable Integer id,
                                          @PathVariable Integer userId,
                                          HttpServletRequest request) {
    try {
      this.projectService.delProjectUser(id, userId,
                                         HttpRequestUtil.getOperator(request));
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
    return Result.success();
  }

  @GetMapping(value = {"/unassigned"})
  public Result<List<UserBriefVO>>
  unassigned(@RequestParam(value = "id") Integer id) {
    try {
      return this.projectService.unassignedByProjectId(id);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @GetMapping(value = {"/user/{userId}"})
  public Result<List<ProjectBriefVO>>
  getProjectBriefByUserId(@PathVariable(value = "userId") Integer userId) {
    return this.projectService.getProjectBriefByUserId(userId);
  }
}
