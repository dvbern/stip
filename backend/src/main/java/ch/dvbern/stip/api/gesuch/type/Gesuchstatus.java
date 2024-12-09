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

import lombok.Getter;

@Getter
public enum Gesuchstatus {
    ABKLAERUNG_DURCH_RECHSTABTEILUNG,
    VERFUEGT,
    VERSANDBEREIT,
    VERSENDET,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    IN_BEARBEITUNG_SB,
    IN_FREIGABE,
    IN_BEARBEITUNG_GS,
    ANSPRUCH_MANUELL_PRUEFEN,
    BEREIT_FUER_BEARBEITUNG,
    EINGEREICHT,
    JURISTISCHE_ABKLAERUNG,
    FEHLENDE_DOKUMENTE,
    NICHT_ANSPRUCHSBERECHTIGT,
    NICHT_BEITRAGSBERECHTIGT,
    KEIN_STIPENDIENANSPRUCH,
    STIPENDIENANSPRUCH,
    GESUCH_ABGELEHNT,
    NEGATIVE_VERFUEGUNG;

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_EDIT =
        Collections.unmodifiableSet(
            EnumSet.of(
                IN_BEARBEITUNG_SB
            )
        );

    public static final Set<Gesuchstatus> ADMIN_CAN_EDIT =
        Collections.unmodifiableSet(
            EnumSet.allOf(Gesuchstatus.class)
        );

    public static final Set<Gesuchstatus> GESUCH_IS_VERFUEGT_OR_FURTHER = Collections.unmodifiableSet(
        EnumSet.of(
            Gesuchstatus.VERFUEGT,
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            Gesuchstatus.VERSANDBEREIT,
            Gesuchstatus.VERSENDET
        )
    );

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_GET_BERECHNUNG = Collections.unmodifiableSet(
        EnumSet.of(
            Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
            Gesuchstatus.IN_BEARBEITUNG_SB,
            Gesuchstatus.IN_FREIGABE,
            Gesuchstatus.VERFUEGT,
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            Gesuchstatus.VERSANDBEREIT,
            Gesuchstatus.VERSENDET
        )
    );

    public boolean isEingereicht() {
        return this != IN_BEARBEITUNG_GS;
    }
}
