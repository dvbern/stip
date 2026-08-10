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

package ch.dvbern.stip.berechnung.adapter.dummy;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.domain.model.BerechnungAdapterType;
import ch.dvbern.stip.berechnung.domain.port.BerechnungPort;
import ch.dvbern.stip.berechnung.domain.qualifier.BerechnungQualifier;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
@BerechnungQualifier(
    type = BerechnungAdapterType.DUMMY,
    majorVersion = 0,
    minorVersion = 0
)
public class DummyBerechnungAdapter implements BerechnungPort {

    @Override
    public BerechnungsresultatDto getBerechnungsresultat(Gesuch gesuch) {
        return null;
    }
}
