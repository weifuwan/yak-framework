package io.yak.framework.schedule.provider.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/** Quartz JobFactory that performs Spring dependency injection on job instances. */
public final class AutowiringSpringBeanJobFactory
        extends SpringBeanJobFactory {

    private final AutowireCapableBeanFactory beanFactory;

    public AutowiringSpringBeanJobFactory(
            AutowireCapableBeanFactory beanFactory) {

        this.beanFactory = beanFactory;
    }

    @Override
    protected Object createJobInstance(
            TriggerFiredBundle bundle)
            throws Exception {

        Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
