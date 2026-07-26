package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.type.ResourceTypeQueryDTO;
import io.yak.framework.security.common.entity.ResourceType;
import io.yak.framework.security.common.vo.resource.ResourceTypeVO;
import io.yak.framework.security.dao.ResourceTypeDao;
import io.yak.framework.security.service.ResourceTypeService;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "yakSecurityResourceTypeServiceImpl")
public class ResourceTypeServiceImpl implements ResourceTypeService {
  @Autowired private ResourceTypeDao resourceTypeDao;

  @Override
  public List<ResourceTypeVO> getAllResourceTypeList() {
    List<ResourceType> resourceTypeList = this.resourceTypeDao.selectAll();
    return CopyBeanUtil.copyList(resourceTypeList, ResourceTypeVO.class);
  }

  @Override
  public List<Long> getAllResourceTypeIdList() {
    List<ResourceType> resourceTypeList = this.resourceTypeDao.selectAll();
    ArrayList<Long> result = new ArrayList<Long>();
    for (ResourceType resourceType : resourceTypeList) {
      result.add(resourceType.getId());
    }
    return result;
  }

  @Override
  public PagingData<ResourceTypeVO>
  getResourceTypePage(ResourceTypeQueryDTO queryDTO) {
    IPage<ResourceType> pageInfo = this.resourceTypeDao.selectPage(queryDTO);
    List<ResourceTypeVO> list =
        CopyBeanUtil.copyList(pageInfo.getRecords(), ResourceTypeVO.class);
    return new PagingData<ResourceTypeVO>(list, pageInfo);
  }

  @Override
  public ResourceTypeVO
  getResourceTypeByResourceTypeId(Long resourceTypeId) {
    if (resourceTypeId == null) {
      return null;
    }
    ResourceType resourceType =
        this.resourceTypeDao.selectByResourceTypeId(resourceTypeId);
    return CopyBeanUtil.copy(resourceType, ResourceTypeVO.class);
  }

  @Override
  public void saveResourceType(List<String> resourceTypeNameList) {
    ArrayList<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
    for (String resourceName : resourceTypeNameList) {
      ResourceType resourceType = new ResourceType();
      resourceType.setTypeName(resourceName);
      resourceTypeList.add(resourceType);
    }
    this.resourceTypeDao.insertBatch(resourceTypeList);
  }
}
