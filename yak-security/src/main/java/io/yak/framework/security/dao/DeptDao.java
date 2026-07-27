package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;

import java.util.List;

/**
 * 部门数据访问接口。
 */
public interface DeptDao {

    List<Dept> selectAllAndAscOrderByLevel();

    List<Long> selectIdListByLikeDeptName(String deptName);

    DeptBrief selectBriefByDeptId(Long deptId);

    Dept selectByDeptId(Long deptId);

    List<Long> selectAllDeptIdList();

    List<Long> selectIdListByParentId(Long parentId);

    boolean existsByParentIdAndDeptName(
            Long parentId,
            String deptName,
            Long excludeDeptId);

    int insert(Dept dept);

    int update(Dept dept);

    int deleteById(Long deptId);

    void insertBatch(List<Dept> deptList);

    List<DeptBrief> selectAllDeptBriefList();
}
