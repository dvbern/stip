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

package ch.dvbern.stip.stipdecision.type;

import lombok.Getter;

@Getter
public enum StipDeciderResult {
    GESUCH_VALID("Gesuch valid"),
    ANSPRUCH_UNKLAR("Anspruch unklar"),
    ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_FINANZIELL_UNABHAENGIG(
    "Anspruch manuell prüfen: Stipendienrechtlicher Wohnsitz, Finanziell unabhaengig"
    ),
    ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN(
    "Anspruch manuell prüfen: Stipendienrechtlicher Wohnsitz, Heimatort nicht Bern"
    ),
    ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN(
    "Anspruch manuell prüfen: Stipendienrechtlicher Wohnsitz, Wohnsitz Eltern nicht Bern"
    ),
    ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_KESB_NICHT_BERN(
    "Anspruch manuell prüfen: Stipendienrechtlicher Wohnsitz, KESB nicht Bern"
    ),
    ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNG_NICHT_ANERKANNT("Anspruch manuell prüfen: Ausbildung nicht anerkannt"),
    ANSPRUCH_MANUELL_PRUEFEN_ZWEITAUSBILDUNG("Anspruch manuell prüfen: Zweitausbildung"),
    ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNGSDAUER("Anspruch manuell prüfen: Ausbildungsdauer"),
    ANSPRUCH_MANUELL_PRUEFEN_ALTER_PIA("Anspruch manuell prüfen: Alter PIA"),
    NEGATIVVERFUEGUNG_NICHTEINTRETENSVERFUEGUNG("Negativverfügung: Nichteintretensverfügung"),
    NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_FLUECHTLING_NICHT_BERN(
    "Negativverfügung: Stipendienrechtlicher Wohnsitz, Flüchtling nicht Bern"
    ),
    NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_PIA_NICHT_BERN(
    "Negativverfügung: Stipendienrechtlicher Wohnsitz, Wohnsitz PIA nicht Bern"
    ),
    // NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN("Negativverfügung: Stipendienrechtlicher
    // Wohnsitz, Heimatort nicht Bern"),
    NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN(
    "Negativverfügung: Stipendienrechtlicher Wohnsitz, Wohnsitz Eltern nicht Bern"
    ),
    NEGATIVVERFUEGUNG_NICHT_BERECHTIGTE_PERSON("Negativverfügung: Nicht berechtigte Person"),
    ;

    private final String text;

    StipDeciderResult(String text) {
        this.text = text;
    }
}
