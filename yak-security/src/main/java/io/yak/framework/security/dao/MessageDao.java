package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.Message;
import java.util.List;

public interface MessageDao {
  public void insert(Message var1);

  public void update(Message var1);

  public void insertBatch(List<Message> var1);

  public List<Message> selectListByUserIdAndReadTag(Integer var1, Boolean var2);

  public List<Message> selectListByMessageIdList(List<Integer> var1);
}
