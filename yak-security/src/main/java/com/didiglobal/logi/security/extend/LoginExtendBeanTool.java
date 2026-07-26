/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.NoSuchBeanDefinitionException
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationContext
 *  org.springframework.stereotype.Component
 */
package com.didiglobal.logi.security.extend;

import com.didiglobal.logi.security.extend.LoginExtend;
import com.didiglobal.logi.security.properties.LogiSecurityProper;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(value="logiSecurityLoginExtendBeanTool")
public class LoginExtendBeanTool {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LogiSecurityProper logiSecurityProper;
    private static final String DEFAULT_BEAN_NAME = "logiSecurityDefaultLoginExtendImpl";

    private LoginExtend getCustomLoginExtendImplBean() {
        String customBeanName = this.logiSecurityProper.getLoginExtendBeanName();
        try {
            return (LoginExtend)this.applicationContext.getBean(customBeanName);
        } catch (NoSuchBeanDefinitionException e) {
            throw new UnsupportedOperationException("\u672a\u80fd\u627e\u5230\u81ea\u5b9a\u4e49\u7684LoginExtend\u5b9e\u73b0\u7c7b\u7684bean\uff0c\u4f7f\u7528\u9ed8\u8ba4bean");
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

