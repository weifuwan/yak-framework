package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "项目管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/project")
public class ProjectController {

  private final ProjectService projectService;

  /**
   * 创建项目管理接口。
   *
   * @param projectService 项目服务
   */
  public ProjectController(
          ProjectService projectService) {

    this.projectService = projectService;
  }

  /**
   * 根据项目 ID 查询项目详情。
   *
   * @param projectId 项目 ID
   * @return 项目详情
   */
  @Operation(summary = "根据项目 ID 查询项目详情")
  @GetMapping("/{id}")
  public Result<ProjectVO> detail(
          @PathVariable("id") Long projectId) {

    try {
      return Result.success(
              projectService
                      .getProjectDetailByProjectId(
                              projectId));
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 校验项目是否存在。
   *
   * @param projectId 项目 ID
   * @return 项目是否存在
   */
  @Operation(summary = "校验项目是否存在")
  @GetMapping("/{id}/exist")
  public Result<Boolean> checkExist(
          @PathVariable("id") Long projectId) {

    return Result.success(
            projectService.checkProjectExist(
                    projectId));
  }

  /**
   * 切换项目状态。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @return 状态切换结果
   */
  @Operation(summary = "切换项目状态")
  @PutMapping("/switch/{id}")
  public Result<Void> switchStatus(
          HttpServletRequest request,
          @PathVariable("id") Long projectId) {

    try {
      projectService.changeProjectStatus(
              projectId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 更新项目。
   *
   * @param request HTTP 请求
   * @param projectSaveDTO 项目信息
   * @return 更新结果
   */
  @Operation(summary = "更新项目")
  @PutMapping
  public Result<Void> update(
          HttpServletRequest request,
          @RequestBody
                  ProjectSaveDTO projectSaveDTO) {

    try {
      projectService.updateProject(
              projectSaveDTO,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 创建项目。
   *
   * @param request HTTP 请求
   * @param projectSaveDTO 项目信息
   * @return 创建后的项目详情
   */
  @Operation(summary = "创建项目")
  @PostMapping
  public Result<ProjectVO> create(
          HttpServletRequest request,
          @RequestBody
                  ProjectSaveDTO projectSaveDTO) {

    try {
      return Result.success(
              projectService.createProject(
                      projectSaveDTO,
                      HttpRequestUtil.getOperator(
                              request)));
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 执行项目删除前校验。
   *
   * @param projectId 项目 ID
   * @return 删除校验结果
   */
  @Operation(summary = "执行项目删除前校验")
  @GetMapping("/delete/check/{id}")
  public Result<ProjectDeleteCheckVO> deleteCheck(
          @PathVariable("id") Long projectId) {

    return Result.success(
            projectService.checkBeforeDelete(
                    projectId));
  }

  /**
   * 根据项目 ID 删除项目。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @return 删除结果
   */
  @Operation(summary = "根据项目 ID 删除项目")
  @DeleteMapping("/{id}")
  public Result<Void> delete(
          HttpServletRequest request,
          @PathVariable("id") Long projectId) {

    try {
      projectService.deleteProjectByProjectId(
              projectId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 分页查询项目。
   *
   * @param queryDTO 查询条件
   * @return 项目分页结果
   */
  @Operation(summary = "分页查询项目")
  @PostMapping("/page")
  public PagingResult<ProjectVO> page(
          @RequestBody ProjectQueryDTO queryDTO) {

    PagingData<ProjectVO> pagingData =
            projectService.getProjectPage(
                    queryDTO);

    return PagingResult.success(pagingData);
  }

  /**
   * 查询全部项目简要信息。
   *
   * @return 项目简要信息列表
   */
  @Operation(summary = "查询全部项目简要信息")
  @GetMapping("/list")
  public Result<List<ProjectBriefVO>> list() {
    return Result.success(
            projectService.getProjectBriefList());
  }

  /**
   * 添加项目负责人。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @param ownerId 负责人 ID
   * @return 添加结果
   */
  @Operation(summary = "添加项目负责人")
  @PutMapping("/{id}/owner/{ownerId}")
  public Result<Void> addProjectOwner(
          HttpServletRequest request,
          @PathVariable("id") Long projectId,
          @PathVariable Long ownerId) {

    try {
      projectService.addProjectOwner(
              projectId,
              ownerId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 删除项目负责人。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @param ownerId 负责人 ID
   * @return 删除结果
   */
  @Operation(summary = "删除项目负责人")
  @DeleteMapping("/{id}/owner/{ownerId}")
  public Result<Void> deleteProjectOwner(
          HttpServletRequest request,
          @PathVariable("id") Long projectId,
          @PathVariable Long ownerId) {

    try {
      projectService.delProjectOwner(
              projectId,
              ownerId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 添加项目用户。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @param userId 用户 ID
   * @return 添加结果
   */
  @Operation(summary = "添加项目用户")
  @PutMapping("/{id}/user/{userId}")
  public Result<Void> addProjectUser(
          HttpServletRequest request,
          @PathVariable("id") Long projectId,
          @PathVariable Long userId) {

    try {
      projectService.addProjectUser(
              projectId,
              userId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 删除项目用户。
   *
   * @param request HTTP 请求
   * @param projectId 项目 ID
   * @param userId 用户 ID
   * @return 删除结果
   */
  @Operation(summary = "删除项目用户")
  @DeleteMapping("/{id}/user/{userId}")
  public Result<Void> deleteProjectUser(
          HttpServletRequest request,
          @PathVariable("id") Long projectId,
          @PathVariable Long userId) {

    try {
      projectService.delProjectUser(
              projectId,
              userId,
              HttpRequestUtil.getOperator(request));

      return Result.success(null);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 查询项目未分配用户。
   *
   * @param projectId 项目 ID
   * @return 未分配用户列表
   */
  @Operation(summary = "查询项目未分配用户")
  @GetMapping("/unassigned")
  public Result<List<UserBriefVO>> unassigned(
          @RequestParam("id") Long projectId) {

    try {
      return projectService
              .unassignedByProjectId(
                      projectId);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 根据用户 ID 查询项目简要信息。
   *
   * @param userId 用户 ID
   * @return 项目简要信息列表
   */
  @Operation(summary = "根据用户 ID 查询项目简要信息")
  @GetMapping("/user/{userId}")
  public Result<List<ProjectBriefVO>>
  getProjectBriefByUserId(
          @PathVariable Long userId) {

    return projectService
            .getProjectBriefByUserId(
                    userId);
  }
}