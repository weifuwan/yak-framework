package com.yak.security.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yak.security.domain.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface UserMapper extends BaseMapper<User> {
  User findByUsername(@Param("username") String username);
  List<String> findRoleCodes(@Param("userId") Long userId);
  List<String> findPermissionCodes(@Param("userId") Long userId);
}
