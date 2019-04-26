package com.real.rabbit.springboot.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author: mabin
 * @create: 2019/4/26 14:51
 */
@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfig {

    private static Logger logger = LoggerFactory.getLogger(DruidDataSourceConfig.class);

    @Autowired
    private DruidDataSourceSettings druidDataSourceSettings;

    public static String DRIVER_CLASSNAME;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(druidDataSourceSettings.getDriverClassName());
        DRIVER_CLASSNAME = druidDataSourceSettings.getDriverClassName();
        druidDataSource.setUrl(druidDataSourceSettings.getUrl());
        druidDataSource.setUsername(druidDataSourceSettings.getUsername());
        druidDataSource.setPassword(druidDataSourceSettings.getPassword());
        druidDataSource.setInitialSize(druidDataSourceSettings.getInitialSize());
        druidDataSource.setMinIdle(druidDataSourceSettings.getMinIdle());
        druidDataSource.setMaxActive(druidDataSourceSettings.getMaxActive());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidDataSourceSettings.getTimeBetweenEvictionRunsMillers());
        druidDataSource.setMinEvictableIdleTimeMillis(druidDataSourceSettings.getMinEvictableIdleTimeMillers());
        druidDataSource.setValidationQuery(druidDataSourceSettings.getValidationQuery());
        druidDataSource.setTestWhileIdle(druidDataSourceSettings.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidDataSourceSettings.isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidDataSourceSettings.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(druidDataSourceSettings.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceSettings.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setFilters(druidDataSourceSettings.getFilters());
        druidDataSource.setConnectionProperties(druidDataSourceSettings.getConnectionProperties());
        logger.info("druid datasource config:{}",druidDataSource);
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }
}
