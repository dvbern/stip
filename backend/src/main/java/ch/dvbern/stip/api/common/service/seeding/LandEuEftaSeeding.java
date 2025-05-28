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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.generated.dto.LandDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class LandEuEftaSeeding extends Seeder {
    private final LandService landService;
    private final LandRepository landRepository;
    private final ObjectMapper objectMapper;
    private final ConfigService configService;

    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    protected void seed() {
        LOG.info("Seeding Laender");
        if (landRepository.findAll().count() != 0) {
            LOG.info("Skipping Land seeding because Laender already exist in DB");
            return;
        }

        // TODO KSTIP-1968: Implement CSV seeding, for now just seed Switzerland so the backend can start
        final var switzerland = new LandDto();
        switzerland.setIsEuEfta(true);
        switzerland.setLaendercodeBfs("8100");
        switzerland.setIso3code("CHE");
        switzerland.setDeKurzform("Schweiz");
        switzerland.setFrKurzform("Suisse");
        switzerland.setItKurzform("Svizzera");
        switzerland.setEnKurzform("Switzerland");
        switzerland.setEintragGueltig(true);
        landService.createLand(switzerland);
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    String getJson() {
        var jsonString = "";
        final var classLoader = getClass().getClassLoader();

        try (final var is = classLoader.getResourceAsStream("/seeding/landeuefta/eueftalaender.json")) {
            if (is != null) {
                final var reader = new BufferedReader(new InputStreamReader(is));
                jsonString = reader.lines().collect(Collectors.joining());
            } else {
                LOG.warn("Tried to load eueftalaender.json but cannot find it");
            }
        } catch (IOException e) {
            LOG.error("Failed to load eueftalaender eueftalaender.json", e);
        }
        return jsonString;
    }
}
