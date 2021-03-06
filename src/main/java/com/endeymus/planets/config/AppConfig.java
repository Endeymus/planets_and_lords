package com.endeymus.planets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Конфигулационный класс для основного функицонала приложения
 * @author Mark Shamray
 */
@Configuration
@ComponentScan(basePackages = {"com.endeymus.planets"})
@PropertySource(value = {"classpath:database/db.properties"})
@Profile("dev")
public class AppConfig {

    @Value("${spring.driver}")
    private String driverClassName;
    @Value("${spring.url}")
    private String url;
    @Value("${spring.username}")
    private String username;
    @Value("${spring.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean(name = "jdbcTemplateParam")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

}
