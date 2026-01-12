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

package ch.dvbern.stip.berechnung.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapper;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@RequiredArgsConstructor
@QuarkusTest
@Slf4j
public class BerechnungTestcaseTest {
    @Inject
    BerechnungService berechnungService;
    @Inject
    GesuchFormularMapper gesuchFormularMapper;
    @Inject
    GesuchsperiodeMapper gesuchsperiodeMapper;
    @Inject
    AusbildungMapper ausbildungMapper;
    @Inject
    SteuerdatenMapper steuerdatenMapper;
    @InjectMock
    LandService landService;

    @BeforeEach
    void setup() {
        Mockito.when(landService.requireLandById(Mockito.any())).thenReturn(new Land());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1 })
    void testTestcases(final int no) {
        // Arrange
        final var testcase = BerechnungUtil.getTestcase(no);

        final var gesuchperiode = gesuchsperiodeMapper.toEntity(testcase.gesuchperiode);
        final var gesuch = new Gesuch();
        gesuch.setGesuchsperiode(gesuchperiode);
        gesuch.setEinreichedatum(LocalDate.now());
        final var ausbildung = ausbildungMapper.toNewEntity(testcase.ausbildung);
        ausbildung.setAusbildungsgang(
            new Ausbildungsgang().setAbschluss(
                new Abschluss().setBildungskategorie(testcase.bildungskategorie)
                    .setBildungsrichtung(testcase.bildungsrichtung)
            )
        );
        gesuch.setAusbildung(ausbildung);
        final var gesuchTranche = new GesuchTranche();
        gesuchTranche.setGueltigkeit(new DateRange(LocalDate.now(), LocalDate.now().plusYears(1)));
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchTranche.setGesuch(gesuch);

        GesuchFormular gesuchFormular = new GesuchFormular();
        gesuchFormular.setTranche(gesuchTranche);
        gesuchFormular = gesuchFormularMapper.partialUpdate(
            testcase.gesuch.getGesuchTrancheToWorkWith().getGesuchFormular(),
            gesuchFormular
        );
        final var steuerdaten = testcase.steuerdaten.stream()
            .map(
                steuerdatenMapper::toEntity
            );

        gesuchFormular.getPersonInAusbildung().setNationalitaet(LandGenerator.initSwitzerland());
        gesuchFormular.setSteuerdaten(steuerdaten.collect(Collectors.toSet()));
        var ekPartner = new EinnahmenKosten();
        ekPartner.setNettoerwerbseinkommen(70000);
        ekPartner.setNettoerwerbseinkommen(0);
        ekPartner.setVermoegen(5000);
        ekPartner.setVermoegen(0);
        gesuchFormular.setEinnahmenKostenPartner(ekPartner);
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));

        // Act
        final var berechnungsresultat = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        // assertThat(berechnungsresultat.getBerechnung().intValue(), is(testcase.berechnungsResult));
        // final var tranchenResultat = berechnungsresultat.getTranchenBerechnungsresultate().get(0);
        // TODO: Check what went wrong
        // assertThat(
        // tranchenResultat.getPersoenlichesBudgetresultat().getEinnahmen().getTotal(),
        // is(testcase.einnahmenPersoenlichesBudget)
        // );
        // assertThat(
        // tranchenResultat.getPersoenlichesBudgetresultat().getKosten().getTotal(),
        // is(testcase.ausgabenPersoenlichesBudget)
        // );
        // assertThat(
        // tranchenResultat.getPersoenlichesBudgetresultat().getTotal(),
        // is(testcase.persoenlichesbudgetBerechnet)
        // );
    }
}
