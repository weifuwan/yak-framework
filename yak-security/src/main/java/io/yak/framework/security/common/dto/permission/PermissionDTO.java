package io.yak.framework.security.common.dto.permission;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * 权限数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class PermissionDTO {
  /** 权限编码。 */
  private String permissionCode;
  /** 权限名称。 */
  private String permissionName;
  /** 描述。 */
  private String description;
  /** 子权限列表。 */
  private List<PermissionDTO> childPermissionDTOList;

  public List<PermissionDTO> getChildPermissionDTOList() {
    if (this.childPermissionDTOList == null) {
      this.childPermissionDTOList = new ArrayList<PermissionDTO>();
    }
    return this.childPermissionDTOList;
  }

  public PermissionDTO() {}

  public PermissionDTO(String permissionName, String description) {
    this.permissionName = permissionName;
    this.description = description;
  }

  public PermissionDTO(String permissionName) {
    this.permissionName = permissionName;
    this.description = permissionName;
  }

}
