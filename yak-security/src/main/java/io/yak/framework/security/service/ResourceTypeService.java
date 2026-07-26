package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import java.util.List;

/**
 * 资源类型服务接口。
 */
public interface ResourceTypeService {
  List<ResourceTypeVO> getAllResourceTypeList();

  List<Long> getAllResourceTypeIdList();

  PagingData<ResourceTypeVO>
  getResourceTypePage(ResourceTypeQueryDTO var1);

  ResourceTypeVO getResourceTypeByResourceTypeId(Long var1);

  void saveResourceType(List<String> var1);
}
