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

package ch.dvbern.stip.integration.plzfetch.domain.port;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.integration.plzfetch.domain.qualifier.PlzFetchQualifierLiteral;
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
public class PlzFetchPortFactory {
    @Any
    @Inject
    Instance<PlzFetchPort> plzFetchPorts;

    private final StipConfig stipConfig;

    public PlzFetchPort getPlzFetchAdapter() {
        final var adapterType = stipConfig.globalPorts().plzFetch().adapterType();
        return plzFetchPorts.select(new PlzFetchQualifierLiteral(adapterType)).get();
    }
}
