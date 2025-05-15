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

package ch.dvbern.stip.api.gesuchsperioden.service;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchsperiodenServiceTest {
    private GesuchsperiodenService gesuchsperiodenService;
    private GesuchsperiodeRepository gesuchsperiodeRepository;

    @BeforeEach
    void setUp() {
        gesuchsperiodeRepository = Mockito.mock(GesuchsperiodeRepository.class);
        gesuchsperiodenService =
            new GesuchsperiodenService(null, gesuchsperiodeRepository, null);
    }

    @Test
    void noActiveGesuchsperiodeTest() {
        // arrange
        var ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));
        when(gesuchsperiodeRepository.findStartBeforeOrAt(any())).thenReturn(null);
        // act & assert
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        // arrange
        Gesuchsperiode gesuchsperiode = getValidGesuchsperiode(GueltigkeitStatus.PUBLIZIERT);
        when(gesuchsperiodeRepository.findStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        // act & assert
        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

    @Test
    void gesuchsperiodeInStatusDraftTest() {
        // arrange
        var ausbildung = new Ausbildung();
        Gesuchsperiode gesuchsperiode = getValidGesuchsperiode(GueltigkeitStatus.ENTWURF);

        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));
        when(gesuchsperiodeRepository.findStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        // act & assert
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        // arrange
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        // act & assert
        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

    @Test
    void gesuchsperiodeInStatusArchivedTest() {
        // arrange
        var ausbildung = new Ausbildung();
        Gesuchsperiode gesuchsperiode = getValidGesuchsperiode(GueltigkeitStatus.ARCHIVIERT);

        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));
        when(gesuchsperiodeRepository.findStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        // arrange & assert
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        // arrange
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        // act & assert
        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

    @Test
    void testSetOutdatedGesuchsperiodenToArchiviert(){
        // arrange
        when(gesuchsperiodeRepository.findAllPubliziertStoppBefore(any())).thenReturn(List.of());
        // act & assert
        assertDoesNotThrow(() -> gesuchsperiodenService.setOutdatedGesuchsperiodenToArchiviert());

        // arrange
        Gesuchsperiode outdatedGesuchsperiode = getValidGesuchsperiode(GueltigkeitStatus.PUBLIZIERT);
        outdatedGesuchsperiode.setGesuchsperiodeStopp(LocalDate.now().minusDays(1));

        when(gesuchsperiodeRepository.findAllPubliziertStoppBefore(any())).thenReturn(List.of(outdatedGesuchsperiode));
        // act
        gesuchsperiodenService.setOutdatedGesuchsperiodenToArchiviert();
        // assert
        assertThat(outdatedGesuchsperiode.getGueltigkeitStatus(), is(GueltigkeitStatus.ARCHIVIERT));

        // arrange
        Gesuchsperiode gesuchsperiode = getValidGesuchsperiode(GueltigkeitStatus.PUBLIZIERT);
        gesuchsperiode.setGesuchsperiodeStopp(LocalDate.now());
        // act
        gesuchsperiodenService.setOutdatedGesuchsperiodenToArchiviert();
        // assert
        assertThat(gesuchsperiode.getGueltigkeitStatus(), is(GueltigkeitStatus.PUBLIZIERT));
    }

    private Gesuchsperiode getValidGesuchsperiode(final GueltigkeitStatus gueltigkeitStatus) {
        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setGueltigkeitStatus(gueltigkeitStatus);
        gesuchsperiode.setGesuchsperiodeStart(LocalDate.now().plusMonths(3));
        gesuchsperiode.setAufschaltterminStart(LocalDate.now().minusMonths(3));
        return gesuchsperiode;
    }
}
