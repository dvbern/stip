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

package ch.dvbern.stip.api.sap.util;

import jakarta.ws.rs.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SapReturnCodeType {
    SUCCESS("success"),
    WARNING("warning"),
    ERROR("no action | failure"),
    INFO("info");

    private final String description;

    public static SapReturnCodeType parse(final String raw) {
        return switch (raw) {
            case "S" -> SapReturnCodeType.SUCCESS;
            case "W" -> SapReturnCodeType.WARNING;
            case "E" -> SapReturnCodeType.ERROR;
            case "I" -> SapReturnCodeType.INFO;
            default -> throw new IllegalStateException("Unexpected value: " + raw);
        };
    }

    public static void assertSuccess(final String raw) throws BadRequestException {
        if (!isSuccess(raw)) {
            throw new BadRequestException();
        }
    }

    public static boolean isSuccess(final String raw) {
        return parse(raw).equals(SUCCESS);
    }
}
