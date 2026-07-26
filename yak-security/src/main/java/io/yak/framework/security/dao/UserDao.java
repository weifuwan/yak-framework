package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.user.UserBriefQueryDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.entity.user.UserBrief;
import io.yak.framework.security.common.po.UserPO;
import java.util.List;

public interface UserDao {
  public int addUser(UserPO userPO);

  public int editUser(UserPO userPO);

  public IPage<User> selectPageByUserIdList(UserQueryDTO var1,
                                            List<Long> var2);

  public IPage<UserBrief> selectBriefPageByDeptIdList(UserBriefQueryDTO var1,
                                                      List<Long> var2);

  public User selectByUserId(Long userId);

  public User selectByUserMail(String var1);

  public User selectByUserPhone(String var1);

  public boolean deleteByUserId(Long userId);

  public List<UserBrief> selectBriefListByUserIdList(List<Long> var1);

  public List<UserBrief>
  selectBriefListByNameAndDescOrderByCreateTime(String var1);

  public List<UserBrief> selectBriefListByDeptIdList(List<Long> var1);

  public List<UserBrief> selectBriefListOrderByCreateTime(boolean var1);

  public List<UserBrief> selectAllBriefList();

  public List<Long> selectUserIdListByUsernameOrRealName(String var1);

  public User selectByUsername(String var1);
}
