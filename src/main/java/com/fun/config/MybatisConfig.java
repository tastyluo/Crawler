package com.fun.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by tasty on 2017/3/27 0027.
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.ljx.mapper")
public class MybatisConfig {

    /**
     * 配置事务管理器
     */
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
