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

package ch.dvbern.stip.api.gesuchstatus.type;

import lombok.Getter;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

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

    public static final Set<Gesuchstatus> SB_EDITS_GESUCH =
        Collections.unmodifiableSet(
            EnumSet.of(
                IN_BEARBEITUNG_SB,
                ABKLAERUNG_DURCH_RECHSTABTEILUNG,
                BEREIT_FUER_BEARBEITUNG
            )
        );

    /*
     * Alle Gesuchstatus zwischen eingereicht bis verfügt
     */
    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_VIEW_CHANGES =
        Collections.unmodifiableSet(
            EnumSet.of(
                EINGEREICHT,
                BEREIT_FUER_BEARBEITUNG,
                IN_FREIGABE,
                JURISTISCHE_ABKLAERUNG,
                NICHT_ANSPRUCHSBERECHTIGT,
                ANSPRUCH_MANUELL_PRUEFEN,
                ABKLAERUNG_DURCH_RECHSTABTEILUNG,
                FEHLENDE_DOKUMENTE,
                NEGATIVE_VERFUEGUNG,
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
            Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
            Gesuchstatus.VERFUEGT,
            Gesuchstatus.VERSANDBEREIT,
            Gesuchstatus.VERSENDET,
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            Gesuchstatus.IN_BEARBEITUNG_SB,
            Gesuchstatus.IN_FREIGABE,
            Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN,
            Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
            Gesuchstatus.EINGEREICHT,
            Gesuchstatus.JURISTISCHE_ABKLAERUNG,
            Gesuchstatus.FEHLENDE_DOKUMENTE,
            Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT,
            Gesuchstatus.NICHT_BEITRAGSBERECHTIGT,
            Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
            Gesuchstatus.STIPENDIENANSPRUCH,
            Gesuchstatus.GESUCH_ABGELEHNT,
            Gesuchstatus.NEGATIVE_VERFUEGUNG
        )
    );

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN = Collections.unmodifiableSet(
        EnumSet.of(
            VERFUEGT,
            VERSANDBEREIT,
            VERSENDET,
            WARTEN_AUF_UNTERSCHRIFTENBLATT,
            STIPENDIENANSPRUCH
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_UPLOAD_UNTERSCHRIFTENBLATT = Collections.unmodifiableSet(
        EnumSet.of(
            ABKLAERUNG_DURCH_RECHSTABTEILUNG,
            ANSPRUCH_MANUELL_PRUEFEN,
            NICHT_ANSPRUCHSBERECHTIGT,
            BEREIT_FUER_BEARBEITUNG,
            NEGATIVE_VERFUEGUNG,
            IN_BEARBEITUNG_SB,
            JURISTISCHE_ABKLAERUNG,
            FEHLENDE_DOKUMENTE,
            IN_FREIGABE,
            VERFUEGT,
            WARTEN_AUF_UNTERSCHRIFTENBLATT,
            VERSANDBEREIT
        )
    );

    public boolean isEingereicht() {
        return this != IN_BEARBEITUNG_GS;
    }
}
