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

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StipDecisionTextSeeding extends Seeder {
    private static final String PATH_TO_CSV = "/seeding/stipDecisionText/%s/stipDecisionText.csv";

    private final ConfigService configService;
    private final StipDecisionTextRepository decisionTextRepository;

    @Override
    protected void doSeed() {
        if (decisionTextRepository.count() != 0) {
            LOG.info("stip decision texts already seeded, skipping...");
            return;
        }

        final var decisionTexts = getDecisionTextsToSeed(getTenant());
        decisionTextRepository.persist(decisionTexts);
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedAllProfiles();
    }

    @SneakyThrows
    private List<StipDecisionText> getDecisionTextsToSeed(final String tenant) {
        final var toLoad = String.format(PATH_TO_CSV, tenant);
        try (final var resource = getClass().getResourceAsStream(toLoad)) {
            if (resource == null) {
                throw new RuntimeException(String.format("Could not load resource %s", toLoad));
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
                return reader.readAll()
                    .stream()
                    .map(
                        plzLine -> new StipDecisionText()
                            .setStipDecision(StipDecision.valueOf(plzLine[0]))
                            .setTitleDe(plzLine[1])
                            .setTextDe(plzLine[2])
                            .setTextFr(plzLine[3])
                    )
                    .toList();
            }
        }
    }
}
