package com.fun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-06 16:54
 */
@Configuration
public class BeanConfig {

    @Autowired
    JobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean createSchedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }
}
