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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum Gesuchstatus {
    ABKLAERUNG_DURCH_RECHSTABTEILUNG,
    VERFUEGT,
    VERFUEGUNG_DRUCKBEREIT,
    VERFUEGUNG_AM_GENERIEREN,
    VERFUEGUNG_VERSANDBEREIT,
    VERFUEGUNG_VERSENDET,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    IN_BEARBEITUNG_SB,
    IN_FREIGABE,
    IN_BEARBEITUNG_GS,
    ANSPRUCH_MANUELL_PRUEFEN,
    ANSPRUCH_PRUEFEN,
    BEREIT_FUER_BEARBEITUNG,
    EINGEREICHT,
    JURISTISCHE_ABKLAERUNG,
    FEHLENDE_DOKUMENTE,
    NICHT_ANSPRUCHSBERECHTIGT,
    NICHT_BEITRAGSBERECHTIGT,
    STIPENDIENANSPRUCH,
    KEIN_STIPENDIENANSPRUCH,
    GESUCH_ABGELEHNT,
    NEGATIVE_VERFUEGUNG,
    DATENSCHUTZBRIEF_DRUCKBEREIT,
    DATENSCHUTZBRIEF_AM_GENERIEREN,
    DATENSCHUTZBRIEF_VERSANDBEREIT;

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_MODIFY_DOKUMENT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS,
            FEHLENDE_DOKUMENTE
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_MODIFY_DOKUMENT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_SB
        )
    );

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_SB
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_OR_JURIST_CAN_TRIGGER_DATENSCHUTZBRIEF_DRUCKBEREIT =
        Collections.unmodifiableSet(
            EnumSet.of(
                ANSPRUCH_PRUEFEN,
                ANSPRUCH_MANUELL_PRUEFEN,
                JURISTISCHE_ABKLAERUNG
            )
        );

    public static final Set<Gesuchstatus> JURIST_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            ABKLAERUNG_DURCH_RECHSTABTEILUNG
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_TRIGGER_ANSPRUCH_CHECK = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_SB
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
            Gesuchstatus.VERFUEGUNG_DRUCKBEREIT,
            Gesuchstatus.VERFUEGUNG_VERSENDET
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_GET_BERECHNUNG = Collections.unmodifiableSet(
        EnumSet.of(
            Gesuchstatus.IN_BEARBEITUNG_SB,
            Gesuchstatus.VERFUEGT,
            Gesuchstatus.VERFUEGUNG_DRUCKBEREIT,
            Gesuchstatus.VERFUEGUNG_VERSENDET,
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            Gesuchstatus.IN_FREIGABE,
            Gesuchstatus.NICHT_BEITRAGSBERECHTIGT,
            Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
            Gesuchstatus.STIPENDIENANSPRUCH,
            Gesuchstatus.GESUCH_ABGELEHNT,
            Gesuchstatus.NEGATIVE_VERFUEGUNG,
            Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT
        )
    );

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN = Collections.unmodifiableSet(
        EnumSet.of(
            STIPENDIENANSPRUCH,
            KEIN_STIPENDIENANSPRUCH
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_UPDATE_NACHFRIST = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_SB,
            FEHLENDE_DOKUMENTE
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
            VERFUEGUNG_DRUCKBEREIT
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_CREATE_SALDOKORREKTUR = Collections.unmodifiableSet(
        EnumSet.of(
            BEREIT_FUER_BEARBEITUNG,
            IN_BEARBEITUNG_SB,
            JURISTISCHE_ABKLAERUNG,
            IN_FREIGABE,
            WARTEN_AUF_UNTERSCHRIFTENBLATT,
            VERFUEGUNG_DRUCKBEREIT,
            VERFUEGT,
            VERFUEGUNG_VERSENDET,
            STIPENDIENANSPRUCH,
            KEIN_STIPENDIENANSPRUCH
        )
    );

    public static final Set<Gesuchstatus> GESUCHSTELLER_RECEIVES_CURRENT_GESUCH = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS,
            FEHLENDE_DOKUMENTE,
            STIPENDIENANSPRUCH,
            KEIN_STIPENDIENANSPRUCH
        )
    );

    public static final Set<Gesuchstatus> GESUCH_VERFUEGUNG_ABGESCHLOSSEN = Collections.unmodifiableSet(
        EnumSet.of(
            STIPENDIENANSPRUCH,
            KEIN_STIPENDIENANSPRUCH
        )
    );

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_CREATE_BESCHWERDE_ENTSCHEID = Collections.unmodifiableSet(
        EnumSet.of(KEIN_STIPENDIENANSPRUCH, STIPENDIENANSPRUCH)
    );

    public boolean isEingereicht() {
        return this != IN_BEARBEITUNG_GS;
    }

}
