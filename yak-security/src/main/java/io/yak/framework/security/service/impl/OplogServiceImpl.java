package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.common.PagingData;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.entity.Oplog;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import io.yak.framework.security.dao.OplogDao;
import io.yak.framework.security.service.OplogService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.NetworkUtil;
import io.yak.framework.security.util.SensitiveDataSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 操作日志服务实现类。
 *
 * <p>负责保存操作者、操作目标、操作内容及客户端 IP，
 * 用于安全审计和操作追踪。
 *
 * @author weifuwan
 */
@Service("yakSecurityOplogServiceImpl")
public class OplogServiceImpl implements OplogService {

    /**
     * 非 HTTP 系统操作使用的 IP 标识。
     */
    private static final String SYSTEM_OPERATOR_IP =
            "0.0.0.0";

    private static final String DEFAULT_OPERATE_PAGE = "SYSTEM";

    private static final String DEFAULT_OPERATION_METHOD = "SERVICE";

    private final OplogDao oplogDao;

    /**
     * 创建操作日志服务。
     *
     * @param oplogDao 操作日志数据访问对象
     */
    public OplogServiceImpl(
            OplogDao oplogDao) {

        this.oplogDao = oplogDao;
    }

    /**
     * 分页查询操作日志。
     *
     * @param queryDTO 查询条件
     * @return 操作日志分页数据
     */
    @Override
    public PagingData<OplogVO> getOplogPage(
            OplogQueryDTO queryDTO) {

        if (queryDTO == null) {
            throw new IllegalArgumentException(
                    "操作日志查询条件不能为空");
        }

        IPage<Oplog> oplogPage =
                oplogDao.selectPageWithoutDetail(
                        queryDTO);

        if (oplogPage == null) {
            throw new IllegalStateException(
                    "查询操作日志分页数据失败");
        }

        List<Oplog> oplogList =
                oplogPage.getRecords();

        if (CollectionUtils.isEmpty(oplogList)) {
            return new PagingData<>(
                    new ArrayList<>(),
                    oplogPage);
        }

        List<OplogVO> oplogVOList =
                new ArrayList<>(oplogList.size());

        for (Oplog oplog : oplogList) {
            OplogVO oplogVO =
                    convertToVO(oplog);

            if (oplogVO != null) {
                oplogVOList.add(oplogVO);
            }
        }

        return new PagingData<>(
                oplogVOList,
                oplogPage);
    }

    /**
     * 根据操作日志 ID 查询日志详情。
     *
     * @param oplogId 操作日志 ID
     * @return 操作日志详情
     */
    @Override
    public OplogVO getOplogDetailByOplogId(
            Long oplogId) {

        if (oplogId == null) {
            return null;
        }

        Oplog oplog =
                oplogDao.selectByOplogId(
                        oplogId);

        return convertToVO(oplog);
    }

    /**
     * 查询全部操作目标类型。
     *
     * @return 操作目标类型列表
     */
    @Override
    public List<String> listTargetType() {
        List<String> targetTypeList =
                oplogDao.listTargetType();

        if (CollectionUtils.isEmpty(
                targetTypeList)) {

            return new ArrayList<>();
        }

        return targetTypeList.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 保存操作日志。
     *
     * @param oplogDTO 操作日志信息
     * @return 操作日志 ID
     */
    @Override
    @Transactional(
            transactionManager =
                    "yakSecurityTransactionManager",
            rollbackFor = Exception.class)
    public Long saveOplog(
            OplogDTO oplogDTO) {

        if (oplogDTO == null) {
            throw new IllegalArgumentException(
                    "操作日志信息不能为空");
        }

        Oplog oplog =
                CopyBeanUtil.copy(
                        oplogDTO,
                        Oplog.class);

        if (oplog == null) {
            throw new IllegalStateException(
                    "操作日志对象转换失败");
        }

        /*
         * 不直接修改调用方传入的 DTO，
         * 只对即将持久化的实体执行敏感数据清理。
         */
        oplog.setOperator(
                sanitize(oplogDTO.getOperator()));

        oplog.setOperatePage(
                sanitize(defaultIfBlank(
                        oplogDTO.getOperatePage(),
                        DEFAULT_OPERATE_PAGE)));

        oplog.setOperateType(
                sanitize(oplogDTO.getOperateType()));

        oplog.setTarget(
                sanitize(oplogDTO.getTarget()));

        oplog.setTargetType(
                sanitize(oplogDTO.getTargetType()));

        oplog.setDetail(
                sanitize(oplogDTO.getDetail()));

        oplog.setOperationMethods(
                sanitize(defaultIfBlank(
                        oplogDTO.getOperationMethods(),
                        DEFAULT_OPERATION_METHOD)));

        oplog.setOperatorIp(
                sanitize(
                        NetworkUtil.getRealIpAddressOrDefault(
                                SYSTEM_OPERATOR_IP)));


        oplogDao.insert(oplog);


        if (oplog.getId() == null) {
            throw new IllegalStateException(
                    "保存操作日志后未生成日志 ID");
        }

        return oplog.getId();
    }

    /**
     * 将操作日志实体转换为视图对象。
     *
     * @param oplog 操作日志实体
     * @return 操作日志视图对象
     */
    private OplogVO convertToVO(
            Oplog oplog) {

        if (oplog == null) {
            return null;
        }

        OplogVO oplogVO =
                CopyBeanUtil.copy(
                        oplog,
                        OplogVO.class);

        if (oplogVO == null) {
            throw new IllegalStateException(
                    "操作日志视图对象转换失败");
        }

        oplogVO.setCreateTime(
                oplog.getCreateTime());

        oplogVO.setUpdateTime(
                oplog.getUpdateTime());

        return oplogVO;
    }

    private static String defaultIfBlank(
            String value,
            String defaultValue) {

        return StringUtils.hasText(value)
                ? value
                : defaultValue;
    }

    /**
     * 清理可能包含敏感内容的字符串。
     *
     * @param value 原始内容
     * @return 清理后的内容
     */
    private String sanitize(String value) {
        if (value == null) {
            return null;
        }

        return SensitiveDataSanitizer.sanitize(
                value);
    }
}