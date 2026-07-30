package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.common.Result;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.vo.tenant.TenantVO;
import io.yak.framework.security.context.CurrentUser;
import io.yak.framework.security.service.TenantService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前登录用户的租户上下文接口。
 *
 * <p>普通已登录用户可以查询自己可进入的租户，不要求租户管理权限。
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "当前租户接口")
@RestController
@RequestMapping("/yak-security/api/v1/tenant-context")
public class TenantContextController {

  private final CurrentUser currentUser;
  private final TenantService tenantService;

  public TenantContextController(
      CurrentUser currentUser,
      TenantService tenantService) {
    this.currentUser = currentUser;
    this.tenantService = tenantService;
  }

  @Operation(summary = "查询当前用户可进入的租户")
  @GetMapping("/mine")
  public Result<List<TenantVO>> mine() {
    return Result.success(
        tenantService.listByUserId(
            currentUser.getUserId()));
  }

  @Operation(summary = "查询当前请求租户")
  @GetMapping("/current")
  public Result<TenantVO> current() {
    Long tenantId = currentUser.getTenantId();
    return Result.success(
        tenantId == null
            ? null
            : tenantService.detail(tenantId));
  }
}
