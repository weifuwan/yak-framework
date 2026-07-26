package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.entity.ResourceType;
import io.yak.framework.security.common.po.ResourceTypePO;
import io.yak.framework.security.dao.ResourceTypeDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.ResourceTypeMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class ResourceTypeDaoImpl
    extends BaseDaoImpl<ResourceTypePO> implements ResourceTypeDao {
  @Autowired private ResourceTypeMapper resourceTypeMapper;

  @Override
  public List<ResourceType> selectAll() {
    return CopyBeanUtil.copyList(this.resourceTypeMapper.selectList(null),
                                 ResourceType.class);
  }

  @Override
  public IPage<ResourceType> selectPage(ResourceTypeQueryDTO queryDTO) {
    Page pageInfo =
        new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    String typeName = queryDTO.getTypeName();
    queryWrapper.like(!StringUtils.isEmpty((Object)typeName),
                      (Object) "type_name", (Object)typeName);
    this.resourceTypeMapper.selectPage((IPage)pageInfo, (Wrapper)queryWrapper);
    return CopyBeanUtil.copyPage(pageInfo, ResourceType.class);
  }

  @Override
  public ResourceType selectByResourceTypeId(Integer resourceTypeId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "id", (Object)resourceTypeId);
    return CopyBeanUtil.copy(
        this.resourceTypeMapper.selectOne((Wrapper)queryWrapper),
        ResourceType.class);
  }

  @Override
  public void insertBatch(List<ResourceType> resourceTypeList) {
    if (CollectionUtils.isEmpty(resourceTypeList)) {
      return;
    }
    List<ResourceTypePO> resourceTypePOList =
        CopyBeanUtil.copyList(resourceTypeList, ResourceTypePO.class);
    for (ResourceTypePO resourceTypePO : resourceTypePOList) {
      resourceTypePO.setAppName(this.yakSecurityProperties.getAppName());
      this.resourceTypeMapper.insert(resourceTypePO);
    }
  }
}
