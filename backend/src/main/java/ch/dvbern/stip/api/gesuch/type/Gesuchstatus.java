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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.type;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum Gesuchstatus {
    IN_BEARBEITUNG_GS,
    GESUCH_EINGEREICHT,
    BEREIT_FUER_BEARBEITUNG,
    ABKLAERUNG_DURCH_RECHSTABTEILUNG,
    ANSPRUCH_MANUELL_PRUEFEN,
    NICHT_ANSPRUCHSBERECHTIGT,
    IN_BEARBEITUNG_SB,
    GESUCH_ABGELEHNT,
    NICHT_BEITRAGSBERECHTIGT,
    REVISION_IN_PRUEFUNG,
    JOUR_FIX,
    FEHLENDE_DOKUMENTE,
    IN_FREIGABE,
    VERFUEGT,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    VERSANDBEREIT,
    VERSENDET,
    KEIN_STIPENDIEN_ANSPRUCH,
    BEREIT_FUER_AUSZAHLUNG,
    UEBERMITTELT_AN_SAP;

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(EnumSet.of(
        IN_BEARBEITUNG_GS
    ));

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_EDIT = Collections.unmodifiableSet(EnumSet.of(
        BEREIT_FUER_BEARBEITUNG,
        ABKLAERUNG_DURCH_RECHSTABTEILUNG,
        ANSPRUCH_MANUELL_PRUEFEN,
        NICHT_ANSPRUCHSBERECHTIGT,
        JOUR_FIX,
        IN_BEARBEITUNG_SB,
        IN_FREIGABE,
        VERFUEGT,
        WARTEN_AUF_UNTERSCHRIFTENBLATT,
        VERSANDBEREIT,
        VERSENDET
    ));

    public boolean isEingereicht() {
        return this != IN_BEARBEITUNG_GS;
    }
}
