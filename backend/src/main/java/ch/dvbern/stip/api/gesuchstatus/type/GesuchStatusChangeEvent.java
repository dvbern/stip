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

@Getter
public enum GesuchStatusChangeEvent {
    ABKLAERUNG_DURCH_RECHSTABTEILUNG,
    VERFUEGT,
    VERSANDBEREIT,
    VERSENDET,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    IN_BEARBEITUNG_SB,
    IN_FREIGABE,
    FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT,
    GESUCH_ZURUECKWEISEN,
    ANSPRUCH_MANUELL_PRUEFEN,
    ANSPRUCH_PRUEFEN,
    BEREIT_FUER_BEARBEITUNG,
    FEHLENDE_DOKUMENTE_EINREICHEN,
    EINGEREICHT,
    JURISTISCHE_ABKLAERUNG,
    FEHLENDE_DOKUMENTE,
    NICHT_ANSPRUCHSBERECHTIGT,
    KEIN_STIPENDIENANSPRUCH,
    NICHT_BEITRAGSBERECHTIGT,
    STIPENDIENANSPRUCH,
    GESUCH_ABGELEHNT,
    NEGATIVE_VERFUEGUNG,
    AENDERUNG_AKZEPTIEREN,
    GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH,
    GESUCH_AENDERUNG_ZURUECKWEISEN_KEIN_STIPENDIENANSPRUCH,
    GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH,
    GESUCH_AENDERUNG_ZURUECKWEISEN_STIPENDIENANSPRUCH
}
