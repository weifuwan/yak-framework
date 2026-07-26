package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.ResourceDTO;
import io.yak.framework.security.extend.ResourceExtend;
import java.util.ArrayList;
import java.util.List;

public class DefaultResourceExtendImpl implements ResourceExtend {
  @Override
  public PagingData<ResourceDTO>
  getResourcePage(Long projectId, Long resourceTypeId,
                  String resourceName, int page, int size) {
    return null;
  }

  @Override
  public List<ResourceDTO> getResourceList(Long projectId,
                                           Long resourceTypeId) {
    return new ArrayList<ResourceDTO>();
  }

  @Override
  public int getResourceCnt(Long projectId, Long resourceTypeId) {
    return 0;
  }
}
