package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.config.ConfigDTO;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.vo.config.ConfigVO;
import io.yak.framework.security.service.ConfigService;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置管理接口。
 *
 * @author weifuwan
 */
@RestController
@RequestMapping("/yak-security/api/v1/config")
public class ConfigController {

    private final ConfigService configService;

    /**
     * 创建配置管理接口。
     *
     * @param configService 配置服务
     */
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 根据条件查询配置列表。
     *
     * @param condition 查询条件
     * @return 配置列表
     */
    @PostMapping("/list")
    public Result<List<ConfigVO>> list(
            @RequestBody ConfigDTO condition) {

        return Result.buildSucc(
                configService.queryByCondt(condition));
    }

    /**
     * 分页查询配置。
     *
     * @param queryDTO 分页查询条件
     * @return 配置分页结果
     */
    @PostMapping("/page")
    public PagingResult<ConfigVO> page(
            @RequestBody ConfigQueryDTO queryDTO) {

        PagingData<ConfigVO> pagingData =
                configService.pagingConfig(queryDTO);

        return PagingResult.success(pagingData);
    }

    /**
     * 查询全部配置分组。
     *
     * @return 配置分组列表
     */
    @GetMapping("/group/list")
    public Result<List<String>> groups() {
        return Result.buildSucc(
                configService.listGroups());
    }

    /**
     * 根据配置 ID 查询配置详情。
     *
     * @param configId 配置 ID
     * @return 配置详情
     */
    @GetMapping("/get")
    public Result<ConfigVO> get(
            @RequestParam("configId") Long configId) {

        return Result.buildSucc(
                configService.getConfigById(configId));
    }

    /**
     * 切换配置状态。
     *
     * @param request   HTTP 请求
     * @param configDTO 配置信息
     * @return 状态切换结果
     */
    @PostMapping("/switch")
    public Result<Void> switchConfig(
            HttpServletRequest request,
            @RequestBody ConfigDTO configDTO) {

        return configService.switchConfig(
                configDTO.getId(),
                configDTO.getStatus(),
                HttpRequestUtil.getOperator(request));
    }

    /**
     * 删除配置。
     *
     * @param request  HTTP 请求
     * @param configId 配置 ID
     * @return 删除结果
     */
    @DeleteMapping("/del")
    public Result<Void> delete(
            HttpServletRequest request,
            @RequestParam("id") Long configId) {

        return configService.delConfig(
                configId,
                HttpRequestUtil.getOperator(request));
    }

    /**
     * 新增配置。
     *
     * <p>保留原有 PUT 请求方式，避免影响现有前端调用。
     *
     * @param request   HTTP 请求
     * @param configDTO 配置信息
     * @return 新增结果及配置 ID
     */
    @PutMapping("/add")
    public Result<Long> add(
            HttpServletRequest request,
            @RequestBody ConfigDTO configDTO) {

        return configService.addConfig(
                configDTO,
                HttpRequestUtil.getOperator(request));
    }

    /**
     * 编辑配置。
     *
     * @param request   HTTP 请求
     * @param configDTO 配置信息
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Result<Void> edit(
            HttpServletRequest request,
            @RequestBody ConfigDTO configDTO) {

        return configService.editConfig(
                configDTO,
                HttpRequestUtil.getOperator(request));
    }
}
