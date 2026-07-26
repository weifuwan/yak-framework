package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.entity.ResourceType;
import java.util.List;

public interface ResourceTypeDao {
  public List<ResourceType> selectAll();

  public IPage<ResourceType> selectPage(ResourceTypeQueryDTO var1);

  public ResourceType selectByResourceTypeId(Long resourceTypeId);

  public void insertBatch(List<ResourceType> var1);
}
