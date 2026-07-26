package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import java.util.List;

public interface ResourceTypeService {
  public List<ResourceTypeVO> getAllResourceTypeList();

  public List<Integer> getAllResourceTypeIdList();

  public PagingData<ResourceTypeVO>
  getResourceTypePage(ResourceTypeQueryDTO var1);

  public ResourceTypeVO getResourceTypeByResourceTypeId(Integer var1);

  public void saveResourceType(List<String> var1);
}
