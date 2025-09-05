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

package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import jakarta.inject.Singleton;

@Singleton
@CalculatorVersion(major = 1, minor = 0)
public class PersonenImHaushaltRequestV1Builder implements PersonenImHaushaltRequestBuilder {
    @Override
    public CalculatorRequest buildRequest(final GesuchFormular gesuchFormular, final ElternTyp elternToPrioritize) {
        return PersonenImHaushaltRequestV1.createRequest(gesuchFormular, elternToPrioritize);
    }
}
