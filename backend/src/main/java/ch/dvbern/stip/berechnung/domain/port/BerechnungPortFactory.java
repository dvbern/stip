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

package ch.dvbern.stip.berechnung.domain.port;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.berechnung.domain.model.BerechnungAdapterType;
import ch.dvbern.stip.berechnung.domain.qualifier.BerechnungQualifierLiteral;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class BerechnungPortFactory {
    @Any
    @Inject
    Instance<BerechnungPort> berechnungPorts;

    private final StipConfig stipConfig;

    public BerechnungPort getBerechnungPort() {
        return berechnungPorts.select(new BerechnungQualifierLiteral(BerechnungAdapterType.BERN, 1, 0)).get();
    }
}
