package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.po.ConfigPO;
import io.yak.framework.security.dao.ConfigDao;
import io.yak.framework.security.dao.mapper.ConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统配置数据访问实现。
 */
@Repository
@RequiredArgsConstructor
public class ConfigDaoImpl
        implements ConfigDao {

    private final ConfigMapper configMapper;

    /**
     * 新增配置。
     *
     * @param config 配置信息
     * @return 受影响行数
     */
    @Override
    public int insert(ConfigPO config) {
        return configMapper.insert(config);
    }

    /**
     * 根据主键更新配置。
     *
     * @param config 配置信息
     * @return 受影响行数
     */
    @Override
    public int updateById(ConfigPO config) {
        return configMapper.updateById(config);
    }

    /**
     * 根据配置分组和配置名称更新配置。
     *
     * @param config 配置信息
     * @return 受影响行数
     */
    @Override
    public int update(ConfigPO config) {
        LambdaUpdateWrapper<ConfigPO> wrapper =
                Wrappers.<ConfigPO>lambdaUpdate()
                        .eq(
                                ConfigPO::getValueGroup,
                                config.getValueGroup()
                        )
                        .eq(
                                ConfigPO::getValueName,
                                config.getValueName()
                        );

        return configMapper.update(config, wrapper);
    }

    /**
     * 根据主键删除配置。
     *
     * @param id 配置主键
     * @return 受影响行数
     */
    @Override
    public int deleteById(Long id) {
        return configMapper.deleteById(id);
    }

    /**
     * 分页查询配置。
     *
     * @param queryDTO 查询条件
     * @return 配置分页数据
     */
    @Override
    public IPage<ConfigPO> selectPage(
            ConfigQueryDTO queryDTO) {

        Page<ConfigPO> page = Page.of(
                queryDTO.getPage(),
                queryDTO.getSize()
        );

        LambdaQueryWrapper<ConfigPO> wrapper =
                Wrappers.<ConfigPO>lambdaQuery()
                        .eq(
                                StringUtils.hasText(
                                        queryDTO.getValueGroup()
                                ),
                                ConfigPO::getValueGroup,
                                queryDTO.getValueGroup()
                        )
                        .eq(
                                queryDTO.getStatus() != null,
                                ConfigPO::getStatus,
                                queryDTO.getStatus()
                        )
                        .eq(
                                StringUtils.hasText(
                                        queryDTO.getOperator()
                                ),
                                ConfigPO::getOperator,
                                queryDTO.getOperator()
                        )
                        .like(
                                StringUtils.hasText(
                                        queryDTO.getMemo()
                                ),
                                ConfigPO::getMemo,
                                queryDTO.getMemo()
                        )
                        .like(
                                StringUtils.hasText(
                                        queryDTO.getValueName()
                                ),
                                ConfigPO::getValueName,
                                queryDTO.getValueName()
                        )
                        .orderByDesc(ConfigPO::getCreateTime);

        return configMapper.selectPage(page, wrapper);
    }

    /**
     * 根据配置对象中的非空条件查询配置。
     *
     * @param condition 查询条件
     * @return 配置列表
     */
    @Override
    public List<ConfigPO> listByCondition(
            ConfigPO condition) {

        LambdaQueryWrapper<ConfigPO> wrapper =
                Wrappers.<ConfigPO>lambdaQuery()
                        .eq(
                                StringUtils.hasText(
                                        condition.getValueGroup()
                                ),
                                ConfigPO::getValueGroup,
                                condition.getValueGroup()
                        )
                        .eq(
                                StringUtils.hasText(
                                        condition.getValueName()
                                ),
                                ConfigPO::getValueName,
                                condition.getValueName()
                        )
                        .eq(
                                condition.getStatus() != null,
                                ConfigPO::getStatus,
                                condition.getStatus()
                        );

        return configMapper.selectList(wrapper);
    }

    /**
     * 根据配置分组查询配置列表。
     *
     * @param groupName 配置分组
     * @return 配置列表
     */
    @Override
    public List<ConfigPO> listConfigByGroup(
            String groupName) {

        return configMapper.selectList(
                Wrappers.<ConfigPO>lambdaQuery()
                        .eq(
                                ConfigPO::getValueGroup,
                                groupName
                        )
        );
    }

    /**
     * 查询所有不重复的配置分组。
     *
     * @return 配置分组列表
     */
    @Override
    public List<String> listDistinctGroup() {
        List<ConfigPO> configs =
                configMapper.selectList(
                        Wrappers.<ConfigPO>lambdaQuery()
                                .select(
                                        ConfigPO::getValueGroup
                                )
                                .isNotNull(
                                        ConfigPO::getValueGroup
                                )
                                .groupBy(
                                        ConfigPO::getValueGroup
                                )
                                .orderByAsc(
                                        ConfigPO::getValueGroup
                                )
                );

        return configs.stream()
                .map(ConfigPO::getValueGroup)
                .filter(StringUtils::hasText)
                .toList();
    }

    /**
     * 根据主键查询配置。
     *
     * @param configId 配置主键
     * @return 配置信息
     */
    @Override
    public ConfigPO getbyId(Long configId) {
        return configMapper.selectById(configId);
    }

    /**
     * 根据配置分组和配置名称查询配置。
     *
     * @param valueGroup 配置分组
     * @param valueName  配置名称
     * @return 配置信息
     */
    @Override
    public ConfigPO getByGroupAndName(
            String valueGroup,
            String valueName) {

        return configMapper.selectOne(
                Wrappers.<ConfigPO>lambdaQuery()
                        .eq(
                                ConfigPO::getValueGroup,
                                valueGroup
                        )
                        .eq(
                                ConfigPO::getValueName,
                                valueName
                        )
        );
    }
}