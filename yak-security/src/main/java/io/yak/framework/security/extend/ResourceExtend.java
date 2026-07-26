package io.yak.framework.security.extend;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.ResourceDTO;
import java.util.List;

/**
 * 业务资源扩展点。安全模块通过该接口读取外部资源，供权限计算和数据隔离使用。
 */
public interface ResourceExtend {
  PagingData<ResourceDTO>
  getResourcePage(Long var1, Long var2, String var3, int var4, int var5);

  List<ResourceDTO> getResourceList(Long var1, Long var2);

  int getResourceCnt(Long var1, Long var2);
}
