package io.yak.framework.security.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.yak.framework.security.common.po.OplogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OplogMapper extends BaseMapper<OplogPO> {}
