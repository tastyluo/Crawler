package com.fun.entity;

import javax.persistence.*;

@Table(name = "schedule_task")
public class ScheduleTask {
    /**
     * 编号
     */
    @Id
    @Column(name = "TASK_ID")
    private Integer taskId;

    /**
     * 名称
     */
    @Column(name = "TASK_NAME")
    private String taskName;

    /**
     * 定时表达式
     */
    @Column(name = "TASK_CRON")
    private String taskCron;

    /**
     * 状态
     */
    @Column(name = "TASK_STATUS")
    private String taskStatus;

    /**
     * 分组
     */
    @Column(name = "TASK_GROUP")
    private String taskGroup;

    /**
     * 描述
     */
    @Column(name = "TASK_DESCRIBE")
    private String taskDescribe;

    /**
     * 类型
     */
    @Column(name = "TASK_TYPE")
    private String taskType;

    /**
     * 获取编号
     *
     * @return TASK_ID - 编号
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置编号
     *
     * @param taskId 编号
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取名称
     *
     * @return TASK_NAME - 名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置名称
     *
     * @param taskName 名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取定时表达式
     *
     * @return TASK_CRON - 定时表达式
     */
    public String getTaskCron() {
        return taskCron;
    }

    /**
     * 设置定时表达式
     *
     * @param taskCron 定时表达式
     */
    public void setTaskCron(String taskCron) {
        this.taskCron = taskCron;
    }

    /**
     * 获取状态
     *
     * @return TASK_STATUS - 状态
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置状态
     *
     * @param taskStatus 状态
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 获取分组
     *
     * @return TASK_GROUP - 分组
     */
    public String getTaskGroup() {
        return taskGroup;
    }

    /**
     * 设置分组
     *
     * @param taskGroup 分组
     */
    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    /**
     * 获取描述
     *
     * @return TASK_DESCRIBE - 描述
     */
    public String getTaskDescribe() {
        return taskDescribe;
    }

    /**
     * 设置描述
     *
     * @param taskDescribe 描述
     */
    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    /**
     * 获取类型
     *
     * @return TASK_TYPE - 类型
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * 设置类型
     *
     * @param taskType 类型
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}