package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import io.yak.framework.security.common.po.DeptPO;
import io.yak.framework.security.dao.DeptDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.DeptMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class DeptDaoImpl extends BaseDaoImpl<DeptPO> implements DeptDao {
  @Autowired private DeptMapper deptMapper;

  private QueryWrapper<DeptPO> wrapBriefQuery() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(
        new String[] {"id", "dept_name", "leaf", "level", "parent_id"});
    return queryWrapper;
  }

  @Override
  public List<Dept> selectAllAndAscOrderByLevel() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.orderByAsc((Object) "level");
    return CopyBeanUtil.copyList(
        this.deptMapper.selectList((Wrapper)queryWrapper), Dept.class);
  }

  @Override
  public List<Integer> selectIdListByLikeDeptName(String deptName) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"id"})
        .like(!StringUtils.isEmpty((Object)deptName), (Object) "dept_name",
              (Object)deptName);
    List deptIdList = this.deptMapper.selectObjs((Wrapper)queryWrapper);
    return deptIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public DeptBrief selectBriefByDeptId(Integer deptId) {
    QueryWrapper<DeptPO> queryWrapper = this.wrapBriefQuery();
    queryWrapper.eq((Object) "id", (Object)deptId);
    return CopyBeanUtil.copy(this.deptMapper.selectOne((Wrapper)queryWrapper),
                             DeptBrief.class);
  }

  @Override
  public List<Integer> selectAllDeptIdList() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"id"});
    List deptIdList = this.deptMapper.selectObjs((Wrapper)queryWrapper);
    return deptIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public List<Integer> selectIdListByParentId(Integer deptId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"id"})
        .eq((Object) "parent_id", (Object)deptId);
    List deptIdList = this.deptMapper.selectObjs((Wrapper)queryWrapper);
    return deptIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public void insertBatch(List<Dept> deptList) {
    if (CollectionUtils.isEmpty(deptList)) {
      return;
    }
    List<DeptPO> deptPOList = CopyBeanUtil.copyList(deptList, DeptPO.class);
    for (DeptPO deptPO : deptPOList) {
      deptPO.setAppName(this.yakSecurityProperties.getAppName());
      this.deptMapper.insert(deptPO);
    }
  }

  @Override
  public List<DeptBrief> selectAllDeptBriefList() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(
        new String[] {"id", "description", "parent_id", "leaf", "level"});
    return CopyBeanUtil.copyList(
        this.deptMapper.selectList((Wrapper)queryWrapper), DeptBrief.class);
  }
}
