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

package ch.dvbern.stip.integration.steuerdaten.domain.service;

import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAccess;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.repository.SteuerdatenAccessRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusComponentTest
class SteuerdatenAccessServiceTest {

    @Inject
    SteuerdatenAccessService steuerdatenAccessService;

    @InjectMock
    SteuerdatenAccessRepository steuerdatenAccessRepository;

    @Test
    void logAccess_persistsAccessLogWithCorrectValues() {
        final var adapterType = SteuerdatenAdapterType.NESKO;
        final var gesuchNr = "G-2024-001";
        final var fallNr = "F-2024-001";
        final var svn = "756.1234.5678.97";

        steuerdatenAccessService.logAccess(adapterType, fallNr, gesuchNr, svn);

        final var captor = ArgumentCaptor.forClass(SteuerdatenAccess.class);
        Mockito.verify(steuerdatenAccessRepository).persistAndFlush(captor.capture());

        final var captured = captor.getValue();
        assertThat(captured, is(notNullValue()));
        assertThat(captured.getSteuerdatenAdapterType(), is(adapterType));
        assertThat(captured.getFallNr(), is(fallNr));
        assertThat(captured.getGesuchNr(), is(gesuchNr));
        assertThat(captured.getSvNr(), is(svn));
    }

    @Test
    void logAccess_callsPersistAndFlushOnRepository() {
        steuerdatenAccessService.logAccess(SteuerdatenAdapterType.DUMMY, "G-001", "F-001", "756.0000.0000.00");

        Mockito.verify(steuerdatenAccessRepository, Mockito.times(1))
            .persistAndFlush(Mockito.any(SteuerdatenAccess.class));
    }
}
