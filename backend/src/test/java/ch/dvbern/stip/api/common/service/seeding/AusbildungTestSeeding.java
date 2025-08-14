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

package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.util.TestConstants;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AusbildungTestSeeding extends Seeder {
    private final AusbildungSeeding ausbildungSeeding;
    private final AusbildungsgangRepository ausbildungsgangRepository;

    @Override
    protected void seed() {
        ausbildungSeeding.seed();

        TestConstants.TEST_AUSBILDUNGSGANG_ID = ausbildungsgangRepository.findAll().firstResult().getId();
    }

    @Override
    protected List<String> getProfiles() {
        return List.of("test");
    }
}
