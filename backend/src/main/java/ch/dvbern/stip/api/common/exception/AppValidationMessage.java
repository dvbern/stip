package ch.dvbern.stip.api.common.exception;

import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class AppValidationMessage {


    private static final String APP_VALIDATION = "AppValidation.";

    public static AppValidationMessage invalidDate(String monthYear) {
        return of(
            "InvalidDate",
            I18nMessage.of(APP_VALIDATION + "INVALID_DATE", "date", monthYear)
        );
    }

    // This key is sent to the client in the error response
    String clientKey;

    I18nMessage i18nMessage;

    public AppValidationException create() {
        return new AppValidationException(this);
    }
}
