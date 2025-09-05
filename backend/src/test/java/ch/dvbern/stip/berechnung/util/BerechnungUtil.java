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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ch.dvbern.stip.api.tenancy.service.MockTenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1Builder;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungsStammdatenV1Mapper;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1Builder;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltCalculator;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import ch.dvbern.stip.berechnung.service.StipendienCalculator;
import ch.dvbern.stip.berechnung.service.bern.v1.PersonenImHaushaltCalculatorV1;
import ch.dvbern.stip.berechnung.service.bern.v1.StipendienCalculatorV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

@UtilityClass
public class BerechnungUtil {
    public CalculatorRequest getRequest(final int fall) {
        try {
            final var resource =
                BerechnungUtil.class.getClassLoader().getResource(String.format("berechnung/fall_%d.json", fall));
            assert resource != null;
            final var inputs = Files.readString(Paths.get(resource.toURI()));
            final var mapper = new ObjectMapper();
            return mapper.readValue(inputs, BerechnungRequestV1.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public BerechnungService getMockBerechnungService() {
        final var personenImHaushaltCalculators = (Instance<PersonenImHaushaltCalculator>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new PersonenImHaushaltCalculatorV1()))
            .when(personenImHaushaltCalculators)
            .stream();

        final var personenImHaushaltRequestBuilders =
            (Instance<PersonenImHaushaltRequestBuilder>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new PersonenImHaushaltRequestV1Builder()))
            .when(personenImHaushaltRequestBuilders)
            .stream();

        final var personenImHaushaltService = new PersonenImHaushaltService(
            personenImHaushaltCalculators,
            personenImHaushaltRequestBuilders
        );

        final var requestBuilders = (Instance<BerechnungRequestBuilder>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new BerechnungRequestV1Builder(personenImHaushaltService)))
            .when(requestBuilders)
            .stream();

        final var berechnungStammdatenMapper = (Instance<BerechnungsStammdatenMapper>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new BerechnungsStammdatenV1Mapper()))
            .when(berechnungStammdatenMapper)
            .stream();

        final var calculators = (Instance<StipendienCalculator>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new StipendienCalculatorV1())).when(calculators).stream();

        return new BerechnungService(
            requestBuilders,
            berechnungStammdatenMapper,
            calculators,
            new MockTenantService()
        );
    }
}
