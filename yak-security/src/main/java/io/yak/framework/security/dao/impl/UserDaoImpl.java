package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.user.UserBriefQueryDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.entity.user.UserBrief;
import io.yak.framework.security.common.po.UserPO;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.PWEncryptUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class UserDaoImpl extends BaseDaoImpl<UserPO> implements UserDao {
  @Autowired private UserMapper userMapper;

  @Override
  public int addUser(UserPO userPO) throws Exception {
    userPO.setPw(PWEncryptUtil.encode(userPO.getPw()));
    userPO.setAppName(this.yakSecurityProperties.getAppName());
    return this.userMapper.insert(userPO);
  }

  @Override
  public int editUser(UserPO userPO) throws Exception {
    userPO.setPw(PWEncryptUtil.encode(userPO.getPw()));
    return this.userMapper.updateById(userPO);
  }

  @Override
  public IPage<User> selectPageByUserIdList(UserQueryDTO queryDTO,
                                            List<Integer> userIdList) {
    Page page = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    ((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.eq(
                                                      queryDTO.getId() != null,
                                                      (Object) "id",
                                                      (Object)queryDTO.getId()))
                                       .like(queryDTO.getUserName() != null,
                                             (Object) "user_name",
                                             (Object)queryDTO.getUserName()))
                        .like(queryDTO.getRealName() != null,
                              (Object) "real_name",
                              (Object)queryDTO.getRealName()))
         .in(!CollectionUtils.isEmpty(userIdList), (Object) "id", userIdList))
        .orderByDesc((Object) "create_time");
    this.userMapper.selectPage((IPage)page, (Wrapper)queryWrapper);
    page.setTotal(
        (long)this.userMapper.selectCount((Wrapper)queryWrapper).intValue());
    page.setRecords(this.decodePW(page.getRecords()));
    return CopyBeanUtil.copyPage(page, User.class);
  }

  @Override
  public IPage<UserBrief>
  selectBriefPageByDeptIdList(UserBriefQueryDTO queryDTO,
                              List<Integer> deptIdList) {
    Page page = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    if (deptIdList != null && deptIdList.isEmpty()) {
      return CopyBeanUtil.copyPage(page, UserBrief.class);
    }
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    ((QueryWrapper)((QueryWrapper)queryWrapper.like(
                        !StringUtils.isEmpty((Object)queryDTO.getUserName()),
                        (Object) "user_name", (Object)queryDTO.getUserName()))
         .like(!StringUtils.isEmpty((Object)queryDTO.getRealName()),
               (Object) "real_name", (Object)queryDTO.getRealName()))
        .in(deptIdList != null, (Object) "dept_id", deptIdList);
    this.userMapper.selectPage((IPage)page, (Wrapper)queryWrapper);
    page.setRecords(this.decodePW(page.getRecords()));
    return CopyBeanUtil.copyPage(page, UserBrief.class);
  }

  @Override
  public User selectByUserId(Integer userId) {
    if (userId == null) {
      return null;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "id", (Object)userId);
    return CopyBeanUtil.copy(
        this.decodePW((UserPO)this.userMapper.selectOne((Wrapper)queryWrapper)),
        User.class);
  }

  @Override
  public User selectByUserMail(String userName) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "email", (Object)userName);
    return CopyBeanUtil.copy(
        this.decodePW((UserPO)this.userMapper.selectOne((Wrapper)queryWrapper)),
        User.class);
  }

  @Override
  public User selectByUserPhone(String userPhone) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "phone", (Object)userPhone);
    return CopyBeanUtil.copy(
        this.decodePW((UserPO)this.userMapper.selectOne((Wrapper)queryWrapper)),
        User.class);
  }

  @Override
  public boolean deleteByUserId(Integer userId) {
    if (userId == null) {
      return false;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "id", (Object)userId);
    return this.userMapper.delete((Wrapper)queryWrapper) > 0;
  }

  @Override
  public List<UserBrief> selectBriefListByUserIdList(List<Integer> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) {
      return new ArrayList<UserBrief>();
    }
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    queryWrapper.in((Object) "id", userIdList);
    return CopyBeanUtil.copyList(
        this.decodePW(this.userMapper.selectList((Wrapper)queryWrapper)),
        UserBrief.class);
  }

  @Override
  public List<UserBrief>
  selectBriefListByNameAndDescOrderByCreateTime(String name) {
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    ((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.like(
                                       !StringUtils.isEmpty((Object)name),
                                       (Object) "user_name", (Object)name))
                        .or())
         .like(!StringUtils.isEmpty((Object)name), (Object) "real_name",
               (Object)name))
        .orderByDesc((Object) "create_time");
    return CopyBeanUtil.copyList(
        this.decodePW(this.userMapper.selectList((Wrapper)queryWrapper)),
        UserBrief.class);
  }

  @Override
  public List<UserBrief> selectBriefListByDeptIdList(List<Integer> deptIdList) {
    if (deptIdList != null && deptIdList.isEmpty()) {
      return new ArrayList<UserBrief>();
    }
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    queryWrapper.in(deptIdList != null, (Object) "dept_id", deptIdList);
    return CopyBeanUtil.copyList(
        this.decodePW(this.userMapper.selectList((Wrapper)queryWrapper)),
        UserBrief.class);
  }

  @Override
  public List<UserBrief> selectBriefListOrderByCreateTime(boolean isAsc) {
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    if (isAsc) {
      queryWrapper.orderByAsc((Object) "create_time");
    } else {
      queryWrapper.orderByDesc((Object) "create_time");
    }
    this.userMapper.selectList((Wrapper)queryWrapper);
    return CopyBeanUtil.copyList(
        this.decodePW(this.userMapper.selectList((Wrapper)queryWrapper)),
        UserBrief.class);
  }

  @Override
  public List<UserBrief> selectAllBriefList() {
    QueryWrapper<UserPO> queryWrapper = this.wrapBriefQuery();
    return CopyBeanUtil.copyList(
        this.decodePW(this.userMapper.selectList((Wrapper)queryWrapper)),
        UserBrief.class);
  }

  @Override
  public List<Integer> selectUserIdListByUsernameOrRealName(String name) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    ((QueryWrapper)((QueryWrapper)queryWrapper.select(new String[] {"id"})
                        .like(!StringUtils.isEmpty((Object)name),
                              (Object) "user_name", (Object)name))
         .or())
        .like(!StringUtils.isEmpty((Object)name), (Object) "real_name",
              (Object)name);
    List userIdList = this.userMapper.selectObjs((Wrapper)queryWrapper);
    return userIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public User selectByUsername(String username) {
    if (StringUtils.isEmpty((Object)username)) {
      return null;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "user_name", (Object)username);
    UserPO userPO =
        this.decodePW((UserPO)this.userMapper.selectOne((Wrapper)queryWrapper));
    return CopyBeanUtil.copy(userPO, User.class);
  }

  private QueryWrapper<UserPO> wrapBriefQuery() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(
        new String[] {"id", "user_name", "real_name", "dept_id"});
    return queryWrapper;
  }

  private List<UserPO> decodePW(List<UserPO> userPOS) {
    if (CollectionUtils.isEmpty(userPOS)) {
      return userPOS;
    }
    return userPOS.stream()
        .map(u -> this.decodePW((UserPO)u))
        .collect(Collectors.toList());
  }

  private UserPO decodePW(UserPO userPO) {
    if (null != userPO && !StringUtils.isEmpty((Object)userPO.getPw())) {
      try {
        userPO.setPw(PWEncryptUtil.decode(userPO.getPw()));
      } catch (Exception exception) {
        // empty catch block
      }
    }
    return userPO;
  }
}
