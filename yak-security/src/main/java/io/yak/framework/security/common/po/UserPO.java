package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户持久化对象。
 *
 * <p>保存用户身份、联系方式和所属部门信息，密码及盐值不会出现在字符串输出中。
 *
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"pw", "salt"})
@TableName(value = "yak_security_user")
public class UserPO extends BasePO {
  /** 用户名。 */
  private String userName;

  /** 密码密文。 */
  private String pw;

  /** 密码盐值。 */
  private String salt;

  /** 用户真实姓名。 */
  private String realName;

  /** 手机号码。 */
  private String phone;

  /** 电子邮箱地址。 */
  private String email;

  /** 所属部门标识。 */
  private Long deptId;

  /** 用户状态：1 启用，2 禁用。 */
  private Integer status = 1;
}
