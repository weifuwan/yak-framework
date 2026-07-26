package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.OplogExtra;
import io.yak.framework.security.common.po.OplogExtraPO;
import io.yak.framework.security.dao.OplogExtraDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.OplogExtraMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OplogExtraDaoImpl
    extends BaseDaoImpl<OplogExtraPO> implements OplogExtraDao {
  @Autowired private OplogExtraMapper oplogExtraMapper;

  @Override
  public List<OplogExtra> selectListByType(Integer type) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "type", (Object)type);
    return CopyBeanUtil.copyList(
        this.oplogExtraMapper.selectList((Wrapper)queryWrapper),
        OplogExtra.class);
  }

  @Override
  public void insertBatch(List<OplogExtra> oplogExtraList) {
    if (CollectionUtils.isEmpty(oplogExtraList)) {
      return;
    }
    List<OplogExtraPO> oplogExtraPOList =
        CopyBeanUtil.copyList(oplogExtraList, OplogExtraPO.class);
    for (OplogExtraPO oplogExtraPO : oplogExtraPOList) {
      oplogExtraPO.setAppName(this.yakSecurityProperties.getApplicationName());
      this.oplogExtraMapper.insert(oplogExtraPO);
    }
  }
}
