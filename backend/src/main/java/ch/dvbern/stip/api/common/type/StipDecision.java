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

package ch.dvbern.stip.api.common.type;

public enum StipDecision {
    GESUCH_VALID,
    EINGABEFRIST_ABGELAUFEN,
    AUSBILDUNG_NICHT_ANERKANNT,
    AUSBILDUNGEN_LAENGER_12_JAHRE,
    AUSBILDUNG_IM_LEBENSLAUF,
    PIA_AELTER_35_JAHRE,
    KEIN_WOHNSITZ_KANTON_BE,
    SCHULJAHR_9_SEKSTUFE_1,
    AUSBILDUNG_BPI1,
    ART_32_BBV,
    ZWEITAUSBILDUNG,
    AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG,
    EBA_LEHRE_2,
    HOCHSCHULSTUDIUM_2,
    MEHRERE_AUSBILDUNGSWECHSEL,
    NICHT_BERECHTIGTE_PERSON,
    ANSPRUCH_MANUELL_PRUEFEN,
}
