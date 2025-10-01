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

package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

class EinnahmenKostenMapperTest {
    private EinnahmenKostenMapper mapper;
    private EinnahmenKosten einnahmenKosten;
    private EinnahmenKostenUpdateDto einnahmenKostenUpdateDto;

    @BeforeEach
    void setUp() {
        mapper = new EinnahmenKostenMapperImpl();
        einnahmenKosten = new EinnahmenKosten();
        einnahmenKostenUpdateDto = new EinnahmenKostenUpdateDto();
    }

    @Test
    void resetDependentDataBeforeUpdate() {
        /* reset alternativeWohnformWohnend to default */
        // arrange
        einnahmenKosten.setWgWohnend(false);
        einnahmenKosten.setAlternativeWohnformWohnend(false);
        einnahmenKostenUpdateDto.setWgWohnend(true);
        einnahmenKostenUpdateDto.setWgAnzahlPersonen(2);

        // act
        mapper.partialUpdate(einnahmenKostenUpdateDto, einnahmenKosten);

        // assert
        assertThat(einnahmenKosten.getAlternativeWohnformWohnend(), is(nullValue()));

        /* reset wgWohnend & coresponding to default */
        // arrange
        einnahmenKosten.setWgWohnend(true);
        einnahmenKosten.setWgAnzahlPersonen(2);
        einnahmenKostenUpdateDto.setWgWohnend(false);
        einnahmenKostenUpdateDto.setAlternativeWohnformWohnend(true);

        // act
        mapper.partialUpdate(einnahmenKostenUpdateDto, einnahmenKosten);

        // assert
        assertThat(einnahmenKosten.getWgWohnend(), is(false));
        assertThat(einnahmenKosten.getWgAnzahlPersonen(), is(nullValue()));

    }
}
