package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.ResourceDTO;
import io.yak.framework.security.extend.ResourceExtend;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component(value = "yakSecurityDefaultResourceExtendImpl")
public class DefaultResourceExtendImpl implements ResourceExtend {
  @Override
  public PagingData<ResourceDTO>
  getResourcePage(Integer projectId, Integer resourceTypeId,
                  String resourceName, int page, int size) {
    return null;
  }

  @Override
  public List<ResourceDTO> getResourceList(Integer projectId,
                                           Integer resourceTypeId) {
    return new ArrayList<ResourceDTO>();
  }

  @Override
  public int getResourceCnt(Integer projectId, Integer resourceTypeId) {
    return 0;
  }
}
