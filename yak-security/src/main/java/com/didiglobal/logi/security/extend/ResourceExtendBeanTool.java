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

import com.didiglobal.logi.security.extend.ResourceExtend;
import com.didiglobal.logi.security.properties.LogiSecurityProper;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(value="logiSecurityResourceExtendBeanTool")
public class ResourceExtendBeanTool {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LogiSecurityProper logiSecurityProper;
    private static final String DEFAULT_BEAN_NAME = "logiSecurityDefaultResourceExtendImpl";

    private ResourceExtend getCustomResourceExtendImplBean() {
        String customBeanName = this.logiSecurityProper.getResourceExtendBeanName();
        try {
            return (ResourceExtend)this.applicationContext.getBean(customBeanName);
        } catch (NoSuchBeanDefinitionException e) {
            throw new UnsupportedOperationException("\u672a\u80fd\u627e\u5230\u81ea\u5b9a\u4e49\u7684ResourceExtend\u5b9e\u73b0\u7c7b\u7684bean\uff0c\u4f7f\u7528\u9ed8\u8ba4bean");
        }
    }

    private ResourceExtend getDefaultResourceExtendImplBean() {
        return (ResourceExtend)this.applicationContext.getBean(DEFAULT_BEAN_NAME);
    }

    public ResourceExtend getResourceExtendImpl() {
        ResourceExtend resourceExtend;
        try {
            resourceExtend = this.getCustomResourceExtendImplBean();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            resourceExtend = this.getDefaultResourceExtendImplBean();
        }
        return resourceExtend;
    }
}

