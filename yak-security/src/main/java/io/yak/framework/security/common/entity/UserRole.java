package io.yak.framework.security.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户与角色关联实体。
 *
 * @author weifuwan
 */
@Data
@AllArgsConstructor
public class UserRole {
  /** 用户标识。 */
  private Long userId;
  /** 角色标识。 */
  private Long roleId;

}
