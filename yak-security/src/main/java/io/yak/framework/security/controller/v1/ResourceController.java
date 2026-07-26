package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.resource.AssignToManyUserDTO;
import io.yak.framework.security.common.dto.resource.AssignToOneUserDTO;
import io.yak.framework.security.common.dto.resource.BatchAssignDTO;
import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUQueryDTO;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import io.yak.framework.security.common.vo.resource.MByRDataVO;
import io.yak.framework.security.common.vo.resource.MByRVO;
import io.yak.framework.security.common.vo.resource.MByUDataVO;
import io.yak.framework.security.common.vo.resource.MByUVO;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import io.yak.framework.security.service.ResourceTypeService;
import io.yak.framework.security.service.UserResourceService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源权限管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "资源权限管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/resource")
public class ResourceController {

  private final UserResourceService userResourceService;

  private final ResourceTypeService resourceTypeService;

  /**
   * 创建资源权限管理接口。
   *
   * @param userResourceService 用户资源权限服务
   * @param resourceTypeService 资源类型服务
   */
  public ResourceController(
          UserResourceService userResourceService,
          ResourceTypeService resourceTypeService) {

    this.userResourceService = userResourceService;
    this.resourceTypeService = resourceTypeService;
  }

  /**
   * 查询全部资源类型。
   *
   * @return 资源类型列表
   */
  @Operation(summary = "查询全部资源类型")
  @GetMapping("/type/list")
  public Result<List<ResourceTypeVO>> typeList() {
    return Result.success(
            resourceTypeService.getAllResourceTypeList());
  }

  /**
   * 导入资源类型。
   *
   * @param typeNameList 资源类型名称列表
   * @return 导入结果
   */
  @Operation(summary = "导入资源类型")
  @PostMapping("/type/import")
  public Result<Void> typeImport(
          @RequestBody List<String> typeNameList) {

    resourceTypeService.saveResourceType(
            typeNameList);

    return Result.success(null);
  }

  /**
   * 查询资源查看权限控制状态。
   *
   * @return 是否开启查看权限控制
   */
  @Operation(summary = "查询资源查看权限控制状态")
  @GetMapping("/vpc/status")
  public Result<Boolean> vpcStatus() {
    return Result.success(
            userResourceService
                    .getViewPermissionControlStatus());
  }

  /**
   * 切换资源查看权限控制状态。
   *
   * @return 切换结果
   */
  @Operation(summary = "切换资源查看权限控制状态")
  @PutMapping("/vpc/switch")
  public Result<Void> vpcSwitch() {
    userResourceService
            .changeResourceViewControlStatus();

    return Result.success(null);
  }

  /**
   * 查询按用户管理的资源权限数据。
   *
   * @param queryDTO 查询条件
   * @return 资源权限数据列表
   */
  @Operation(summary = "查询按用户管理的资源权限数据")
  @PostMapping("/mbu/list")
  public Result<List<MByUDataVO>> mbuList(
          @RequestBody MByUDataQueryDTO queryDTO) {

    return Result.success(
            userResourceService
                    .getManagerByUserDataList(
                            queryDTO));

  }

  /**
   * 查询按资源管理的用户权限数据。
   *
   * @param queryDTO 查询条件
   * @return 用户权限数据列表
   */
  @Operation(summary = "查询按资源管理的用户权限数据")
  @PostMapping("/mbr/list")
  public Result<List<MByRDataVO>> mbrList(
          @RequestBody MByRDataQueryDTO queryDTO) {

    return Result.success(
            userResourceService
                    .getManagerByResourceDataList(
                            queryDTO));

  }

  /**
   * 分页查询按资源管理的权限信息。
   *
   * @param queryDTO 查询条件
   * @return 权限分页结果
   */
  @Operation(summary = "分页查询按资源管理的权限信息")
  @PostMapping("/mbr/page")
  public PagingResult<MByRVO> mbrPage(
          @RequestBody MByRQueryDTO queryDTO) {

    PagingData<MByRVO> pagingData =
            userResourceService
                    .getManageByResourcePage(
                            queryDTO);

    return PagingResult.success(pagingData);

  }

  /**
   * 分页查询按用户管理的权限信息。
   *
   * @param queryDTO 查询条件
   * @return 权限分页结果
   */
  @Operation(summary = "分页查询按用户管理的权限信息")
  @PostMapping("/mbu/page")
  public PagingResult<MByUVO> mbuPage(
          @RequestBody MByUQueryDTO queryDTO) {

    PagingData<MByUVO> pagingData =
            userResourceService
                    .getManageByUserPage(
                            queryDTO);

    return PagingResult.success(pagingData);
  }

  /**
   * 为多个用户分配资源权限。
   *
   * @param assignDTO 分配参数
   * @return 分配结果
   */
  @Operation(summary = "为多个用户分配资源权限")
  @PostMapping("/permission/mbr/assign")
  public Result<Void> mbrAssign(
          @RequestBody AssignToManyUserDTO assignDTO) {

    userResourceService
            .assignResourcePermission(
                    assignDTO);

    return Result.success(null);

  }

  /**
   * 为单个用户分配资源权限。
   *
   * @param assignDTO 分配参数
   * @return 分配结果
   */
  @Operation(summary = "为单个用户分配资源权限")
  @PostMapping("/permission/mbu/assign")
  public Result<Void> mbuAssign(
          @RequestBody AssignToOneUserDTO assignDTO) {

    userResourceService
            .assignResourcePermission(
                    assignDTO);

    return Result.success(null);

  }

  /**
   * 批量分配资源权限。
   *
   * @param assignDTO 批量分配参数
   * @return 分配结果
   */
  @Operation(summary = "批量分配资源权限")
  @PostMapping("/permission/assign/batch")
  public Result<Void> batchAssign(
          @RequestBody BatchAssignDTO assignDTO) {

    userResourceService
            .batchAssignResourcePermission(
                    assignDTO);

    return Result.success(null);

  }

  /**
   * 查询资源权限控制级别。
   *
   * @param queryDTO 查询条件
   * @return 权限控制级别
   */
  @Operation(summary = "查询资源权限控制级别")
  @PostMapping("/control/level")
  public Result<Integer> getControlLevel(
          @RequestBody ControlLevelQueryDTO queryDTO) {

    ControlLevelCode controlLevel =
            userResourceService
                    .getControlLevel(
                            queryDTO);

    return Result.success(
            controlLevel.getType());

  }
}
