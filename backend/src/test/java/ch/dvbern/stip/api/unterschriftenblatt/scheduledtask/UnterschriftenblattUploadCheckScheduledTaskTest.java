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

package ch.dvbern.stip.api.unterschriftenblatt.scheduledtask;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.common.service.seeding.GesuchTestSeeding;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchhistory.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@QuarkusTest
class UnterschriftenblattUploadCheckScheduledTaskTest {

    @Inject
    UnterschriftenblattUploadCheckScheduledTask scheduledTask;

    @InjectMock
    GesuchRepository gesuchRepository;

    @InjectMock
    GesuchHistoryRepository gesuchHistoryRepository;

    @InjectMock
    BuchhaltungRepository buchhaltungRepository;

    @InjectMock
    BerechnungService berechnungService;

    @InjectMock
    StatusprotokollService statusprotokollService;

    Gesuch gesuch;

    @BeforeEach
    void setUp() {
        // arrange
        Mockito.doNothing().when(buchhaltungRepository).persistAndFlush(any());

        var berechnungsresultatDto = new BerechnungsresultatDto();
        berechnungsresultatDto.setBerechnungTotal(0);
        berechnungsresultatDto.setYear(2025);
        var tranchenBerechnungsblattDto = new TranchenBerechnungsresultatDto();
        tranchenBerechnungsblattDto.setBerechnungAnteilTotal(0);
        berechnungsresultatDto.setTranchenBerechnungsresultate(List.of(tranchenBerechnungsblattDto));

        when(berechnungService.getBerechnungsresultatFromGesuch(any(), anyInt(), anyInt()))
            .thenCallRealMethod();

        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT);
        var verfuegung = new Verfuegung();
        verfuegung.setGesuch(gesuch);
        gesuch.setEinreichedatum(LocalDate.now());
        gesuch.setVerfuegungs(List.of(verfuegung));
        when(gesuchRepository.getAllWartenAufUnterschriftenblatt()).thenReturn(List.of(gesuch));
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        var gesuchsjahr = new Gesuchsjahr();
        gesuchsjahr.setTechnischesJahr(2025);
        var gesuchperiode = GesuchTestSeeding.getGesuchsperiode(gesuchsjahr);
        gesuch.setGesuchsperiode(gesuchperiode);

        var ausbildung = new Ausbildung();
        var abschluss = new Abschluss();
        abschluss.setBezeichnungDe("test");
        abschluss.setBezeichnungFr("test");
        abschluss.setAusbildungskategorie(Ausbildungskategorie.BERUFS_UND_HOEHERE_FACHSCHULE);
        var ausbildungsgang = new Ausbildungsgang();
        ausbildungsgang.setAbschluss(abschluss);
        ausbildung.setAusbildungsgang(ausbildungsgang);

        var ausbildungsstaette = new Ausbildungsstaette();
        ausbildungsstaette.setNameDe("test");
        ausbildungsstaette.setNameFr("test");
        ausbildungsgang.setAusbildungsstaette(ausbildungsstaette);

        var fall = new Fall();
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);

        var zuordnung = new Zuordnung();
        zuordnung.setFall(fall);
        var sachbearbeiter = new Sachbearbeiter();
        sachbearbeiter.setNachname("Muster");
        sachbearbeiter.setVorname("Max");
        sachbearbeiter.setEmail("test");
        sachbearbeiter.setTelefonnummer("031");
        sachbearbeiter.setFunktionDe("test");
        sachbearbeiter.setFunktionFr("test");
        zuordnung.setSachbearbeiter(sachbearbeiter);
        fall.setSachbearbeiterZuordnung(zuordnung);

        when(gesuchHistoryRepository.getWhereStatusChangeHappenedBefore(any(), any(), any()))
            .thenReturn(Stream.of(gesuch));
    }

    @Test
    void automaticChangeOfGesuchStatusToDruckbereit_shouldWork() {
        // act & assert
        assertDoesNotThrow(() -> scheduledTask.run());
        // todo KSTIP-2663 move call of addBerechnungsblattToDocument to another state transtition
        /*
         * try {
         * // verify that correct boolean value (addAllBerechnungsblaetter = false) has been passed
         * verify(berechnungsblattService, times(1))
         * .addBerechnungsblattToDocument(any(), any(), any(), org.mockito.ArgumentMatchers.eq(false));
         * } catch (IOException e) {
         * fail();
         * }
         *
         */
        // verify that the flag has been set to true & that gesuch is in correct state
        // todo KSTIP-2663 move gesuch.isVerfuegt() to another state transtition
        // assertThat(gesuch.isVerfuegt(), is(true));
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.VERFUEGUNG_DRUCKBEREIT));
    }

}
