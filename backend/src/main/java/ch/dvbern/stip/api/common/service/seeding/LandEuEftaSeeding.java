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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.stammdaten.service.LandService;
import ch.dvbern.stip.generated.dto.LandEuEftaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class LandEuEftaSeeding extends Seeder {
    private final LandService landService;
    private final ObjectMapper objectMapper;
    private final ConfigService configService;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected void doSeed() {
        LOG.info("Seeding EU/EFTA Laender");
        final var json = getJson();

        List<LandEuEftaDto> dtos = new ArrayList<>();
        try {
            dtos = objectMapper.readValue(json, new TypeReference<List<LandEuEftaDto>>() {});
        } catch (JsonProcessingException e) {
            LOG.error("Failed to deserialize eueftalaender json", e);
            return;
        }
        landService.setLaenderEuEfta(dtos);
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
