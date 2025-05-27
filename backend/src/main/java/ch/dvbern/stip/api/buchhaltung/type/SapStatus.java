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

package ch.dvbern.stip.api.buchhaltung.type;

import java.math.BigInteger;

public enum SapStatus {
    IN_PROGRESS, // Initial: Die Delivery wurde noch nicht vollständig verarbeitet.
    SUCCESS, // Erfolgreich: Alle Belege/Datensätze der Delivery wurden erfolgreich verarbeitet.
    PARTIAL_SUCCESS, // Teilerfolg: Mindestens ein Beleg/Datensätze der Delivery enthält einen Fehler und einen
                     // Erfolgsfall.
    FAILURE, // Fehler: Alle Belege/Datensätze der Delivery enthalten Fehler.
    SUSPENDED; // Abgeschlossen: Alle oder ein Teil der Belege der Delivery wurden nicht bearbeitet.

    public static SapStatus parse(final BigInteger code) {
        return switch (code.intValue()) {
            case 0 -> IN_PROGRESS;
            case 1 -> SUCCESS;
            case 2 -> PARTIAL_SUCCESS;
            case 3 -> FAILURE;
            case 4 -> SUSPENDED;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }
}
