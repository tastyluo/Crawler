package com.fun.model;

import lombok.Data;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：207-05 15:41
 */
@Data
public class ScheduleTaskInfo {

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 定时表达式
     */
    private String cron;

    /**
     * 状态
     */
    private String status;

    /**
     * 分组
     */
    private String group;

    /**
     * 描述
     */
    private String describe;

    /**
     * 类型
     */
    private String type;
}
