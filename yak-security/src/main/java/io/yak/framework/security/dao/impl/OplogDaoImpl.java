package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.entity.Oplog;
import io.yak.framework.security.common.po.OplogPO;
import io.yak.framework.security.dao.OplogDao;
import io.yak.framework.security.dao.mapper.OplogMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * 操作日志数据访问实现。
 */
@Repository
@RequiredArgsConstructor
public class OplogDaoImpl
        implements OplogDao {

    private final OplogMapper oplogMapper;

    /**
     * 分页查询操作日志，不返回操作详情。
     *
     * @param queryDTO 查询条件
     * @return 操作日志分页数据
     */
    @Override
    public IPage<Oplog> selectPageWithoutDetail(
            OplogQueryDTO queryDTO) {

        Page<OplogPO> page = Page.of(
                queryDTO.getPage(),
                queryDTO.getSize()
        );

        LambdaQueryWrapper<OplogPO> wrapper = Wrappers.<OplogPO>lambdaQuery()
                .select(
                        OplogPO::getId,
                        OplogPO::getOperateType,
                        OplogPO::getOperationMethods,
                        OplogPO::getTarget,
                        OplogPO::getTargetType,
                        OplogPO::getOperatorIp,
                        OplogPO::getOperator,
                        OplogPO::getCreateTime,
                        OplogPO::getUpdateTime
                )
                .eq(
                        queryDTO.getOperateType() != null,
                        OplogPO::getOperateType,
                        queryDTO.getOperateType()
                )
                .eq(
                        queryDTO.getTargetType() != null,
                        OplogPO::getTargetType,
                        queryDTO.getTargetType()
                )
                .eq(
                        StringUtils.hasText(
                                queryDTO.getOperationMethods()
                        ),
                        OplogPO::getOperationMethods,
                        queryDTO.getOperationMethods()
                )
                .like(
                        StringUtils.hasText(queryDTO.getDetail()),
                        OplogPO::getDetail,
                        queryDTO.getDetail()
                )
                .like(
                        StringUtils.hasText(queryDTO.getTarget()),
                        OplogPO::getTarget,
                        queryDTO.getTarget()
                )
                .like(
                        StringUtils.hasText(queryDTO.getOperator()),
                        OplogPO::getOperator,
                        queryDTO.getOperator()
                )
                .ge(
                        queryDTO.getStartTime() != null,
                        OplogPO::getCreateTime,
                        toTimestamp(queryDTO.getStartTime())
                )
                .le(
                        queryDTO.getEndTime() != null,
                        OplogPO::getCreateTime,
                        toTimestamp(queryDTO.getEndTime())
                )
                .orderByDesc(OplogPO::getUpdateTime);

        IPage<OplogPO> result =
                oplogMapper.selectPage(page, wrapper);

        return CopyBeanUtil.copyPage(result, Oplog.class);
    }

    /**
     * 根据操作日志主键查询完整日志。
     *
     * @param oplogId 操作日志主键
     * @return 操作日志；主键为空或数据不存在时返回 {@code null}
     */
    @Override
    public Oplog selectByOplogId(Long oplogId) {
        if (oplogId == null) {
            return null;
        }

        return CopyBeanUtil.copy(
                oplogMapper.selectById(oplogId),
                Oplog.class
        );
    }

    /**
     * 新增操作日志，并回填日志主键。
     *
     * @param oplog 操作日志
     */
    @Override
    public void insert(Oplog oplog) {
        OplogPO oplogPO =
                CopyBeanUtil.copy(oplog, OplogPO.class);

        oplogMapper.insert(oplogPO);

        oplog.setId(oplogPO.getId());
    }

    /**
     * 查询所有操作目标类型。
     *
     * @return 不重复的操作目标类型列表
     */
    @Override
    public List<String> listTargetType() {
        return oplogMapper.selectList(
                Wrappers.<OplogPO>lambdaQuery()
                        .select(OplogPO::getTargetType)
                        .isNotNull(OplogPO::getTargetType)
                        .groupBy(OplogPO::getTargetType)
                        .orderByAsc(OplogPO::getTargetType)
        )
                .stream()
                .map(OplogPO::getTargetType)
                .filter(StringUtils::hasText)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 将毫秒时间戳转换为数据库时间类型。
     */
    private static Timestamp toTimestamp(Long timeMillis) {
        return timeMillis == null
                ? null
                : new Timestamp(timeMillis);
    }
}
