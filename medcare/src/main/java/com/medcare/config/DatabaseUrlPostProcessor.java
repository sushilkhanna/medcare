package com.medcare.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts Render's DATABASE_URL (or JDBC_DATABASE_URL) from the
 * {@code postgresql://user:password@host:port/db} format to a proper
 * JDBC URL {@code jdbc:postgresql://host:port/db} with separate
 * username and password properties.
 *
 * Runs before any beans are created, so HikariCP sees the corrected URL.
 */
public class DatabaseUrlPostProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUrlPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Check DATABASE_URL (standard) first, then JDBC_DATABASE_URL
        String rawUrl = environment.getProperty("DATABASE_URL");
        if (rawUrl == null || rawUrl.isBlank()) {
            rawUrl = environment.getProperty("JDBC_DATABASE_URL");
        }
        if (rawUrl == null || rawUrl.isBlank()) {
            return; // No URL set — local dev will use defaults from application.properties
        }

        // Already a JDBC URL — still extract credentials if embedded in the URL
        if (rawUrl.startsWith("jdbc:")) {
            log.info("Database URL is already in JDBC format");
            return;
        }

        try {
            // Parse postgresql://user:password@host:port/dbname
            URI uri = new URI(rawUrl);
            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String dbName = uri.getPath(); // includes leading /

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + dbName;

            Map<String, Object> props = new HashMap<>();
            props.put("spring.datasource.url", jdbcUrl);

            // Extract username and password from URI userInfo
            String userInfo = uri.getUserInfo();
            if (userInfo != null) {
                String[] parts = userInfo.split(":", 2);
                props.put("spring.datasource.username", parts[0]);
                if (parts.length > 1) {
                    props.put("spring.datasource.password", parts[1]);
                }
            }

            // Add sslmode for Render (required for external connections)
            if (!jdbcUrl.contains("sslmode")) {
                props.put("spring.datasource.url", jdbcUrl + "?sslmode=require");
            }

            environment.getPropertySources().addFirst(
                    new MapPropertySource("renderDatabaseUrl", props));

            log.info("Converted database URL to JDBC format: jdbc:postgresql://{}:{}{}", host, port, dbName);
        } catch (Exception e) {
            log.error("Failed to parse DATABASE_URL: {}", e.getMessage());
        }
    }
}
