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
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class AppValidationMessage {

    private static final String APP_VALIDATION = "AppValidation.";

    public static AppValidationMessage invalidDate(String monthYear) {
        return of(
            "InvalidDate",
            I18nMessage.of(APP_VALIDATION + "INVALID_DATE", "date", monthYear)
        );
    }

    public static AppValidationMessage badSignatureDetectedInUpload() {
        return of(
            "BadSignatureDetectedInUpload",
            I18nMessage.of(APP_VALIDATION + "BAD_SIGNATURE_DETECTED_IN_UPLOAD")
        );
    }

    // This key is sent to the client in the error response
    String clientKey;

    I18nMessage i18nMessage;

    public AppValidationException create() {
        return new AppValidationException(this);
    }
}
