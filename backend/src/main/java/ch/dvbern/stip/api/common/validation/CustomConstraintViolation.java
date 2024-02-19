package ch.dvbern.stip.api.common.validation;

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Getter;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;

@Getter
public class CustomConstraintViolation {

    private static final ResourceBundle VALIDATION_MESSAGE_BUNDLE =
        ResourceBundle.getBundle(
            "ValidationMessages",
            Locale.GERMAN,
            requireNonNull(currentThread().getContextClassLoader())
        );
    private final String message;
    private final String messageTemplate;

    public CustomConstraintViolation(String messageTamplate) {
        this.messageTemplate = messageTamplate;
        this.message = VALIDATION_MESSAGE_BUNDLE.getString(messageTamplate);
    }
}
