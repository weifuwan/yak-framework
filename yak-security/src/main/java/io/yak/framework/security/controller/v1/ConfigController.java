package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.config.ConfigDTO;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.vo.config.ConfigVO;
import io.yak.framework.security.service.ConfigService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/config"})
public class ConfigController {
  @Autowired private ConfigService configService;

  @PostMapping(value = {"/list"})
  @ResponseBody
  public Result<List<ConfigVO>> list(@RequestBody ConfigDTO param) {
    return Result.buildSucc(CopyBeanUtil.copyList(
        this.configService.queryByCondt(param), ConfigVO.class));
  }

  @PostMapping(value = {"/page"})
  public PagingResult<ConfigVO> page(@RequestBody ConfigQueryDTO queryDTO) {
    PagingData<ConfigVO> configVO = this.configService.pagingConfig(queryDTO);
    return PagingResult.success(configVO);
  }

  @GetMapping(value = {"/group/list"})
  @ResponseBody
  public Result<List<String>> groups() {
    return Result.buildSucc(this.configService.listGroups());
  }

  @GetMapping(value = {"/get"})
  @ResponseBody
  public Result<ConfigVO>
  get(@RequestParam(value = "configId") Integer configId) {
    return Result.buildSucc(CopyBeanUtil.copy(
        this.configService.getConfigById(configId), ConfigVO.class));
  }

  @PostMapping(value = {"/switch"})
  @ResponseBody
  public Result<Void> switchConfig(HttpServletRequest request,
                                   @RequestBody ConfigDTO param) {
    return this.configService.switchConfig(
        param.getId(), param.getStatus(), HttpRequestUtil.getOperator(request));
  }

  @DeleteMapping(value = {"/del"})
  @ResponseBody
  public Result<Void> delete(HttpServletRequest request,
                             @RequestParam(value = "id") Integer id) {
    return this.configService.delConfig(id,
                                        HttpRequestUtil.getOperator(request));
  }

  @PutMapping(value = {"/add"})
  @ResponseBody
  public Result<Integer> add(HttpServletRequest request,
                             @RequestBody ConfigDTO param) {
    return this.configService.addConfig(param,
                                        HttpRequestUtil.getOperator(request));
  }

  @PostMapping(value = {"/edit"})
  @ResponseBody
  public Result<Void> edit(HttpServletRequest request,
                           @RequestBody ConfigDTO param) {
    return this.configService.editConfig(param,
                                         HttpRequestUtil.getOperator(request));
  }
}
