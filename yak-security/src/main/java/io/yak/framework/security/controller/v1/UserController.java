package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.common.vo.user.UserVO;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.HttpRequestUtil;
import io.yak.framework.security.util.JsonUtils;
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
 * 用户管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "用户管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/user")
public class UserController {

  private final UserService userService;

  /**
   * 创建用户管理接口。
   *
   * @param userService 用户服务
   */
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 校验用户字段是否可用。
   *
   * @param type 校验类型
   * @param value 校验值
   * @return 校验结果
   */
  @Operation(summary = "校验用户字段是否可用")
  @GetMapping("/{type}/{value}/check")
  public Result<Void> check(
          @PathVariable Integer type,
          @PathVariable String value) {

    return userService.check(type, value);
  }

  /**
   * 根据用户 ID 集合批量查询用户详情。
   *
   * <p>ids 参数格式示例：{@code [1, 2, 3]}。
   *
   * @param ids 用户 ID JSON 数组
   * @return 用户详情列表
   */
  @Operation(summary = "根据用户 ID 集合批量查询用户详情")
  @GetMapping
  public Result<List<UserVO>> detailList(
          @RequestParam("ids") String ids) {

    try {
      /*
       * 用户 ID 类型是 Long，不能再解析为 Integer。
       */
      List<Long> userIds =
              JsonUtils.toList(ids, Long.class);

      return userService.getUserDetailsByUserIds(
              userIds);
    } catch (Exception exception) {
      throw new YakSecurityException(
              ResultCode.PARAM_NOT_VALID,
              exception);
    }
  }

  /**
   * 根据用户 ID 查询用户详情。
   *
   * @param userId 用户 ID
   * @return 用户详情
   */
  @Operation(summary = "根据用户 ID 查询用户详情")
  @GetMapping("/{id}")
  public Result<UserVO> detail(
          @PathVariable("id") Long userId) {

    return Result.success(
            userService.getUserDetailByUserId(
                    userId));

  }

  /**
   * 分页查询用户。
   *
   * @param queryDTO 查询条件
   * @return 用户分页结果
   */
  @Operation(summary = "分页查询用户")
  @PostMapping("/page")
  public PagingResult<UserVO> page(
          @RequestBody UserQueryDTO queryDTO) {

    PagingData<UserVO> pagingData =
            userService.getUserPage(queryDTO);

    return PagingResult.success(pagingData);
  }

  /**
   * 根据部门 ID 查询用户。
   *
   * @param deptId 部门 ID
   * @return 用户简要信息列表
   */
  @Operation(summary = "根据部门 ID 查询用户")
  @GetMapping("/list/dept/{deptId}")
  public Result<List<UserBriefVO>> listByDeptId(
          @PathVariable Long deptId) {

    return Result.success(
            userService.getUserBriefListByDeptId(
                    deptId));
  }

  /**
   * 根据角色 ID 查询用户。
   *
   * @param roleId 角色 ID
   * @return 用户简要信息列表
   */
  @Operation(summary = "根据角色 ID 查询用户")
  @GetMapping("/list/role/{roleId}")
  public Result<List<UserBriefVO>> listByRoleId(
          @PathVariable Long roleId) {

    return Result.success(
            userService.getUserBriefListByRoleId(
                    roleId));
  }

  /**
   * 查询用户的角色分配信息。
   *
   * @param userId 用户 ID
   * @return 角色分配信息列表
   */
  @Operation(summary = "查询用户的角色分配信息")
  @GetMapping("/assign/list/{userId}")
  public Result<List<AssignInfoVO>> assignList(
          @PathVariable Long userId) {

    return Result.success(
            userService.getAssignInfoListByUserId(
                    userId));

  }

  /**
   * 根据用户名或真实姓名模糊查询用户。
   *
   * @param keyword 查询关键字
   * @return 用户简要信息列表
   */
  @Operation(summary = "根据用户名或真实姓名模糊查询用户")
  @GetMapping("/list/{keyword}")
  public Result<List<UserBriefVO>> listByName(
          @PathVariable String keyword) {

    return Result.success(
            userService.searchUserBriefList(
                    keyword));
  }

  /**
   * 新增用户。
   *
   * <p>保留原有 PUT 请求方式，避免影响现有前端调用。
   *
   * @param request HTTP 请求
   * @param userDTO 用户信息
   * @return 新增结果
   */
  @Operation(summary = "新增用户")
  @PutMapping("/add")
  public Result<Void> add(
          HttpServletRequest request,
          @RequestBody UserDTO userDTO) {

    return userService.addUser(
            userDTO,
            HttpRequestUtil.getOperator(request));
  }

  /**
   * 编辑用户。
   *
   * @param request HTTP 请求
   * @param userDTO 用户信息
   * @return 编辑结果
   */
  @Operation(summary = "编辑用户")
  @PostMapping("/edit")
  public Result<Void> edit(
          HttpServletRequest request,
          @RequestBody UserDTO userDTO) {

    return userService.editUser(
            userDTO,
            HttpRequestUtil.getOperator(request));
  }

  /**
   * 根据用户 ID 删除用户。
   *
   * @param userId 用户 ID
   * @return 删除结果
   */
  @Operation(summary = "根据用户 ID 删除用户")
  @DeleteMapping("/{id}")
  public Result<Void> delete(
          @PathVariable("id") Long userId) {

    return userService.deleteByUserId(userId);
  }
}
