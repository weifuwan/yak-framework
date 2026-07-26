package io.yak.framework.security.extend;

import io.yak.framework.security.extend.LoginExtend;
import io.yak.framework.security.properties.YakSecurityProperties;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(value = "yakSecurityLoginExtendBeanTool")
public class LoginExtendBeanTool {
  @Autowired private ApplicationContext applicationContext;
  @Autowired private YakSecurityProperties yakSecurityProperties;
  private static final String DEFAULT_BEAN_NAME =
      "yakSecurityDefaultLoginExtendImpl";

  private LoginExtend getCustomLoginExtendImplBean() {
    String customBeanName = this.yakSecurityProperties.getLoginExtendBeanName();
    try {
      return (LoginExtend)this.applicationContext.getBean(customBeanName);
    } catch (NoSuchBeanDefinitionException e) {
      throw new UnsupportedOperationException(
          "\u672a\u80fd\u627e\u5230\u81ea\u5b9a\u4e49\u7684LoginExtend\u5b9e" +
          "\u73b0\u7c7b\u7684bean\uff0c\u4f7f\u7528\u9ed8\u8ba4bean");
    }
  }

  private LoginExtend getDefaultLoginExtendImplBean() {
    return (LoginExtend)this.applicationContext.getBean(DEFAULT_BEAN_NAME);
  }

  public LoginExtend getLoginExtendImpl() {
    LoginExtend loginExtend;
    try {
      loginExtend = this.getCustomLoginExtendImplBean();
    } catch (UnsupportedOperationException e) {
      loginExtend = this.getDefaultLoginExtendImplBean();
    }
    return loginExtend;
  }
}
