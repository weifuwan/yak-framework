package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.dept.Dept;
import io.yak.framework.security.common.entity.dept.DeptBrief;

import java.util.List;

/**
 * 部门数据访问接口。
 */
public interface DeptDao {
    List<Dept> selectAllAndAscOrderByLevel();

    List<Long> selectIdListByLikeDeptName(String var1);

    DeptBrief selectBriefByDeptId(Long deptId);

    List<Long> selectAllDeptIdList();

    List<Long> selectIdListByParentId(Long parentId);

    void insertBatch(List<Dept> var1);

    List<DeptBrief> selectAllDeptBriefList();
}
