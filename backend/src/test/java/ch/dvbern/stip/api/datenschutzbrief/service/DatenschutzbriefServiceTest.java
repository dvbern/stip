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

package ch.dvbern.stip.api.datenschutzbrief.service;

import java.util.UUID;

import ch.dvbern.stip.api.datenschutzbrief.repo.DatenschutzbriefDownloadLogRepository;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.repo.ElternRepository;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.pdf.service.DatenschutzbriefPdfService;
import ch.dvbern.stip.api.util.TestUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class DatenschutzbriefServiceTest {
    @InjectMock
    private ElternRepository elternRepository;
    @InjectMock
    private GesuchTrancheRepository gesuchTrancheRepository;
    @InjectMock
    private DatenschutzbriefDownloadLogRepository datenschutzbriefDownloadLogRepository;
    @InjectMock
    private DatenschutzbriefPdfService datenschutzbriefPdfService;
    @InjectMocks
    @InjectSpy
    private DatenschutzbriefService datenschutzbriefService;

    @BeforeEach
    void setUp() {
        var gesuch = TestUtil.getFullGesuch();
        gesuch.setId(UUID.randomUUID());
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getNewestGesuchTranche().get());
        when(elternRepository.requireById(any())).thenReturn(
            gesuch.getNewestGesuchTranche().get().getGesuchFormular().getElterns().stream().toList().get(0)
        );
        Mockito.doNothing().when(datenschutzbriefDownloadLogRepository).persistAndFlush(any());
    }

    @Test
    void getDatenschutzbriefShouldAddLogs() {
        final var eltern = new Eltern();
        eltern.setId(UUID.randomUUID());
        datenschutzbriefService.getDateschutzbriefByteStream(UUID.randomUUID(), eltern);
        verify(datenschutzbriefService, times(1)).logDatenschutzbriefDownload(any(), any());
    }

}
