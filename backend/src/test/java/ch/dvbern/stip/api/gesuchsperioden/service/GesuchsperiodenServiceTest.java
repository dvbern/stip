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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.service.EntityReferenceMapperImpl;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchsperiodenServiceTest {

    private GesuchsperiodenService gesuchsperiodenService;
    // getGesuchsperiodeForAusbildung
    private GesuchsperiodeMapper gesuchsperiodeMapper;
    private GesuchsperiodeRepository gesuchsperiodeRepository;
    private GesuchsjahrRepository gesuchsjahrRepository;

    @BeforeEach
    void setUp() {
        gesuchsperiodeMapper = new GesuchsperiodeMapperImpl(new EntityReferenceMapperImpl());
        gesuchsperiodeRepository = Mockito.mock(GesuchsperiodeRepository.class);
        gesuchsjahrRepository = Mockito.mock(GesuchsjahrRepository.class);
        gesuchsperiodenService =
            new GesuchsperiodenService(gesuchsperiodeMapper, gesuchsperiodeRepository, gesuchsjahrRepository);
    }

    // todo: throw a 400 with message code when no active gesuchsperiode found
    @Test
    void noActiveGesuchsperiodeTest() {
        var ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));
        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(null);
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        gesuchsperiode.setGesuchsperiodeStart(LocalDate.now().plusMonths(3));
        gesuchsperiode.setAufschaltterminStart(LocalDate.now().minusMonths(3));
        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(gesuchsperiode);

        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

    @Test
    void gesuchsperiodeInStatusDraftTest() {
        var ausbildung = new Ausbildung();
        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.ENTWURF);
        gesuchsperiode.setGesuchsperiodeStart(LocalDate.now().plusMonths(3));
        gesuchsperiode.setAufschaltterminStart(LocalDate.now().minusMonths(3));

        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));
        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

    @Test
    void gesuchsperiodeInStatusArchivedTest() {
        var ausbildung = new Ausbildung();
        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.ARCHIVIERT);
        gesuchsperiode.setGesuchsperiodeStart(LocalDate.now().plusMonths(3));
        gesuchsperiode.setAufschaltterminStart(LocalDate.now().minusMonths(3));

        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(3));

        when(gesuchsperiodeRepository.findAllStartBeforeOrAt(any())).thenReturn(gesuchsperiode);
        assertThrows(
            CustomValidationsException.class,
            () -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung)
        );

        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        assertDoesNotThrow(() -> gesuchsperiodenService.getGesuchsperiodeForAusbildung(ausbildung));
    }

}
