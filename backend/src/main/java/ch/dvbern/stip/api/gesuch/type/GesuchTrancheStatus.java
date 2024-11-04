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

package ch.dvbern.stip.api.gesuch.type;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum GesuchTrancheStatus {
    IN_BEARBEITUNG_GS,
    UEBERPRUEFEN,
    MANUELLE_AENDERUNG,
    AKZEPTIERT,
    ABGELEHNT;

    public static final Set<GesuchTrancheStatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS
        )
    );

    public static final Set<GesuchTrancheStatus> SACHBEARBEITER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            UEBERPRUEFEN,
            MANUELLE_AENDERUNG
        )
    );

    public static final Set<GesuchTrancheStatus> ADMIN_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS,
            UEBERPRUEFEN,
            AKZEPTIERT,
            ABGELEHNT
        )
    );
}
