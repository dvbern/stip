package ch.dvbern.stip.api.common.exception;

import lombok.Getter;

@Getter
public class AppValidationException extends AppException {
    private static final long serialVersionUID = -6978804033368571677L;

    private final String clientKey;

    public AppValidationException(AppValidationMessage message) {
        super(message.getI18nMessage(), ExceptionId.random(), null);
        this.clientKey = message.getClientKey();
    }
}
