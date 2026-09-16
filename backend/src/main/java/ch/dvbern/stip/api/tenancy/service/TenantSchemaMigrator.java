/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.tenancy.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Creates one database schema per {@link TenantIdentifier}
 */
@Slf4j
@ApplicationScoped
public class TenantSchemaMigrator {

    private static final String TENANT_CHANGE_LOG = "db/migration/changelog.xml";

    @Inject
    AgroalDataSource dataSource;

    @ConfigProperty(name = "quarkus.liquibase.contexts")
    Optional<List<String>> liquibaseContexts;

    void migrateTenantSchemas(@Observes @Priority(10) final StartupEvent startupEvent) {
        for (final var tenantIdentifier : TenantIdentifier.values()) {
            final var schemaName = tenantIdentifier.getIdentifier().toLowerCase(Locale.ROOT);
            try {
                createSchemaIfNotExists(schemaName);
                migrateSchema(schemaName);
            } catch (SQLException | LiquibaseException e) {
                throw new IllegalStateException("Liquibase migration failed for tenant schema: " + schemaName, e);
            }
        }
    }

    private void createSchemaIfNotExists(final String schemaName) throws SQLException {
        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS \"%s\"".formatted(schemaName));
        }
    }

    private void migrateSchema(final String schemaName) throws SQLException, LiquibaseException {
        final Connection connection = dataSource.getConnection();
        connection.setSchema(schemaName);

        final Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(schemaName);
        database.setLiquibaseSchemaName(schemaName);

        try (
            Liquibase liquibase = new Liquibase(
                TENANT_CHANGE_LOG,
                new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader()),
                database
            )
        ) {
            final var contexts = liquibaseContexts
                .map(values -> new Contexts(String.join(",", values)))
                .orElseGet(Contexts::new);
            liquibase.update(contexts, new LabelExpression());
            LOG.info("Applied changelog '{}' to tenant schema '{}'", TENANT_CHANGE_LOG, schemaName);
        }
    }
}
