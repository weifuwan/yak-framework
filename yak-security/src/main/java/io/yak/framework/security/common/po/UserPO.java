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
  private String userName;
  private String pw;
  private String salt;
  private String realName;
  private String phone;
  private String email;
  private Long deptId;
  /** 用户状态：1 启用，2 禁用。 */
  private Integer status = 1;
}
