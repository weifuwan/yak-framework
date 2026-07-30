package io.yak.framework.security.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.yak.framework.security.common.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;

/** 租户 MyBatis 映射接口。 */
@Mapper
public interface TenantMapper extends BaseMapper<TenantPO> {
}
