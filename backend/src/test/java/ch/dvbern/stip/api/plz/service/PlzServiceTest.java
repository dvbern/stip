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
import ch.dvbern.stip.api.common.type.Kanton;
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

        assertThat(plzService.isInKanton(new Adresse().setPlz(plzInBern1), Kanton.BE)).isTrue();
        assertThat(plzService.isInKanton(new Adresse().setPlz(plzInBern2), Kanton.BE)).isTrue();
        assertThat(plzService.isInKanton(new Adresse().setPlz(exInBern), Kanton.BE)).isFalse();
        assertThat(plzService.isInKanton(new Adresse().setPlz(plzNotInBern), Kanton.BE)).isFalse();

        assertThat(plzService.isInKanton(new Adresse().setPlz(plzOutsideSwitzerland), Kanton.BE)).isFalse();
        assertThat(plzService.isInKanton(new Adresse().setPlz(invalidPlz), Kanton.BE)).isFalse();
        assertThat(plzService.isInKanton(new Adresse().setPlz(""), Kanton.BE)).isFalse();
        assertThat(plzService.isInKanton(new Adresse().setPlz(null), Kanton.BE)).isFalse();

    }
}
