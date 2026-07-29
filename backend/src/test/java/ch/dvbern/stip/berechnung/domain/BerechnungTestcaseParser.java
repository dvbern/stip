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

package ch.dvbern.stip.berechnung.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.common.service.EntityCopyMapper;
import ch.dvbern.stip.api.common.service.seeding.GesuchsperiodeSeeding;
import ch.dvbern.stip.api.common.service.seeding.GesuchsperiodeSeeding.Season;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.repo.DemoDataAbschlussRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataAusbildungsgangRepository;
import ch.dvbern.stip.api.demo.service.GenerateDemoDataService;
import ch.dvbern.stip.api.demo.util.ParseDemoDataUtil;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchNummerService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.BernBerechnungAdapterV1_0;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class BerechnungTestcaseParser {
    static final String OUTPUT_DIR = "testcase/serialized-testcases-2";

    LandRepository landRepository = Mockito.mock(LandRepository.class);

    DemoDataAusbildungsgangRepository demoDataAusbildungsgangRepository =
        Mockito.mock(DemoDataAusbildungsgangRepository.class);

    DemoDataAbschlussRepository demoDataAbschlussRepository =
        Mockito.mock(DemoDataAbschlussRepository.class);

    GesuchsperiodenService gesuchsperiodenService = Mockito.mock(GesuchsperiodenService.class);

    GesuchNummerService gesuchNummerService = Mockito.mock(GesuchNummerService.class);

    GenerateDemoDataService generateDemoDataService = new GenerateDemoDataService(
        demoDataAusbildungsgangRepository,
        demoDataAbschlussRepository,
        null,
        null,
        Mappers.getMapper(EntityCopyMapper.class),
        null,
        null,
        null,
        landRepository,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        gesuchsperiodenService,
        gesuchNummerService,
        null,
        null
    );

    private final BernBerechnungAdapterV1_0 berechnungAdapter = new BernBerechnungAdapterV1_0(
        Mappers.getMapper(
            BerechnungsStammdatenMapper.class
        )
    );

    // @ParameterizedTest
    // @ArgumentsSource(BerechnungTestcaseParserArgumentProvider.class)
    void parseAndSafeTestcases(final DemoData demoData) {
        Mockito.when(landRepository.getByIso2code(ArgumentMatchers.any())).thenReturn(Optional.of(new Land()));
        Mockito.when(demoDataAusbildungsgangRepository.requireAusbildungsgangByDemoData(ArgumentMatchers.any()))
            .thenAnswer(
                invocation -> {
                    final DemoAusbildungDto input =
                        (DemoAusbildungDto) Arrays.stream(invocation.getArguments()).findFirst().get();
                    if (
                        input.getAusbildungsgang().contains("Bachelor") || input.getAusbildungsgang().contains("Master")
                    ) {
                        return new Ausbildungsgang()
                            .setAbschluss(new Abschluss().setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B));
                    }
                    return new Ausbildungsgang()
                        .setAbschluss(new Abschluss().setBildungskategorie(Bildungskategorie.SEKUNDARSTUFE_II));
                }
            );

        Mockito.when(demoDataAbschlussRepository.requireByAbschlussName(ArgumentMatchers.any()))
            .thenAnswer(
                invocation -> {
                    final String input =
                        (String) Arrays.stream(invocation.getArguments()).findFirst().get();
                    if (input.contains("Bachelor") || input.contains("Master")) {
                        return new Abschluss().setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B);
                    }
                    return new Abschluss().setBildungskategorie(Bildungskategorie.SEKUNDARSTUFE_II);
                }
            );

        GesuchsperiodeSeeding gesuchsperiodeSeeding = new GesuchsperiodeSeeding(
            null, null, null
        );
        var gesuchsperiode = gesuchsperiodeSeeding.getPeriodeForSeeding(
            "Herbst",
            "Automne",
            new Gesuchsjahr().setTechnischesJahr(LocalDate.now().getYear()),
            Season.FALL,
            GueltigkeitStatus.PUBLIZIERT,
            LocalDate.of(LocalDate.now().getYear(), 7, 1),
            LocalDate.of(LocalDate.now().getYear() + 1, 6, 30),
            LocalDate.of(LocalDate.now().getYear(), 7, 15),
            LocalDate.of(LocalDate.now().getYear(), 12, 31),
            LocalDate.of(LocalDate.now().getYear() + 1, 3, 31),
            LocalDate.of(LocalDate.now().getYear(), 12, 31)
        );

        Mockito.when(gesuchsperiodenService.getGesuchsperiodeForAusbildung(ArgumentMatchers.any()))
            .thenReturn(Pair.of(gesuchsperiode, null));

        Mockito.when(gesuchNummerService.createGesuchNummer(ArgumentMatchers.any())).thenReturn("");

        final Path outputDir = Path.of(OUTPUT_DIR);
        final Path outputFile = outputDir.resolve("testcase-%s.json".formatted(demoData.getTestFall()));

        final Gesuch gesuch = generateDemoDataService.createEinreichableGesuch(demoData, new Fall());

        try {
            Files.createDirectories(outputDir);

            final ObjectMapper mapper = createObjectMapper();

            final GesuchTranche gesuchTranche = gesuch.getLatestGesuchTranche();
            final GesuchFormular gesuchFormular = gesuchTranche.getGesuchFormular();

            gesuch.setEinreichedatum(LocalDate.parse(demoData.getGesuchseingang(), ParseDemoDataUtil.dmyFormatter));

            final BerechnungsresultatDto berechnungsresultat = berechnungAdapter.getBerechnungsresultat(gesuch);

            final ObjectNode root = mapper.createObjectNode();

            root.put("testcaseId", demoData.getTestFall());
            root.set("gesuch", mapper.valueToTree(gesuch));
            root.set("gesuchTranche", mapper.valueToTree(gesuchTranche));
            root.set("gesuchFormular", mapper.valueToTree(gesuchFormular));
            root.set("ausbildung", mapper.valueToTree(gesuch.getAusbildung()));
            root.set("gesuchsperiode", mapper.valueToTree(gesuch.getGesuchsperiode()));
            root.set("personInAusbildung", mapper.valueToTree(gesuchFormular.getPersonInAusbildung()));
            root.set("familiensituation", mapper.valueToTree(gesuchFormular.getFamiliensituation()));
            root.set("elterns", mapper.valueToTree(gesuchFormular.getElterns()));
            root.set("geschwisters", mapper.valueToTree(gesuchFormular.getGeschwisters()));
            root.set("kinds", mapper.valueToTree(gesuchFormular.getKinds()));
            root.set("lebenslaufItems", mapper.valueToTree(gesuchFormular.getLebenslaufItems()));
            root.set("einnahmenKosten", mapper.valueToTree(gesuchFormular.getEinnahmenKosten()));
            root.set("einnahmenKostenPartner", mapper.valueToTree(gesuchFormular.getEinnahmenKostenPartner()));
            root.set("partner", mapper.valueToTree(gesuchFormular.getPartner()));
            root.set("steuererklaerung", mapper.valueToTree(gesuchFormular.getSteuererklaerung()));
            root.set("steuerdaten", mapper.valueToTree(gesuchFormular.getSteuerdaten()));

            root.put("stipendien", berechnungsresultat.getBerechnungStipendium());
            root.put(
                "darlehen",
                berechnungsresultat.getBerechnungDarlehen() == null ? 0 : berechnungsresultat.getBerechnungDarlehen()
            );

            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile.toFile(), root);
        } catch (IOException e) {
            throw new IllegalStateException("Could not serialize gesuch to " + outputFile, e);
        }

    }

    private ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mapper.addMixIn(GesuchFormular.class, GesuchFormularMixin.class);
        mapper.addMixIn(GesuchTranche.class, GesuchTrancheMixin.class);
        mapper.addMixIn(Fall.class, FallMixin.class);
        mapper.addMixIn(Ausbildung.class, AusbildungMixin.class);
        mapper.addMixIn(Gesuch.class, GesuchMixin.class);

        return mapper;
    }

    @JsonIgnoreProperties(
        {
            "tranche",
            "ausbildung"
        }
    )
    private abstract static class GesuchFormularMixin {
    }

    @JsonIgnoreProperties(
        {
            "gesuch",
            "gesuchFormular"
        }
    )
    private abstract static class GesuchTrancheMixin {
    }

    @JsonIgnoreProperties(
        {
            "gesuchs",
            "latestGesuch"
        }
    )
    private abstract static class FallMixin {
    }

    @JsonIgnoreProperties(
        {
            "latestGesuch"
        }
    )
    private abstract static class AusbildungMixin {
    }

    @JsonIgnoreProperties(
        {
            "aenderungZuUeberpruefen",
            "aenderungs",
            "latestGesuchTranche",
            "earliestGesuchTranche",
            "newestGesuchTranche",
            "akzeptierteAenderungs",
            "tranchenTranchen",
            "aenderungZuUeberpruefen",
            "gesuchGueltigkeitAb",
            "gesuchGueltigkeitBis",
            "allPendingDatenschutschbriefsForMassendruck",
            "allPendingVerfuegungsForMassendruck",
            "firstVerfuegung",
            "erstgesuch",
            "neverBeenVerfuegt",
        }
    )
    private abstract static class GesuchMixin {
    }

}
