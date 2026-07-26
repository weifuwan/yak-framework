package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.user.UserBriefQueryDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.entity.user.UserBrief;
import io.yak.framework.security.common.po.UserPO;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 用户数据访问实现。
 *
 * <p>负责用户信息的增删改查，并通过 MyBatis-Plus 保持既有逻辑删除语义。
 *
 * @author weifuwan
 */
@Component
@RequiredArgsConstructor
public class UserDaoImpl extends BaseDaoImpl<UserPO> implements UserDao {
  private final UserMapper userMapper;

  @Override
  public int addUser(UserPO userPO) { return userMapper.insert(userPO); }

  @Override
  public int editUser(UserPO userPO) { return userMapper.updateById(userPO); }

  @Override
  public IPage<User> selectPageByUserIdList(UserQueryDTO queryDTO, List<Long> userIdList) {
    Page<UserPO> page = Page.of(queryDTO.getPage(), queryDTO.getSize());
    LambdaQueryWrapper<UserPO> wrapper = Wrappers.<UserPO>lambdaQuery()
        .eq(queryDTO.getId() != null, UserPO::getId, queryDTO.getId())
        .like(StringUtils.hasText(queryDTO.getUserName()), UserPO::getUserName, queryDTO.getUserName())
        .like(StringUtils.hasText(queryDTO.getRealName()), UserPO::getRealName, queryDTO.getRealName())
        .in(!CollectionUtils.isEmpty(userIdList), UserPO::getId, userIdList)
        .orderByDesc(UserPO::getCreateTime);
    return CopyBeanUtil.copyPage(userMapper.selectPage(page, wrapper), User.class);
  }

  @Override
  public IPage<UserBrief> selectBriefPageByDeptIdList(UserBriefQueryDTO queryDTO,
                                                       List<Long> deptIdList) {
    Page<UserPO> page = Page.of(queryDTO.getPage(), queryDTO.getSize());
    if (deptIdList != null && deptIdList.isEmpty()) {
      return CopyBeanUtil.copyPage(page, UserBrief.class);
    }
    LambdaQueryWrapper<UserPO> wrapper = briefQuery()
        .like(StringUtils.hasText(queryDTO.getUserName()), UserPO::getUserName, queryDTO.getUserName())
        .like(StringUtils.hasText(queryDTO.getRealName()), UserPO::getRealName, queryDTO.getRealName())
        .in(deptIdList != null, UserPO::getDeptId, deptIdList);
    return CopyBeanUtil.copyPage(userMapper.selectPage(page, wrapper), UserBrief.class);
  }

  @Override
  public User selectByUserId(Long userId) {
    return userId == null ? null : CopyBeanUtil.copy(userMapper.selectById(userId), User.class);
  }

  @Override
  public User selectByUserMail(String email) {
    return CopyBeanUtil.copy(userMapper.selectOne(Wrappers.<UserPO>lambdaQuery()
        .eq(UserPO::getEmail, email)), User.class);
  }

  @Override
  public User selectByUserPhone(String phone) {
    return CopyBeanUtil.copy(userMapper.selectOne(Wrappers.<UserPO>lambdaQuery()
        .eq(UserPO::getPhone, phone)), User.class);
  }

  @Override
  public boolean deleteByUserId(Long userId) {
    return userId != null && userMapper.deleteById(userId) > 0;
  }

  @Override
  public List<UserBrief> selectBriefListByUserIdList(List<Long> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) return Collections.emptyList();
    List<UserPO> users = userMapper.selectList(briefQuery().in(UserPO::getId, userIdList));
    return CopyBeanUtil.copyList(users, UserBrief.class);
  }

  @Override
  public List<UserBrief> selectBriefListByNameAndDescOrderByCreateTime(String name) {
    LambdaQueryWrapper<UserPO> wrapper = briefQuery()
        .and(StringUtils.hasText(name), nested -> nested.like(UserPO::getUserName, name)
            .or().like(UserPO::getRealName, name))
        .orderByDesc(UserPO::getCreateTime);
    return CopyBeanUtil.copyList(userMapper.selectList(wrapper), UserBrief.class);
  }

  @Override
  public List<UserBrief> selectBriefListByDeptIdList(List<Long> deptIdList) {
    if (deptIdList != null && deptIdList.isEmpty()) return Collections.emptyList();
    return CopyBeanUtil.copyList(userMapper.selectList(briefQuery()
        .in(deptIdList != null, UserPO::getDeptId, deptIdList)), UserBrief.class);
  }

  @Override
  public List<UserBrief> selectBriefListOrderByCreateTime(boolean ascending) {
    LambdaQueryWrapper<UserPO> wrapper = briefQuery()
        .orderBy(true, ascending, UserPO::getCreateTime);
    List<UserPO> users = userMapper.selectList(wrapper);
    return CopyBeanUtil.copyList(users, UserBrief.class);
  }

  @Override
  public List<UserBrief> selectAllBriefList() {
    return CopyBeanUtil.copyList(userMapper.selectList(briefQuery()), UserBrief.class);
  }

  @Override
  public List<Long> selectUserIdListByUsernameOrRealName(String name) {
    if (!StringUtils.hasText(name)) return Collections.emptyList();
    return userMapper.selectObjs(Wrappers.<UserPO>lambdaQuery().select(UserPO::getId)
        .and(nested -> nested.like(UserPO::getUserName, name).or().like(UserPO::getRealName, name)))
        .stream().map(value -> ((Number) value).longValue()).collect(Collectors.toList());
  }

  @Override
  public User selectByUsername(String username) {
    if (!StringUtils.hasText(username)) return null;
    return CopyBeanUtil.copy(userMapper.selectOne(Wrappers.<UserPO>lambdaQuery()
        .eq(UserPO::getUserName, username)), User.class);
  }

  private LambdaQueryWrapper<UserPO> briefQuery() {
    return Wrappers.<UserPO>lambdaQuery().select(UserPO::getId, UserPO::getUserName,
        UserPO::getRealName, UserPO::getDeptId);
  }
}
