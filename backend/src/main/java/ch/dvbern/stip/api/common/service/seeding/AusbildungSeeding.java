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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import ch.dvbern.stip.api.common.exception.CancelInvocationException;
import ch.dvbern.stip.api.config.service.ConfigService;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
            try {
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
            } catch (Exception e) {
                throw new CancelInvocationException(e);
            }
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
            final var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
            try (
                final var reader =
                    new CSVReaderHeaderAwareBuilder(new InputStreamReader(resource, StandardCharsets.UTF_8))
                        .withCSVParser(csvParser)
                        .build()
            ) {
                List<Abschluss> abschluesses = new ArrayList<>();
                Map<String, String> rowMap;
                while ((rowMap = reader.readMap()) != null) {
                    final var abschluss = new Abschluss()
                        .setBezeichnungDe(rowMap.get("bezeichnungDe"))
                        .setBezeichnungFr(rowMap.get("bezeichnungFr"))
                        .setAusbildungskategorie(Ausbildungskategorie.valueOf(rowMap.get("ausbildungskategorie")))
                        .setBildungskategorie(Bildungskategorie.valueOf(rowMap.get("bildungskategorie")))
                        .setBildungsrichtung(Bildungsrichtung.valueOf(rowMap.get("bildungsrichtung")))
                        .setBfsKategorie(Integer.valueOf(rowMap.get("bfsKategorie")))
                        .setBerufsbefaehigenderAbschluss(
                            Boolean.parseBoolean(rowMap.get("berufsbefaehigenderAbschluss"))
                        )
                        .setFerien(FerienTyp.valueOf(rowMap.get("ferien")))
                        .setZusatzfrage(
                            rowMap.get("zusatzfrage").isEmpty() ? null
                                : AbschlussZusatzfrage.valueOf(rowMap.get("zusatzfrage"))
                        )
                        .setAskForBerufsmaturitaet(Boolean.parseBoolean(rowMap.get("askForBerufsmaturitaet")));
                    abschluesses.add(abschluss);
                }
                return abschluesses;
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

            final var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
            try (
                final var reader =
                    new CSVReaderHeaderAwareBuilder(new InputStreamReader(resource, StandardCharsets.UTF_8))
                        .withCSVParser(csvParser)
                        .build()
            ) {
                List<Ausbildungsstaette> ausbildungsstaettes = new ArrayList<>();
                Map<String, String> rowMap;
                while ((rowMap = reader.readMap()) != null) {
                    final var ausbildungsstaette = new Ausbildungsstaette()
                        .setNameDe(rowMap.get("nameDe"))
                        .setNameFr(rowMap.get("nameFr"))
                        .setNummerTyp(parseAusbildungsstaetteNummerTyp(rowMap))
                        .setNummer(parseAusbildungsstaetteNummer(rowMap));
                    ausbildungsstaettes.add(ausbildungsstaette);
                }
                return ausbildungsstaettes;
            }
        }
    }

    private AusbildungsstaetteNummerTyp parseAusbildungsstaetteNummerTyp(
        final Map<String, String> ausbildungsstaetteRowMap
    ) {
        if (!ausbildungsstaetteRowMap.get("chShis").isEmpty()) {
            return AusbildungsstaetteNummerTyp.CH_SHIS;
        } else if (!ausbildungsstaetteRowMap.get("burNo").isEmpty()) {
            return AusbildungsstaetteNummerTyp.BUR_NO;
        } else if (!ausbildungsstaetteRowMap.get("ctNo").isEmpty()) {
            return AusbildungsstaetteNummerTyp.CT_NO;
        } else {
            return AusbildungsstaetteNummerTyp.OHNE_NO;
        }
    }

    private String parseAusbildungsstaetteNummer(final Map<String, String> ausbildungsstaetteRowMap) {
        if (!ausbildungsstaetteRowMap.get("chShis").isEmpty()) {
            return ausbildungsstaetteRowMap.get("chShis");
        } else if (!ausbildungsstaetteRowMap.get("burNo").isEmpty()) {
            return ausbildungsstaetteRowMap.get("burNo");
        } else if (!ausbildungsstaetteRowMap.get("ctNo").isEmpty()) {
            return ausbildungsstaetteRowMap.get("ctNo");
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
            final var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
            try (
                final var reader =
                    new CSVReaderHeaderAwareBuilder(new InputStreamReader(resource, StandardCharsets.UTF_8))
                        .withCSVParser(csvParser)
                        .build()
            ) {
                List<Ausbildungsgang> ausbildungsgangs = new ArrayList<>();
                Map<String, String> rowMap;
                while ((rowMap = reader.readMap()) != null) {
                    final var abschlussBezeichnungDe = rowMap.get("abschlussDe");
                    final var ausbildungskategorie = Ausbildungskategorie.valueOf(rowMap.get("ausbildungskategorie"));
                    final var ausbildungsstaetteNameDe = rowMap.get("ausbildungsstaetteDe");
                    final var bildungsrichtungOpt = Optional.ofNullable(rowMap.get("Bildungsrichtung (Optional)"));
                    final boolean isBildungsrichtungPresent =
                        bildungsrichtungOpt.isPresent() && !bildungsrichtungOpt.get().isEmpty();
                    var possibleMatchingAbschluesse = abschluesse.stream()
                        .filter(
                            abschluss1 -> abschluss1.getBezeichnungDe().equalsIgnoreCase(abschlussBezeichnungDe)
                            && abschluss1.getAusbildungskategorie() == ausbildungskategorie
                        )
                        .toList();
                    Optional<Abschluss> abschlussOpt = possibleMatchingAbschluesse.stream().findFirst();

                    if (isBildungsrichtungPresent) {
                        final Bildungsrichtung bildungsrichtung =
                            Bildungsrichtung.valueOf(bildungsrichtungOpt.get());
                        abschlussOpt = possibleMatchingAbschluesse.stream()
                            .filter(abschluss1 -> abschluss1.getBildungsrichtung().equals(bildungsrichtung))
                            .findFirst();
                    }

                    final var ausbildungsstaetteOpt = ausbildungsstaetten.stream()
                        .filter(
                            ausbildungsstaette1 -> ausbildungsstaette1.getNameDe()
                                .equalsIgnoreCase(ausbildungsstaetteNameDe)
                        )
                        .findFirst();

                    if (abschlussOpt.isEmpty()) {
                        handleMissingOrMismatchingEntries(
                            String.format(
                                "Could not find Abschluss %s in seeded abschluesse",
                                abschlussBezeichnungDe
                            )
                        );
                        continue;
                    }
                    if (ausbildungsstaetteOpt.isEmpty()) {
                        handleMissingOrMismatchingEntries(
                            String.format(
                                "Could not find Ausbildungsstaette %s in seeded Ausbildungsstaetten",
                                ausbildungsstaetteNameDe
                            )
                        );
                        continue;
                    }

                    final var ausbildungsgang = new Ausbildungsgang()
                        .setAbschluss(abschlussOpt.get())
                        .setAusbildungsstaette(ausbildungsstaetteOpt.get());
                    ausbildungsgangs.add(ausbildungsgang);
                }
                return ausbildungsgangs;
            }
        }
    }

    private void handleMissingOrMismatchingEntries(String message) {
        if (ConfigUtils.getProfiles().stream().anyMatch(profile -> profile.equals("dev"))) {
            throw new RuntimeException(message);
        } else {
            LOG.error(message);
        }
    }
}
