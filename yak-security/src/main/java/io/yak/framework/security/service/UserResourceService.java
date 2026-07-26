package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.resource.AssignToManyUserDTO;
import io.yak.framework.security.common.dto.resource.AssignToOneUserDTO;
import io.yak.framework.security.common.dto.resource.BatchAssignDTO;
import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUQueryDTO;
import io.yak.framework.security.common.dto.resource.UserResourceQueryDTO;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import io.yak.framework.security.common.vo.resource.MByRDataVO;
import io.yak.framework.security.common.vo.resource.MByRVO;
import io.yak.framework.security.common.vo.resource.MByUDataVO;
import io.yak.framework.security.common.vo.resource.MByUVO;
import io.yak.framework.security.exception.YakSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserResourceService {
  public int getResourceCntByUserId(Integer var1, UserResourceQueryDTO var2);

  public PagingData<MByRVO> getManageByResourcePage(MByRQueryDTO var1)
      throws YakSecurityException;

  public PagingData<MByUVO> getManageByUserPage(MByUQueryDTO var1);

  public void assignResourcePermission(AssignToOneUserDTO var1)
      throws YakSecurityException;

  public void assignResourcePermission(AssignToManyUserDTO var1,
                                       HttpServletRequest var2)
      throws YakSecurityException;

  public void batchAssignResourcePermission(BatchAssignDTO var1,
                                            HttpServletRequest var2)
      throws YakSecurityException;

  public List<MByUDataVO> getManagerByUserDataList(MByUDataQueryDTO var1)
      throws YakSecurityException;

  public List<MByRDataVO> getManagerByResourceDataList(MByRDataQueryDTO var1)
      throws YakSecurityException;

  public boolean getViewPermissionControlStatus();

  public void changeResourceViewControlStatus();

  public ControlLevelCode getControlLevel(ControlLevelQueryDTO var1)
      throws YakSecurityException;
}
