package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.entity.Oplog;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import io.yak.framework.security.dao.OplogDao;
import io.yak.framework.security.service.OplogService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.NetworkUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "yakSecurityOplogServiceImpl")
/**
 * 安全操作审计服务，持久化操作者、操作对象和扩展详情，供审计追踪使用。
 */
public class OplogServiceImpl implements OplogService {
  @Autowired private OplogDao oplogDao;

  @Override
  public PagingData<OplogVO> getOplogPage(OplogQueryDTO queryDTO) {
    IPage<Oplog> pageInfo = this.oplogDao.selectPageWithoutDetail(queryDTO);
    ArrayList<OplogVO> oplogVOList = new ArrayList<OplogVO>();
    for (Oplog oplog : pageInfo.getRecords()) {
      OplogVO oplogVO = CopyBeanUtil.copy(oplog, OplogVO.class);
      oplogVO.setCreateTime(oplog.getCreateTime());
      oplogVO.setUpdateTime(oplog.getUpdateTime());
      oplogVOList.add(oplogVO);
    }
    return new PagingData<OplogVO>(oplogVOList, pageInfo);
  }

  @Override
  public OplogVO getOplogDetailByOplogId(Integer oplogId) {
    Oplog oplog = this.oplogDao.selectByOplogId(oplogId);
    if (oplog == null) {
      return null;
    }
    OplogVO oplogVO = CopyBeanUtil.copy(oplog, OplogVO.class);
    oplogVO.setCreateTime(oplog.getCreateTime());
    oplogVO.setUpdateTime(oplog.getUpdateTime());
    return oplogVO;
  }

  @Override
  public List<String> listTargetType() {
    return this.oplogDao.listTargetType();
  }

  @Override
  public Integer saveOplog(OplogDTO oplogDTO) {
    Oplog oplog = CopyBeanUtil.copy(oplogDTO, Oplog.class);
    String realIpAddress = NetworkUtil.getRealIpAddress();
    oplog.setOperatorIp(realIpAddress);
    this.oplogDao.insert(oplog);
    return oplog.getId();
  }
}
