package com.gealocker.be.gealockerbe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcClientConfig {
    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean("ds-postgres")
    public DataSource pgDataSource() {
        DriverManagerDataSource ds =new DriverManagerDataSource();
        ds.setDriverClassName(jdbcProperties.getJDBC_DRIVER());
        ds.setUrl(jdbcProperties.getJDBC_URI());
        ds.setUsername(jdbcProperties.getJDBC_USERNAME());
        ds.setPassword(jdbcProperties.getJDBC_PASSWORD());

        return ds;
    }

    @Bean("postgres")
    public JdbcTemplate pgJdbcTemplate(@Qualifier("ds-postgres") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
