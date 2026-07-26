package io.yak.framework.security.controller.v1;

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
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.ResourceTypeService;
import io.yak.framework.security.service.UserResourceService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/resource"})
public class ResourceController {
  @Autowired private UserResourceService userResourceService;
  @Autowired private ResourceTypeService resourceTypeService;

  @GetMapping(value = {"/type/list"})
  public Result<List<ResourceTypeVO>> typeList() {
    List<ResourceTypeVO> resourceTypeVOList =
        this.resourceTypeService.getAllResourceTypeList();
    return Result.success(resourceTypeVOList);
  }

  @PostMapping(value = {"/type/import"})
  public Result<String> typeImport(@RequestBody @ApiParam(
      name = "list",
      value = "\u8d44\u6e90\u7c7b\u578b\u540dList") List<String> list) {
    this.resourceTypeService.saveResourceType(list);
    return Result.success();
  }

  @GetMapping(value = {"/vpc/status"})
  public Result<Boolean> vpcStatus() {
    boolean isOn = this.userResourceService.getViewPermissionControlStatus();
    return Result.success(isOn);
  }

  @PutMapping(value = {"/vpc/switch"})
  public Result<String> vpcSwitch() {
    this.userResourceService.changeResourceViewControlStatus();
    return Result.success();
  }

  @PostMapping(value = {"/mbu/list"})
  public Result<List<MByUDataVO>>
  mbuList(@RequestBody MByUDataQueryDTO queryDTO) {
    try {
      List<MByUDataVO> resultList =
          this.userResourceService.getManagerByUserDataList(queryDTO);
      return Result.success(resultList);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/mbr/list"})
  public Result<List<MByRDataVO>>
  mbrList(@RequestBody MByRDataQueryDTO queryDTO) {
    try {
      List<MByRDataVO> resultList =
          this.userResourceService.getManagerByResourceDataList(queryDTO);
      return Result.success(resultList);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/mbr/page"})
  public PagingResult<MByRVO> mbrPage(@RequestBody MByRQueryDTO queryDTO) {
    try {
      PagingData<MByRVO> pagingData =
          this.userResourceService.getManageByResourcePage(queryDTO);
      return PagingResult.success(pagingData);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return PagingResult.fail(e);
    }
  }

  @PostMapping(value = {"/mbu/page"})
  public PagingResult<MByUVO> mbuPage(@RequestBody MByUQueryDTO queryDTO) {
    PagingData<MByUVO> pagingData =
        this.userResourceService.getManageByUserPage(queryDTO);
    return PagingResult.success(pagingData);
  }

  @PostMapping(value = {"/permission/mbr/assign"})
  public Result<String> mbrAssign(@RequestBody AssignToManyUserDTO assignDTO,
                                  HttpServletRequest request) {
    try {
      this.userResourceService.assignResourcePermission(assignDTO, request);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
    return Result.success();
  }

  @PostMapping(value = {"/permission/mbu/assign"})
  public Result<String> mbuAssign(@RequestBody AssignToOneUserDTO assignDTO) {
    try {
      this.userResourceService.assignResourcePermission(assignDTO);
      return Result.success();
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/permission/assign/batch"})
  public Result<String> batchAssign(@RequestBody BatchAssignDTO assignDTO,
                                    HttpServletRequest request) {
    try {
      this.userResourceService.batchAssignResourcePermission(assignDTO,
                                                             request);
      return Result.success();
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/control/level"})
  public Result<Integer>
  getControlLevel(@RequestBody ControlLevelQueryDTO queryDTO) {
    try {
      ControlLevelCode controlLevel =
          this.userResourceService.getControlLevel(queryDTO);
      return Result.success(controlLevel.getType());
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }
}
