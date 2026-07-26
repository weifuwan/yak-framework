package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import io.yak.framework.security.service.OplogService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "操作日志管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/oplog")
public class OplogController {

  private final OplogService oplogService;

  /**
   * 创建操作日志管理接口。
   *
   * @param oplogService 操作日志服务
   */
  public OplogController(
          OplogService oplogService) {

    this.oplogService = oplogService;
  }

  /**
   * 分页查询操作日志。
   *
   * @param queryDTO 查询条件
   * @return 操作日志分页结果
   */
  @Operation(summary = "分页查询操作日志")
  @PostMapping("/page")
  public PagingResult<OplogVO> page(
          @RequestBody OplogQueryDTO queryDTO) {

    PagingData<OplogVO> pagingData =
            oplogService.getOplogPage(
                    queryDTO);

    return PagingResult.success(
            pagingData);
  }

  /**
   * 根据操作日志 ID 查询日志详情。
   *
   * @param oplogId 操作日志 ID
   * @return 操作日志详情
   */
  @Operation(summary = "根据操作日志 ID 查询日志详情")
  @GetMapping("/{id}")
  public Result<OplogVO> detail(
          @PathVariable("id") Long oplogId) {

    return Result.buildSucc(
            oplogService
                    .getOplogDetailByOplogId(
                            oplogId));
  }

  /**
   * 查询全部操作目标类型。
   *
   * @return 操作目标类型列表
   */
  @Operation(summary = "查询全部操作目标类型")
  @GetMapping("/type/list")
  public Result<List<String>> targetTypeList() {
    return Result.buildSucc(
            oplogService.listTargetType());
  }
}