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

package ch.dvbern.stip.api.common.exception;

import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppFailureMessage {

    private static final String APP_FAILURE = "AppFailure.";

    public static AppFailureMessage internalError(String message) {
        return build(
            I18nMessage.of(
                APP_FAILURE + "INTERNAL_ERROR",
                "message",
                String.valueOf(message)
            )
        );
    }

    public static AppFailureMessage jsonParsing(String message, String location) {
        return build(
            I18nMessage.of(
                APP_FAILURE + "JSON_PARSING",
                "message",
                message,
                "location",
                location
            )
        );
    }

    public static AppFailureMessage jsonMapping() {
        return build(
            I18nMessage.of(
                APP_FAILURE + "JSON_MAPPING"
            )
        );
    }

    public static AppFailureMessage changedByOtherUser() {
        return build(
            I18nMessage.of(
                APP_FAILURE + "CHANGED_BY_OTHER_USER"
            )
        );
    }

    public static AppFailureMessage missingRequestHeader(String header) {
        return build(
            I18nMessage.of(
                APP_FAILURE + "MISSING_HEADER",
                "header",
                header
            )
        );
    }

    public static AppFailureMessage unrecognizedProperty(
        Class<?> owningDto,
        String propertyName,
        String path,
        String allowedProperties
    ) {
        return build(
            I18nMessage.of(
                APP_FAILURE + "UNRECOGNIZED_PROPERTY",
                "dtoName",
                owningDto.getSimpleName(),
                "propertyName",
                propertyName,
                "path",
                path,
                "allowedProperties",
                allowedProperties
            )
        );
    }

    public static AppFailureMessage databaseConstraintViolation() {
        return build(I18nMessage.of(APP_FAILURE + "DATABASE_CONSTRAINT_VIOLATION"));
    }

    public static AppFailureMessage missingSubject() {
        return build(I18nMessage.of(APP_FAILURE + "MISSING_JWT_SUBJECT"));
    }

    public static AppFailureMessage missingTenantConfig(final String path, final String tenant) {
        return build(
            I18nMessage.of(
                APP_FAILURE + "MISSING_TENANT_CONFIG",
                "path",
                path,
                "tenant",
                tenant
            )
        );
    }

    I18nMessage i18nMessage;

    ExceptionId id;

    private static AppFailureMessage build(I18nMessage message) {
        var id = ExceptionId.random();
        return new AppFailureMessage(message, id);
    }

    public AppFailureException create() {
        return new AppFailureException(this);
    }

    public AppFailureException create(final Throwable t) {
        return new AppFailureException(this, t);
    }
}
