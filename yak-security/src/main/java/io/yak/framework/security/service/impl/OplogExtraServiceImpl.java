package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.OplogExtra;
import io.yak.framework.security.common.enums.oplog.OplogCode;
import io.yak.framework.security.dao.OplogExtraDao;
import io.yak.framework.security.service.OplogExtraService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityOplogExtraServiceImpl")
public class OplogExtraServiceImpl implements OplogExtraService {
  @Autowired private OplogExtraDao oplogExtraDao;

  @Override
  public void saveOplogExtraList(List<String> nameList, OplogCode oplogCode) {
    if (oplogCode == null || CollectionUtils.isEmpty(nameList)) {
      return;
    }
    ArrayList<OplogExtra> oplogExtraList = new ArrayList<OplogExtra>();
    for (String name : nameList) {
      OplogExtra oplogExtra = new OplogExtra();
      oplogExtra.setInfo(name);
      oplogExtra.setType(oplogCode.getType());
    }
    this.oplogExtraDao.insertBatch(oplogExtraList);
  }

  @Override
  public List<String> getOplogExtraNameListByType(Integer type) {
    List<OplogExtra> oplogExtraList = this.oplogExtraDao.selectListByType(type);
    ArrayList<String> result = new ArrayList<String>();
    for (OplogExtra oplogExtra : oplogExtraList) {
      result.add(oplogExtra.getInfo());
    }
    return result;
  }
}
