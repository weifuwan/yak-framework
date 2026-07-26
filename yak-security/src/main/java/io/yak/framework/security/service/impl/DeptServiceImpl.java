package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.dept.DeptDTO;
import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.dept.DeptBriefVO;
import io.yak.framework.security.common.vo.dept.DeptTreeVO;
import io.yak.framework.security.dao.DeptDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.DeptService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.MathUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service(value = "yakSecurityDeptServiceImpl")
public class DeptServiceImpl implements DeptService {
  @Autowired private DeptDao deptDao;

  @Override
  public DeptTreeVO buildDeptTree() {
    List<Dept> deptList = this.deptDao.selectAllAndAscOrderByLevel();
    DeptTreeVO root = DeptTreeVO.builder()
                          .leaf(false)
                          .id(0)
                          .childList(new ArrayList<DeptTreeVO>())
                          .build();
    HashMap<Integer, DeptTreeVO> parentMap = new HashMap<Integer, DeptTreeVO>();
    parentMap.put(0, root);
    for (Dept dept : deptList) {
      DeptTreeVO parent;
      DeptTreeVO deptTreeVO = CopyBeanUtil.copy(dept, DeptTreeVO.class);
      if (deptTreeVO.getLeaf() != null &&
          !deptTreeVO.getLeaf().booleanValue()) {
        deptTreeVO.setChildList(new ArrayList<DeptTreeVO>());
      }
      if ((parent = (DeptTreeVO)parentMap.get(dept.getParentId())) == null) {
        throw new YakSecurityException(ResultCode.DEPT_DATA_ERROR);
      }
      parent.getChildList().add(deptTreeVO);
      parentMap.put(deptTreeVO.getId(), deptTreeVO);
    }
    return root;
  }

  @Override
  public List<DeptBriefVO> getDeptBriefListByChildId(Long deptId) {
    Map<Integer, Dept> deptMap = this.getAllDeptMap();
    return this.getDeptBriefListFromDeptMapByChildId(deptMap, deptId);
  }

  @Override
  public List<Long> getDeptIdListByParentId(Long deptId) {
    if (deptId == null) {
      return this.deptDao.selectAllDeptIdList();
    }
    List<Dept> deptList = this.deptDao.selectAllAndAscOrderByLevel();
    HashSet<Long> deptIdSet = new HashSet<Long>();
    deptIdSet.add(deptId);
    for (Dept dept : deptList) {
      if (!deptIdSet.contains(dept.getParentId()))
        continue;
      deptIdSet.add(dept.getId());
    }
    return new ArrayList<Long>(deptIdSet);
  }

  @Override
  public List<Long> getDeptIdListByParentIdAndDeptName(Long deptId,
                                                          String deptName) {
    List<Long> deptIdList = this.getDeptIdListByParentId(deptId);
    if (deptIdList == null) {
      return new ArrayList<Long>();
    }
    if (deptIdList.isEmpty() || StringUtils.isEmpty((Object)deptName)) {
      return deptIdList;
    }
    HashSet<Long> deptIdSet = new HashSet<Long>(deptIdList);
    ArrayList<Long> result = new ArrayList<Long>();
    deptIdList = this.deptDao.selectIdListByLikeDeptName(deptName);
    for (Long id : deptIdList) {
      if (!deptIdSet.contains(id))
        continue;
      result.add(id);
    }
    return result;
  }

  @Override
  public Map<Integer, Dept> getAllDeptMap() {
    List<Dept> deptList = this.deptDao.selectAllAndAscOrderByLevel();
    HashMap<Integer, Dept> map = new HashMap<Integer, Dept>();
    for (Dept dept : deptList) {
      map.put(dept.getId(), dept);
    }
    return map;
  }

  @Override
  public List<DeptBriefVO>
  getDeptBriefListFromDeptMapByChildId(Map<Integer, Dept> deptMap,
                                       Long deptId) {
    Dept dept;
    if (deptId == null || deptId == 0 || CollectionUtils.isEmpty(deptMap)) {
      return new ArrayList<DeptBriefVO>();
    }
    LinkedList<DeptBriefVO> deptBriefVOList = new LinkedList<DeptBriefVO>();
    while (deptId != null && deptId != 0 &&
           (dept = deptMap.get(deptId)) != null) {
      deptBriefVOList.addFirst(CopyBeanUtil.copy(dept, DeptBriefVO.class));
      deptId = dept.getParentId();
    }
    return deptBriefVOList;
  }

  @Override
  public void saveDept(List<DeptDTO> deptDTOList) {
    if (CollectionUtils.isEmpty(deptDTOList)) {
      return;
    }
    ArrayList<Dept> deptList = new ArrayList<Dept>();
    HashMap<DeptDTO, Integer> deptDTOMap = new HashMap<DeptDTO, Integer>();
    LinkedList<DeptDTO> queue = new LinkedList<DeptDTO>();
    DeptDTO deptDTO = new DeptDTO();
    deptDTO.setChildDeptDTOList(deptDTOList);
    queue.offer(deptDTO);
    int level = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        DeptDTO dto = (DeptDTO)queue.poll();
        if (dto == null)
          continue;
        Dept dept = CopyBeanUtil.copy(dto, Dept.class);
        dept.setLevel(level);
        dept.setParentId((Integer)deptDTOMap.get(dto));
        dept.setLeaf(CollectionUtils.isEmpty(dto.getChildDeptDTOList()));
        dept.setId(0);
        if (level > 0) {
          dept.setId((int)this.getDeptId());
          deptList.add(dept);
        }
        for (DeptDTO child : dto.getChildDeptDTOList()) {
          deptDTOMap.put(child, dept.getId());
          queue.offer(child);
        }
      }
      ++level;
    }
    this.deptDao.insertBatch(deptList);
  }

  @Override
  public List<DeptBrief> listAllDeptBrief() {
    return this.deptDao.selectAllDeptBriefList();
  }

  private long getDeptId() {
    return System.currentTimeMillis() % 1000L * (long)Math.pow(10.0, 5.0) +
        MathUtil.getRandomNumber(5);
  }
}
