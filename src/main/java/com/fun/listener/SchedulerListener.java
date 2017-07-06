package com.fun.listener;

import com.fun.schedule.MyScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-05 13:49
 */
@Configuration
@EnableScheduling
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    public MyScheduler myScheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            myScheduler.init();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
