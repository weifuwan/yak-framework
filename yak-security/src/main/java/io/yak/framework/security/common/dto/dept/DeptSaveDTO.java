package io.yak.framework.security.common.dto.dept;

import lombok.Data;

/**
 * 部门新增和编辑参数。
 */
@Data
public class DeptSaveDTO {

  /** 部门 ID，新增时为空，编辑时必填。 */
  private Long id;

  /** 部门名称。 */
  private String deptName;

  /** 部门描述。 */
  private String description;

  /** 上级部门 ID，0 或空表示根节点。 */
  private Long parentId;
}
