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

package ch.dvbern.stip.berechnung.util;

import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.BernBerechnungAdapterV1_0;
import ch.dvbern.stip.berechnung.domain.port.BerechnungPortFactory;
import ch.dvbern.stip.berechnung.domain.service.BerechnungService;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import lombok.experimental.UtilityClass;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

@UtilityClass
public class BerechnungTestUtil {

    public BerechnungService getMockBerechnungService() {

        final var berechnungPort = new BernBerechnungAdapterV1_0(Mappers.getMapper(BerechnungsStammdatenMapper.class));

        final var berechnungPortFactory = Mockito.mock(BerechnungPortFactory.class);

        Mockito.doAnswer(invocation -> berechnungPort).when(berechnungPortFactory).getBerechnungPort();

        return new BerechnungService(
            berechnungPortFactory
        );
    }
}
