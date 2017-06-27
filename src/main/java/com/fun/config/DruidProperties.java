package com.fun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by tasty on 2017/3/27 0027.
 */
@Data
@ConfigurationProperties(prefix = "datasource.master")
public class DruidProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    private int maxActive;
    private int minIdle;
    private int initialSize;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private int maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
}
