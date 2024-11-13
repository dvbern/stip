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

        final var lowerCase = name.getText().toLowerCase(Locale.US);
        return Identifier.toIdentifier(lowerCase, name.isQuoted());
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
