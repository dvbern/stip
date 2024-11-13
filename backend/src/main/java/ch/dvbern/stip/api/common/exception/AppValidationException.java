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
