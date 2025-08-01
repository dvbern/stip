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

package ch.dvbern.stip.api.gesuchformular.util;

import java.util.function.BiFunction;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.land.entity.Land;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LandGueltigForUtil {
    public static final BiFunction<GesuchFormular, ElternTyp, Land> getLandForElterntyp = (formular, elterntyp) -> {
        if (formular.getElterns() == null) {
            return null;
        }

        final var elternteil = formular.getElternteilOfTyp(elterntyp);
        return elternteil.map(eltern -> eltern.getAdresse().getLand()).orElse(null);
    };
}
