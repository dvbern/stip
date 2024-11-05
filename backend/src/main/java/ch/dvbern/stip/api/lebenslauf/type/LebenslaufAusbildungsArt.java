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

package ch.dvbern.stip.api.lebenslauf.type;

import lombok.Getter;

@Getter
public enum LebenslaufAusbildungsArt {
    BERUFSVORBEREITENDES_SCHULJAHR(false),
    VORLEHRE(false),
    GESTALTERISCHE_VORKURSE(false),
    EIDGENOESSISCHES_BERUFSATTEST(false),
    EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS(true),
    BERUFSMATURITAET_NACH_LEHRE(false),
    FACHMATURITAET(false),
    GYMNASIALE_MATURITAETSSCHULEN(false),
    EIDGENOESSISCHES_DIPLOM(false),
    EIDGENOESSISCHER_FACHAUSWEIS(false),
    DIPLOM_HOEHERE_FACHSCHULE(false),
    BACHELOR_HOCHSCHULE_UNI(false),
    BACHELOR_FACHHOCHSCHULE(true),
    MASTER(true),
    ANDERER_BILDUNGSABSCHLUSS(false);

    private final boolean isBerufsbefaehigenderAbschluss;

    LebenslaufAusbildungsArt(boolean isBerufsbefaehigenderAbschluss) {
        this.isBerufsbefaehigenderAbschluss = isBerufsbefaehigenderAbschluss;
    }
}
