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

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EinnahmenKostenType {
    GESUCHSTELLER("gesuchsteller", (formular) -> {
        if (formular.getEinnahmenKosten() == null) {
            return null;
        }
        return formular.getEinnahmenKosten();
    }),
    PARTNER("partner", formular -> {
        if (formular.getPartner() == null) {
            return null;
        }

        return formular.getEinnahmenKostenPartner();
    });

    private final String property;
    private final Function<GesuchFormular, EinnahmenKosten> producer;
}
