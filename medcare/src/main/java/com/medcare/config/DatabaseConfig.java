package com.medcare.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

/**
 * Ensures zero-config compatibility with cloud providers (like Render),
 * which provide DATABASE_URL as postgresql://user:pass@host:port/dbname.
 * Normalizes postgresql:// into a standard JDBC URL (jdbc:postgresql://...)
 * and extracts credentials if embedded in the URI.
 */
@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String configuredUrl;

    @Value("${spring.datasource.username:postgres}")
    private String configuredUser;

    @Value("${spring.datasource.password:root}")
    private String configuredPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size:10}")
    private int maxPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:2}")
    private int minIdle;

    @Bean
    @Primary
    public DataSource dataSource() {
        String url = configuredUrl;
        String user = configuredUser;
        String pass = configuredPassword;

        if (url != null && url.startsWith("postgresql://")) {
            try {
                URI uri = new URI(url);
                String userInfo = uri.getUserInfo();
                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    user = parts[0];
                    pass = parts[1];
                }
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                url = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
                log.info("Render DATABASE_URL successfully converted to JDBC: {}", url);
            } catch (Exception e) {
                log.warn("Failed to parse URI user info, prepending jdbc: fallback", e);
                url = "jdbc:" + url;
            }
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
