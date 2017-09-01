package com.fun.schedule;

import com.fun.entity.ScheduleTask;
import com.fun.mapper.ScheduleTaskMapper;
import com.fun.model.ScheduleTaskInfo;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-05 13:53
 */
@Component
public class MyScheduler {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    ScheduleTaskMapper scheduleTaskMapper;

    private final static Map<String, Class> taskClazzMap = new HashMap<String, Class>() {
        {
            put("1", JobCrawler.class);
            put("2", JobUpdater.class);
        }
    };

    private static Scheduler scheduler;

    public void init() throws SchedulerException {
        scheduler = schedulerFactoryBean.getScheduler();
//        List<ScheduleTask> tasks = scheduleTaskMapper.selectAll();
//        for (ScheduleTask task : tasks) {
//            startJob(task);
//        }
        scheduler.start();
    }

    private void startJob(ScheduleTask task) throws SchedulerException {
        // 得到调度器
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), task.getTaskGroup());
        // 得到触发器
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        if (task.getTaskCron() == null || task.getTaskCron().isEmpty()) {
            throw (new SchedulerException("执行频率不能为空"));
        }

        // 如果trigger不存在就创建一个
        if (trigger == null) {
            JobDetail jobDetail = newJob(taskClazzMap.get(task.getTaskType()))
                    .withIdentity(task.getTaskName(), task.getTaskGroup())
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCron());
            Trigger trigger2 = newTrigger()
                    .withIdentity(task.getTaskName(), task.getTaskGroup().toString())
                    .withSchedule(scheduleBuilder)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger2);
        } else {
            // Trigger存在。更新
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCron());
            // 按照新的cronExpression表达式重新构建trigger
            Trigger trigger2 = newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder)
                    .build();
            // 按照新的trigger设置job执行
            scheduler.rescheduleJob(triggerKey, trigger2);
        }
    }

    /**
     * 获取计划中的任务
     *
     * @return List<ScheduleTaskInfo>
     * @throws SchedulerException exception
     */
    public List<ScheduleTaskInfo> getScheduleTask() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleTaskInfo> jobList = new ArrayList<>();
        for ( JobKey jobKey : jobKeys ) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for ( Trigger trigger : triggers ) {
                ScheduleTaskInfo scheduleTaskInfo = new ScheduleTaskInfo();
                Trigger.TriggerState status = scheduler.getTriggerState(trigger.getKey());
                scheduleTaskInfo.setName(jobKey.getName());
                scheduleTaskInfo.setStatus(status.toString());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    scheduleTaskInfo.setCron(cronExpression);
                }
                jobList.add(scheduleTaskInfo);
            }
        }
        return jobList;
    }

    /**
     * 更新任务时间表达式
     *
     * @param scheduleTask
     * @throws SchedulerException exception
     */
    public int updateCronExpression(ScheduleTask scheduleTask) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleTask.getTaskName(), scheduleTask.getTaskGroup());
        // 获取trigger
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleTask.getTaskCron());
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .build();
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
        return scheduleTaskMapper.updateByPrimaryKeySelective(scheduleTask);
    }
}
