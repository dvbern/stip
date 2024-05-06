package ch.dvbern.stip.api.common.entity;

import java.util.Locale;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class LiquibaseQuarkusCompatiblePhysicalNamingStrategy implements PhysicalNamingStrategy {
    private static @Nullable Identifier allLowerCase(@Nullable Identifier name) {
        if (name == null) {
            return null;
        }

        String lowerCase = name.getText().toLowerCase(Locale.US);
        Identifier result = Identifier.toIdentifier(lowerCase, name.isQuoted());

        System.out.println("Why no work");

        return result;
    }

    @Override
    public @Nullable Identifier toPhysicalCatalogName(
        @Nullable Identifier name,
        JdbcEnvironment jdbcEnvironment
    ) {
        return allLowerCase(name);
    }

    @Override
    public @Nullable Identifier toPhysicalSchemaName(
        @Nullable Identifier name,
        JdbcEnvironment jdbcEnvironment
    ) {
        return allLowerCase(name);
    }

    @Override
    public @Nullable Identifier toPhysicalTableName(
        @Nullable Identifier name,
        JdbcEnvironment jdbcEnvironment
    ) {
        return allLowerCase(name);
    }

    @Override
    public @Nullable Identifier toPhysicalSequenceName(
        @Nullable Identifier name,
        JdbcEnvironment jdbcEnvironment
    ) {
        return allLowerCase(name);
    }

    @Override
    public @Nullable Identifier toPhysicalColumnName(
        @Nullable Identifier name,
        JdbcEnvironment jdbcEnvironment
    ) {
        return allLowerCase(name);
    }
}
