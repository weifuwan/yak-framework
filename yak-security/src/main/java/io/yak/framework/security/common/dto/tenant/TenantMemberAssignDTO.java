package io.yak.framework.security.common.dto.tenant;

import java.util.List;
import lombok.Data;

/**
 * 租户成员全量分配参数。
 *
 * <p>三个列表都采用全量替换语义；管理员和默认租户用户必须同时出现在用户列表中。
 */
@Data
public class TenantMemberAssignDTO {

  /** 租户成员用户标识列表。 */
  private List<Long> userIdList;

  /** 租户管理员用户标识列表。 */
  private List<Long> adminUserIdList;

  /** 将当前租户设为默认租户的用户标识列表。 */
  private List<Long> defaultUserIdList;
}
