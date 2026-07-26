package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.Message;
import java.util.List;

/**
 * 消息数据访问接口。
 */
public interface MessageDao {
  void insert(Message var1);

  void update(Message var1);

  void insertBatch(List<Message> var1);

  List<Message> selectListByUserIdAndReadTag(Long userId, Boolean readTag);

  List<Message> selectListByMessageIdList(List<Long> var1);
}
