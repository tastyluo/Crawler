package com.fun.model;

import lombok.Data;

import javax.persistence.Table;

/**
 * 所属公司： 华信联创技术工程有限公司
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-09-01 10:02
 */
@Data
@Table(name = "sys_user")
public class UserInfo {
    private String userId;
    private String username;
    private String userRoles;
}
