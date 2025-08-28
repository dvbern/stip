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

package ch.dvbern.stip.api.nesko.service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.kind.repo.NeskoAccessRepository;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.StipendienAuskunftPort;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NeskoGetSteuerdatenAccessLogTest {
    private GesuchTrancheRepository trancheRepository;

    private NeskoGetSteuerdatenService neskoGetSteuerdatenService;
    private NeskoAccessLoggerService neskoAccessLoggerService;
    private NeskoAccessRepository neskoAccessRepository;

    private GesuchTranche gesuchTranche;
    private GesuchFormular gesuchFormular;

    GetSteuerdatenResponse getSteuerdatenResponse;

    @BeforeEach
    void setUp() {
        getSteuerdatenResponse = new GetSteuerdatenResponse();
        getSteuerdatenResponse.setSteuerdaten(new SteuerdatenType());
        var steuerbaresVermoegenKanton = new EffSatzType();
        steuerbaresVermoegenKanton.setEffektiv(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten().setSteuerbaresVermoegenKanton(steuerbaresVermoegenKanton);
        getSteuerdatenResponse.getSteuerdaten().setFahrkosten(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setKostenAuswaertigeVerpflegung(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setTotalEinkuenfte(new EffSatzType());
        getSteuerdatenResponse.getSteuerdaten().getTotalEinkuenfte().setEffektiv(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten().getTotalEinkuenfte().setSatzbestimmend(BigDecimal.ZERO);

        gesuchTranche = new GesuchTranche();
        gesuchFormular = new GesuchFormular();

        var gesuch = new Gesuch();
        gesuchTranche.setGesuch(gesuch);
        gesuch.setGesuchNummer(UUID.randomUUID().toString());
        gesuchFormular.setTranche(gesuchTranche);
        gesuchFormular.getTranche().setGesuch(gesuch);

        gesuchFormular.setTranche(gesuchTranche);
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuch.setGesuchNummer(TestUtil.getFullGesuch().getGesuchNummer());

        trancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(trancheRepository.requireById(any())).thenReturn(gesuchTranche);

        neskoAccessRepository = Mockito.mock(NeskoAccessRepository.class);
        Mockito.doNothing().when(neskoAccessRepository).persistAndFlush(any());

        neskoAccessLoggerService = Mockito.spy(new NeskoAccessLoggerService(neskoAccessRepository));
        neskoGetSteuerdatenService = Mockito.spy(new NeskoGetSteuerdatenService(neskoAccessLoggerService));

        Mockito.doReturn(Mockito.mock(StipendienAuskunftPort.class))
            .when(neskoGetSteuerdatenService)
            .getStipendianAuskunftPort(any());
    }

    @Test
    void accessLogShouldBeWrittenAtEveryNeskoGetSteuerdatenRequest() {
        // arrange (already done in setup)

        // act
        neskoGetSteuerdatenService.getSteuerdatenResponse(
            UUID.randomUUID().toString(),
            TestConstants.AHV_NUMMER_VALID_MUTTER,
            Year.now().getValue() - 1,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        // assert
        verify(neskoAccessLoggerService, times(1)).logAccess(any(), any(), any());
    }

}
