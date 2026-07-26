package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.po.PermissionPO;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.dao.mapper.PermissionMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限数据访问实现。
 *
 * @author weifuwan
 */
@Repository
@RequiredArgsConstructor
public class PermissionDaoImpl
        implements PermissionDao {

    private final PermissionMapper permissionMapper;

    /**
     * 查询全部权限，并按照权限层级升序排列。
     *
     * @return 权限列表
     */
    @Override
    public List<Permission> selectAllAndAscOrderByLevel() {
        List<PermissionPO> permissionPOList =
                permissionMapper.selectList(
                        Wrappers.<PermissionPO>lambdaQuery()
                                .orderByAsc(PermissionPO::getLevel)
                                .orderByAsc(PermissionPO::getId)
                );

        return CopyBeanUtil.copyList(
                permissionPOList,
                Permission.class
        );
    }

    /**
     * 批量新增权限。
     *
     * <p>当前采用循环插入方式，适用于权限数量较少的初始化场景。</p>
     *
     * @param permissionList 权限列表
     */
    @Override
    public void insertBatch(List<Permission> permissionList) {
        if (permissionList == null || permissionList.isEmpty()) {
            return;
        }

        CopyBeanUtil.copyList(
                permissionList,
                PermissionPO.class
        )
                .forEach(permissionMapper::insert);
    }
}