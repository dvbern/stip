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

import java.io.Serial;

import org.checkerframework.checker.nullness.qual.Nullable;

public class AppFailureException extends AppException {
    @Serial
    private static final long serialVersionUID = -1104206699839600550L;

    public AppFailureException(AppFailureMessage message) {
        this(message, null);
    }

    public AppFailureException(AppFailureMessage message, @Nullable Throwable cause) {
        super(message.getI18nMessage(), message.getId(), cause);
    }
}
