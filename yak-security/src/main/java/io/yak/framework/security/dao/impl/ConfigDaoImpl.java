package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.po.ConfigPO;
import io.yak.framework.security.dao.ConfigDao;
import io.yak.framework.security.dao.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigDaoImpl extends BaseDaoImpl<ConfigPO> implements ConfigDao {
    private static final String ID = "id";
    private static final String VALUE_GROUP = "value_group";
    private static final String VALUE_NAME = "value_name";
    private static final String VALUE = "value";
    private static final String STATUS = "status";
    private static final String MEMO = "memo";
    private static final String OPERATOR = "operator";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public int insert(ConfigPO param) {
        return this.configMapper.insert(param);
    }

    @Override
    public int updateById(ConfigPO param) {
        return this.configMapper.updateById(param);
    }

    @Override
    public int update(ConfigPO param) {
        QueryWrapper<ConfigPO> queryWrapper = this.wrapBriefQuery();
        queryWrapper.eq((Object) VALUE_GROUP, (Object) param.getValueGroup());
        queryWrapper.eq((Object) VALUE_NAME, (Object) param.getValueName());
        return this.configMapper.update(param, (Wrapper) queryWrapper);
    }

    @Override
    public int deleteById(Long id) {
        return this.configMapper.deleteById(id);
    }

    @Override
    public IPage<ConfigPO> selectPage(ConfigQueryDTO queryDTO) {
        Page page = new Page((long) queryDTO.getPage(), (long) queryDTO.getSize());
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) ((QueryWrapper) queryWrapper
                .eq(queryDTO.getValueGroup() !=
                                null,
                        (Object)
                                VALUE_GROUP,
                        (Object) queryDTO
                                .getValueGroup()))
                .eq(queryDTO.getStatus() !=
                                null,
                        (Object) STATUS,
                        (Object) queryDTO
                                .getStatus()))
                .eq(queryDTO.getOperator() != null,
                        (Object) OPERATOR,
                        (Object) queryDTO.getOperator()))
                .like(queryDTO.getMemo() != null, (Object) MEMO,
                        (Object) queryDTO.getMemo()))
                .like(queryDTO.getValueName() != null, (Object) VALUE_NAME,
                        (Object) queryDTO.getValueName()))
                .orderByDesc((Object) CREATE_TIME);
        this.configMapper.selectPage((IPage) page, (Wrapper) queryWrapper);
        page.setTotal(
                this.configMapper.selectCount((Wrapper) queryWrapper));
        return page;
    }

    @Override
    public List<ConfigPO> listByCondition(ConfigPO condt) {
        QueryWrapper<ConfigPO> queryWrapper = this.wrapBriefQuery();
        if (!StringUtils.isBlank((CharSequence) condt.getValueGroup())) {
            queryWrapper.eq((Object) VALUE_GROUP, (Object) condt.getValueGroup());
        }
        if (!StringUtils.isBlank((CharSequence) condt.getValueName())) {
            queryWrapper.eq((Object) VALUE_NAME, (Object) condt.getValueName());
        }
        if (null != condt.getStatus()) {
            queryWrapper.eq((Object) STATUS, (Object) condt.getStatus());
        }
        return this.configMapper.selectList((Wrapper) queryWrapper);
    }

    @Override
    public List<ConfigPO> listConfigByGroup(String groupName) {
        QueryWrapper<ConfigPO> queryWrapper = this.wrapBriefQuery();
        queryWrapper.eq((Object) VALUE_GROUP, (Object) groupName);
        return this.configMapper.selectList((Wrapper) queryWrapper);
    }

    @Override
    public List<String> listDistinctGroup() {
        QueryWrapper<ConfigPO> queryWrapper = this.wrapBriefQuery();
        queryWrapper.select(new String[]{"distinct value_group"});
        List configPOS = this.configMapper.selectList((Wrapper) queryWrapper);
        if (!CollectionUtils.isEmpty((Collection) configPOS)) {
            return configPOS.stream()
                    .map(ConfigPO::getValueGroup)
                    .collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }

    @Override
    public ConfigPO getbyId(Long configId) {
        return (ConfigPO) this.configMapper.selectById(configId);
    }

    @Override
    public ConfigPO getByGroupAndName(String valueGroup, String valueName) {
        QueryWrapper<ConfigPO> queryWrapper = this.wrapBriefQuery();
        queryWrapper.eq((Object) VALUE_GROUP, (Object) valueGroup);
        queryWrapper.eq((Object) VALUE_NAME, (Object) valueName);
        return (ConfigPO) this.configMapper.selectOne((Wrapper) queryWrapper);
    }

    private QueryWrapper<ConfigPO> wrapBriefQuery() {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.select(new String[]{ID, VALUE_GROUP, VALUE_NAME, VALUE,
                STATUS, MEMO, OPERATOR, CREATE_TIME,
                UPDATE_TIME});
        return queryWrapper;
    }
}
