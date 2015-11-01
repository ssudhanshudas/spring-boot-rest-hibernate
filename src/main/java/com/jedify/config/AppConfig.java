package com.jedify.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by j1013575 on 10/31/2015.
 */
@Configuration
@PropertySource("classpath:datasource.properties")
public class AppConfig extends WebMvcConfigurerAdapter{

    Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Resource
    private Environment env;

    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public HibernateTemplate hibernateTemplate() {
        return new HibernateTemplate(sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory(){
        LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource());
        sessionFactoryBuilder.scanPackages("com.jedify");
        sessionFactoryBuilder.addProperties(getHibernateProperties());
        return sessionFactoryBuilder.buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.use_sql_comments","true");
        properties.put("hibernate.enable_lazy_load_no_trans",true);
        return properties;
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        Properties properties = new Properties();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://"
                        + env.getRequiredProperty("datasource.connection.host")
                        + ":"
                        + env.getRequiredProperty("datasource.connection.port")
                        + "/"
                        + env.getRequiredProperty("datasource.connection.database")
        );
        dataSource.setUsername(env.getRequiredProperty("datasource.credential.user"));
        dataSource.setPassword(env.getRequiredProperty("datasource.credential.password"));
        dataSource.setDefaultAutoCommit(true);
        return dataSource;
    }

}
