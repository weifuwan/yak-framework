package io.yak.framework.security.common.dto.account;

import lombok.Data;
/**
 * 账号登录数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class AccountLoginDTO {
  /** 用户名。 */
  private String userName;
  /** 密码。 */
  private String pw;

}
