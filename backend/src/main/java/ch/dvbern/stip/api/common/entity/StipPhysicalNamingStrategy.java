package ch.dvbern.stip.api.common.entity;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.regex.Pattern;

public class StipPhysicalNamingStrategy implements PhysicalNamingStrategy {

    private static final Pattern SNAKE_CASE_PATTERN = Pattern.compile("([a-z])([A-Z])");

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    private Identifier convertToSnakeCase(final Identifier identifier) {
        if (identifier == null) {
            return null;
        }
        final String newName = toSnakeCase(identifier.getText());
        return Identifier.toIdentifier(newName);
    }

    public static String toSnakeCase(String s) {
        final String replacement = "$1_$2";
        return SNAKE_CASE_PATTERN.matcher(s).replaceAll(replacement).toLowerCase();
    }
}
