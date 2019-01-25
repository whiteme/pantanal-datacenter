package com.pantanal.data.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by shenn
 */
@Configuration
public class ConfigBeanRegister {
    @Autowired
    private Environment env;

    public static final String dataSource_format="pantanal.datasource.";


    @Bean(name = "xuwuDataSource")
    @Qualifier("xuwuDataSource")
    public DataSource tidbDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("mysql.datasource.url"));
        datasource.setUsername(env.getProperty("mysql.datasource.username"));
        datasource.setPassword(env.getProperty("mysql.datasource.password"));
        datasource.setDriverClassName(env.getProperty("db.mysql.driverClassName"));

        datasource.setInitialSize(Integer.parseInt(env.getProperty(dataSource_format+"initialSize")));
        datasource.setMinIdle(Integer.parseInt(env.getProperty(dataSource_format+"minIdle")));
        datasource.setMaxActive(Integer.parseInt(env.getProperty(dataSource_format+"maxActive")));
        datasource.setMaxWait(Integer.parseInt(env.getProperty(dataSource_format+"maxWait")));
        datasource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty(dataSource_format+"timeBetweenEvictionRunsMillis")));
        datasource.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty(dataSource_format+"minEvictableIdleTimeMillis")));
        datasource.setValidationQuery(env.getProperty(dataSource_format+"validationQuery"));
        datasource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty(dataSource_format+"testWhileIdle")));
        datasource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty(dataSource_format+"testOnBorrow")));
        datasource.setTestOnReturn(Boolean.parseBoolean(env.getProperty(dataSource_format+"testOnReturn")));
        datasource.setPoolPreparedStatements(Boolean.parseBoolean(env.getProperty(dataSource_format+"poolPreparedStatements")));
        datasource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(env.getProperty(dataSource_format+"maxPoolPreparedStatementPerConnectionSize")));
        //try {
        //datasource.setFilters("stat");
        //datasource.setFilters("stat,wall");
        //} catch (SQLException e) {
        //e.printStackTrace();
        //logger.error("druid err:{}", e);
        //}
        return datasource;
    }

    @Bean(name = "xuwuJdbcTemplate")
    public JdbcTemplate tidbJdbcTemplate(@Qualifier("xuwuDataSource")DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }



    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername","xuwu");
        servletRegistrationBean.addInitParameter("loginPassword","xuwu11_223344");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
