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

package ch.dvbern.stip.integration.steuerdaten.domain.port;

import java.util.Optional;

import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.config.type.TenantPortConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.dummy.service.DummySteuerdatenAdapter;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusComponentTest({ SteuerdatenAdapterQualifier.class, DummySteuerdatenAdapter.class })
class SteuerdatenPortFactoryTest {

    @Inject
    SteuerdatenPortFactory steuerdatenPortFactory;

    @InjectMock
    TenantService tenantService;

    @BeforeEach
    void setUp() {
        final var portConfig = Mockito.mock(TenantPortConfig.class);
        final var steuerdatenPortConfig = Mockito.mock(TenantPortConfig.Steuerdaten.class);
        final var tenantConfig = Mockito.mock(TenantConfig.class);

        Mockito.when(steuerdatenPortConfig.adapterType()).thenReturn(Optional.of(SteuerdatenAdapterType.DUMMY));
        Mockito.when(portConfig.steuerdaten()).thenReturn(steuerdatenPortConfig);
        Mockito.when(tenantConfig.port()).thenReturn(portConfig);
        Mockito.when(tenantService.getConfigForCurrentTenant()).thenReturn(tenantConfig);
    }

    @Test
    void getSteuerdatenPort_returnsPortForConfiguredAdapterType() {
        final var result = steuerdatenPortFactory.getSteuerdatenPort();

        assertThat(result, is(notNullValue()));
        assertThat(result, is(instanceOf(DummySteuerdatenAdapter.class)));
    }
}
