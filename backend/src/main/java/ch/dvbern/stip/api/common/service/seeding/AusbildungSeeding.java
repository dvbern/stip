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

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AbschlussRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.config.service.ConfigService;
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
public class AusbildungSeeding extends Seeder {
    private final AbschlussRepository abschlussRepository;
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final ConfigService configService;

    private static final String PATH_TO_CSV_ABSCHLUSS = "/seeding/ausbildung/abschluss.csv";
    private static final String PATH_TO_CSV_AUSBILDUNGSSTAETTE = "/seeding/ausbildung/ausbildungsstaette.csv";
    private static final String PATH_TO_CSV_AUSBILDUNGSGANG = "/seeding/ausbildung/ausbildungsgang.csv";

    @Override
    protected void seed() {
        if (ausbildungsstaetteRepository.count() == 0) {
            LOG.info("Seeding Abschluss, Ausbildungsstaette and Ausbildungsgang");

            final var abschluesse = getAbschluesseToSeed();
            abschlussRepository.persist(abschluesse);
            abschlussRepository.flush();

            final var ausbildungsstaetten = getAusbildungsstaettenToSeed();
            ausbildungsstaetteRepository.persist(ausbildungsstaetten);
            ausbildungsstaetteRepository.flush();

            final var ausbildunggaenge = getAusbildungsgaengeToSeed(
                abschluesse,
                ausbildungsstaetten
            );

            ausbildungsgangRepository.persist(ausbildunggaenge);
            ausbildungsgangRepository.flush();
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    @SneakyThrows
    private List<Abschluss> getAbschluesseToSeed() {
        try (final var resource = getClass().getResourceAsStream(PATH_TO_CSV_ABSCHLUSS)) {
            if (resource == null) {
                throw new FileNotFoundException("Could not load CSV to seed abschluesse: " + PATH_TO_CSV_ABSCHLUSS);
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
                        abschlussLine -> new Abschluss()
                            .setBezeichnungDe(abschlussLine[0])
                            .setBezeichnungFr(abschlussLine[1])
                            .setAusbildungskategorie(Ausbildungskategorie.valueOf(abschlussLine[2]))
                            .setBildungskategorie(Bildungskategorie.valueOf(abschlussLine[3]))
                            .setBildungsrichtung(Bildungsrichtung.valueOf(abschlussLine[4]))
                            .setBfsKategorie(Integer.valueOf(abschlussLine[5]))
                            .setBerufsbefaehigenderAbschluss(Boolean.valueOf(abschlussLine[6]))
                            .setFerien(FerienTyp.valueOf(abschlussLine[7]))
                            .setZusatzfrage(
                                abschlussLine[8].isEmpty() ? null : AbschlussZusatzfrage.valueOf(abschlussLine[8])
                            )
                            .setAskForBerufsmaturitaet(Boolean.valueOf(abschlussLine[9]))
                    )
                    .toList();
            }
        }
    }

    @SneakyThrows
    private List<Ausbildungsstaette> getAusbildungsstaettenToSeed() {
        try (final var resource = getClass().getResourceAsStream(PATH_TO_CSV_AUSBILDUNGSSTAETTE)) {
            if (resource == null) {
                throw new FileNotFoundException(
                    "Could not load CSV to seed ausbildungsstaette: " + PATH_TO_CSV_AUSBILDUNGSSTAETTE
                );
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
                        ausbildungsstaetteLine -> new Ausbildungsstaette()
                            .setNameDe(ausbildungsstaetteLine[0])
                            .setNameFr(ausbildungsstaetteLine[1])
                            .setNummerTyp(parseAusbildungsstaetteNummerTyp(ausbildungsstaetteLine))
                            .setNummer(parseAusbildungsstaetteNummer(ausbildungsstaetteLine))
                    )
                    .toList();
            }
        }
    }

    private AusbildungsstaetteNummerTyp parseAusbildungsstaetteNummerTyp(final String[] ausbildungsstaetteLine) {
        if (!ausbildungsstaetteLine[2].isEmpty()) {
            return AusbildungsstaetteNummerTyp.CH_SHIS;
        } else if (!ausbildungsstaetteLine[3].isEmpty()) {
            return AusbildungsstaetteNummerTyp.BUR_NO;
        } else if (!ausbildungsstaetteLine[4].isEmpty()) {
            return AusbildungsstaetteNummerTyp.CT_NO;
        } else {
            return null;
        }
    }

    private String parseAusbildungsstaetteNummer(final String[] ausbildungsstaetteLine) {
        if (!ausbildungsstaetteLine[2].isEmpty()) {
            return ausbildungsstaetteLine[2];
        } else if (!ausbildungsstaetteLine[3].isEmpty()) {
            return ausbildungsstaetteLine[3];
        } else if (!ausbildungsstaetteLine[4].isEmpty()) {
            return ausbildungsstaetteLine[4];
        } else {
            return null;
        }
    }

    @SneakyThrows
    private List<Ausbildungsgang> getAusbildungsgaengeToSeed(
        final List<Abschluss> abschluesse,
        final List<Ausbildungsstaette> ausbildungsstaetten
    ) {
        try (final var resource = getClass().getResourceAsStream(PATH_TO_CSV_AUSBILDUNGSGANG)) {
            if (resource == null) {
                throw new FileNotFoundException(
                    "Could not load CSV to seed ausbildungsgang: " + PATH_TO_CSV_AUSBILDUNGSGANG
                );
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
                        ausbildungsgangLine -> {
                            final var abschlussBezeichnungDe = ausbildungsgangLine[0];
                            final var ausbildungskategorie = Ausbildungskategorie.valueOf(ausbildungsgangLine[1]);
                            final var ausbildungsstaetteNameDe = ausbildungsgangLine[2];
                            final var abschluss = abschluesse.stream()
                                .filter(
                                    abschluss1 -> abschluss1.getBezeichnungDe().equalsIgnoreCase(abschlussBezeichnungDe)
                                    && abschluss1.getAusbildungskategorie() == ausbildungskategorie
                                )
                                .findFirst()
                                .get();
                            final var ausbildungsstaette = ausbildungsstaetten.stream()
                                .filter(
                                    ausbildungsstaette1 -> ausbildungsstaette1.getNameDe()
                                        .equalsIgnoreCase(ausbildungsstaetteNameDe)
                                )
                                .findFirst()
                                .get();
                            return new Ausbildungsgang()
                                .setAbschluss(abschluss)
                                .setAusbildungsstaette(ausbildungsstaette);
                        }
                    )
                    .toList();
            }
        }
    }
}
