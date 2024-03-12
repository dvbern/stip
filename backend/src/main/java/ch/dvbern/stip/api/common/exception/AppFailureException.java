package ch.dvbern.stip.api.common.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

public class AppFailureException extends AppException {
    private static final long serialVersionUID = -1104206699839600550L;

    public AppFailureException(AppFailureMessage message) {
        this(message, null);
    }

    public AppFailureException(AppFailureMessage message, @Nullable Throwable cause) {
        super(message.getI18nMessage(), message.getId(), cause);
    }

}
