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

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.stream.Streams;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class LandSeeding extends Seeder {
    private static final String PATH_TO_CSV = "/seeding/land/laender.csv";

    private final LandRepository landRepository;
    private final ConfigService configService;

    @Override
    public int getPriority() {
        return 2000;
    }

    @Override
    protected void seed() {
        LOG.info("Seeding Laender");
        if (landRepository.findAll().count() != 0) {
            LOG.info("Skipping Land seeding because Laender already exist in DB");
            return;
        }

        final var laender = getLaenderToSeed();
        landRepository.persist(laender);
        landRepository.flush();
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    @SneakyThrows
    private List<Land> getLaenderToSeed() {
        // See docs/laenderimport.md for documentation on the required structure of the CSV
        try (final var resource = getClass().getResourceAsStream(PATH_TO_CSV)) {
            if (resource == null) {
                throw new FileNotFoundException("Could not load CSV to seed laender: " + PATH_TO_CSV);
            }

            try (
                final var reader = new CSVReaderBuilder(new InputStreamReader(resource, StandardCharsets.UTF_8))
                    .withSkipLines(1)
                    .withCSVParser(
                        new CSVParserBuilder()
                            .withSeparator(';')
                            .build()
                    )
                    .build();
            ) {
                return Streams.of(reader.iterator())
                    .map(
                        plzLine -> new Land()
                            .setLaendercodeBfs(plzLine[0])
                            .setIso2code(plzLine[1].isEmpty() ? null : plzLine[1])
                            .setIso3code(plzLine[2].isEmpty() ? null : plzLine[2])
                            .setDeKurzform(plzLine[3])
                            .setFrKurzform(plzLine[4])
                            .setItKurzform(plzLine[5])
                            .setEnKurzform(plzLine[6])
                            .setGueltig(parseJaNein(plzLine[7]))
                            .setIsEuEfta(parseJaNein(plzLine[8]))
                    )
                    .toList();
            }
        }
    }

    boolean parseJaNein(final String input) {
        if (input == null) {
            return false;
        }

        if (input.length() != 1) {
            throw new IllegalStateException("Input string length must be 1, was: " + input);
        }

        final var character = input.charAt(0);
        return switch (character) {
            case 'J' -> true;
            case 'N' -> false;
            default -> throw new IllegalStateException("'" + character + "' is not J or N (case sensitive)");
        };
    }
}
