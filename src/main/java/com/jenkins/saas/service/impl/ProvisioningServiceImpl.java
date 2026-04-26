package com.jenkins.saas.service.impl;

import com.jenkins.saas.entity.Tenant;
import com.jenkins.saas.exceptions.TenantProvisioningException;
import com.jenkins.saas.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSources;

    @Override
    public void provisionTenant(final Tenant tenant) {
        final String schemaName = "tanant" + tenant.getCompanyCode().toLowerCase();
        //1. create postgres schema
        createSchema(schemaName);
        log.info("Schema created successfully: {}", schemaName);

        //2.Run Flyway migrations for this schema

        runTenantMigration(schemaName);
        log.info("Tenant migration completed successfully for schema: {}", schemaName);
        //3.Initialize the default data(optionnel)

        initializeDefaultDate(schemaName, tenant);
        try {

            log.info("Provisioning tenant {} (schema: {}", tenant.getCompanyCode(), schemaName);

        } catch (final Exception e) {
            log.error("Failed to provision tenant : {}", tenant.getCompanyName(), e);

            //rollback : drop schema creation

            try {
                dropSchema(schemaName);

            } catch (final Exception e1) {
                log.error("Failed to rollback schema {}", tenant.getCompanyName(), e1);

            }
            throw new TenantProvisioningException("Failed to provision tenant");

        }
    }

    private void dropSchema(final String schemaName) {
        final String sql = String.format("DROP SCHEMA IF EXISTS %s CASCADE", schemaName);
        this.jdbcTemplate.execute(sql);
    }


    private void runTenantMigration(final String schemaName) {
        log.info("Running tenant migration for schema: {}", schemaName);
        final Flyway tenantFlyway = Flyway.configure()
                .dataSource(this.dataSources)
                .schemas(schemaName)
                .locations("classpath:db/migration/tenant")
                .baselineOnMigrate(true)
                .table("flyway_schema_history")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .load();

        log.info("Tenant migration started");
        tenantFlyway.migrate();
        log.info("Tenant migration completed");

    }

    private void createSchema(final String schemaName) {
        final String sql = String.format("CREATE SCHEMA IF NOT EXIST %s;", schemaName);
        this.jdbcTemplate.execute(sql);
    }


    private void initializeDefaultDate(final String schemaName, final Tenant tenant) {

        log.info("Initializing default date for schema: {}", tenant.getCompanyName().toLowerCase());
        //here you can add default data initialization code

    }
}
