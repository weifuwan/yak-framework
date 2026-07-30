package io.yak.framework.security.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.yak.framework.security.common.po.UserTenantPO;
import org.apache.ibatis.annotations.Mapper;

/** 用户租户成员关系 MyBatis 映射接口。 */
@Mapper
public interface UserTenantMapper extends BaseMapper<UserTenantPO> {
}
