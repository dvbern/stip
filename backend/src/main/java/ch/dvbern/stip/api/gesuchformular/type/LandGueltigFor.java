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

package ch.dvbern.stip.api.gesuchformular.type;

import java.util.function.Function;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.LandGueltigForUtil;
import ch.dvbern.stip.api.land.entity.Land;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LandGueltigFor {
    PERSON_IN_AUSBILDUNG_NATIONALITAET(
    "personInAusbildung",
    formular -> {
        if (formular.getPersonInAusbildung() == null) {
            return null;
        }

        return formular.getPersonInAusbildung().getNationalitaet();
    }
    ),
    PERSON_IN_AUSBILDUNG_ADRESSE(
    "personInAusbildung",
    formular -> {
        if (formular.getPersonInAusbildung() == null) {
            return null;
        }

        final var pia = formular.getPersonInAusbildung();
        if (pia.getAdresse() == null) {
            return null;
        }

        return pia.getAdresse().getLand();
    }
    ),
    ELTERN_MUTTER(
    "elterns",
    formular -> LandGueltigForUtil.getLandForElterntyp.apply(formular, ElternTyp.MUTTER)
    ),
    ELTERN_VATER(
    "elterns",
    formular -> LandGueltigForUtil.getLandForElterntyp.apply(formular, ElternTyp.VATER)
    ),
    AUSZAHLUNG(
    "auszahlung",
    formular -> {
        if (formular.getAuszahlung() == null) {
            return null;
        }

        final var auszahlung = formular.getAuszahlung();
        if (auszahlung.getAdresse() == null) {
            return null;
        }

        return auszahlung.getAdresse().getLand();
    }
    );

    private final String property;
    private final Function<GesuchFormular, Land> producer;
}
