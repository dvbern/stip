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

package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
class PlzServiceTest {
    @Inject
    PlzService plzService;

    @Test
    void isPLZInCantonBernTest() {

        final String plzInBern1 = "3011";
        final String plzInBern2 = "1657";

        final String plzNotInBern = "8032";
        final String plzOutsideSwitzerland = "200000";
        final String invalidPlz = "test20";

        final String exInBern = "2830";// Vellerat(JU),since1996

        assertThat(plzService.isInBern(new Adresse().setPlz(plzInBern1))).isTrue();
        assertThat(plzService.isInBern(new Adresse().setPlz(plzInBern2))).isTrue();
        assertThat(plzService.isInBern(new Adresse().setPlz(exInBern))).isFalse();
        assertThat(plzService.isInBern(new Adresse().setPlz(plzNotInBern))).isFalse();

        assertThat(plzService.isInBern(new Adresse().setPlz(plzOutsideSwitzerland))).isFalse();
        assertThat(plzService.isInBern(new Adresse().setPlz(invalidPlz))).isFalse();
        assertThat(plzService.isInBern(new Adresse().setPlz(""))).isFalse();
        assertThat(plzService.isInBern(new Adresse().setPlz(null))).isFalse();

    }
}
