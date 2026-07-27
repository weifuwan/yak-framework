package io.yak.framework.security.common.vo.dept;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 部门删除前检查结果。
 */
@Data
public class DeptDeleteCheckVO {

  /** 部门 ID。 */
  private Long deptId;

  /** 是否允许删除。 */
  private Boolean deletable;

  /** 直属子部门名称。 */
  private List<String> childDeptNameList = new ArrayList<>();

  /** 直属关联用户名称。 */
  private List<String> userNameList = new ArrayList<>();
}
