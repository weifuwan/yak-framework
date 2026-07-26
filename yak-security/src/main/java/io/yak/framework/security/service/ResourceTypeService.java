package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import java.util.List;

/**
 * 资源类型服务接口。
 */
public interface ResourceTypeService {
  /**
   * 查询全部资源类型。
   */
  List<ResourceTypeVO> getAllResourceTypeList();

  /**
   * 查询全部资源类型 ID。
   */
  List<Long> getAllResourceTypeIdList();

  /**
   * 分页查询资源类型。
   */
  PagingData<ResourceTypeVO>
  getResourceTypePage(ResourceTypeQueryDTO var1);

  /**
   * 根据资源类型 ID 查询资源类型。
   */
  ResourceTypeVO getResourceTypeByResourceTypeId(Long var1);

  /**
   * 保存资源类型。
   */
  void saveResourceType(List<String> var1);
}
