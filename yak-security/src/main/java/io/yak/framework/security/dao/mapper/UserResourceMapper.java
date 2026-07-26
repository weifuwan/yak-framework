package io.yak.framework.security.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.yak.framework.security.common.po.UserResourcePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserResourceMapper extends BaseMapper<UserResourcePO> {}
