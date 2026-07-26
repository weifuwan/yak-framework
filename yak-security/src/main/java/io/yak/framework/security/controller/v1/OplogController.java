package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import io.yak.framework.security.service.OplogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/oplog"})
public class OplogController {
  @Autowired private OplogService oplogService;

  @PostMapping(value = {"/page"})
  public PagingResult<OplogVO> page(@RequestBody OplogQueryDTO queryDTO) {
    PagingData<OplogVO> pageOplog = this.oplogService.getOplogPage(queryDTO);
    return PagingResult.success(pageOplog);
  }

  @GetMapping(value = {"/{id}"})
  public Result<OplogVO> get(@PathVariable Integer id) {
    OplogVO oplogVO = this.oplogService.getOplogDetailByOplogId(id);
    return Result.success(oplogVO);
  }

  @GetMapping(value = {"/type/list"})
  @ResponseBody
  public Result<List<String>> types() {
    return Result.buildSucc(this.oplogService.listTargetType());
  }
}
