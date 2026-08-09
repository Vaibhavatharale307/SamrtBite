package com.smartbite.mainservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Converts the old MySQL ENUM column to VARCHAR once. New order statuses can
 * then be added in Java without breaking existing local project databases.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusSchemaMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        Integer enumColumnCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = 'orders'
                  AND column_name = 'status'
                  AND data_type = 'enum'
                """, Integer.class);

        if (enumColumnCount != null && enumColumnCount > 0) {
            jdbcTemplate.execute("ALTER TABLE orders MODIFY COLUMN status VARCHAR(30) NOT NULL");
            log.info("Migrated orders.status from ENUM to VARCHAR(30)");
        }
    }
}
