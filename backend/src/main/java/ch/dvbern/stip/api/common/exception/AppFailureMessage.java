package ch.dvbern.stip.api.common.exception;

import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.hibernate.exception.ConstraintViolationException;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppFailureMessage {

    private static final String APP_FAILURE = "AppFailure.";

    public static AppFailureMessage internalError(String message) {
        return build(I18nMessage.of(
            APP_FAILURE + "INTERNAL_ERROR",
            "message", String.valueOf(message)
        ));
    }

    public static AppFailureMessage jsonParsing(String message, String location) {
        return build(I18nMessage.of(
            APP_FAILURE + "JSON_PARSING",
            "message", message,
            "location", location
        ));
    }

    public static AppFailureMessage jsonMapping() {
        return build(I18nMessage.of(
            APP_FAILURE + "JSON_MAPPING"
        ));
    }

    public static AppFailureMessage changedByOtherUser() {
        return build(I18nMessage.of(
            APP_FAILURE + "CHANGED_BY_OTHER_USER"
        ));
    }

    public static AppFailureMessage missingRequestHeader(String header) {
        return build(I18nMessage.of(
            APP_FAILURE + "MISSING_HEADER",
            "header", header
        ));
    }

    public static AppFailureMessage unrecognizedProperty(
        Class<?> owningDto,
        String propertyName,
        String path,
        String allowedProperties) {
        return build(I18nMessage.of(
            APP_FAILURE + "UNRECOGNIZED_PROPERTY",
            "dtoName", owningDto.getSimpleName(),
            "propertyName", propertyName,
            "path", path,
            "allowedProperties", allowedProperties
        ));
    }
    public static AppFailureMessage databaseConstraintViolation() {
        return build(I18nMessage.of(APP_FAILURE + "DATABASE_CONSTRAINT_VIOLATION"));
    }

    public static AppFailureMessage missingSubject() {
        return build(I18nMessage.of(APP_FAILURE + "MISSING_JWT_SUBJECT"));
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
