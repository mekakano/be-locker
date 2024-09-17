package com.gealocker.be.gealockerbe.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class JdbcProperties {
    private String JDBC_DRIVER = "org.postgresql.Driver";
    private String JDBC_URI = "jdbc:postgresql://localhost:9721/linovdb";
    private String JDBC_USERNAME = "linovdb";
    private String JDBC_PASSWORD = "linov";
}