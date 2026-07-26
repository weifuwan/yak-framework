package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.vo.oplog.OplogVO;

import java.util.List;

/**
 * 操作日志服务接口。
 *
 * @author weifuwan
 */
public interface OplogService {

  /**
   * 保存操作日志。
   *
   * @param oplogDTO 操作日志信息
   * @return 操作日志 ID
   */
  Long saveOplog(
          OplogDTO oplogDTO);

  /**
   * 分页查询操作日志。
   *
   * @param queryDTO 查询条件
   * @return 操作日志分页数据
   */
  PagingData<OplogVO> getOplogPage(
          OplogQueryDTO queryDTO);

  /**
   * 根据操作日志 ID 查询日志详情。
   *
   * @param oplogId 操作日志 ID
   * @return 操作日志详情
   */
  OplogVO getOplogDetailByOplogId(
          Long oplogId);

  /**
   * 查询全部操作目标类型。
   *
   * @return 操作目标类型列表
   */
  List<String> listTargetType();
}