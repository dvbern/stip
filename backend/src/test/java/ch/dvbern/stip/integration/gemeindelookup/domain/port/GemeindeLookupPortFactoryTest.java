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

package ch.dvbern.stip.integration.gemeindelookup.domain.port;

import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.config.type.TenantPortConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.gemeindelookup.adapter.dummy.DummyGemeindeLookupAdapter;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupAdapterType;
import ch.dvbern.stip.integration.gemeindelookup.domain.qualifier.GemeindeLookupQualifier;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusComponentTest({ GemeindeLookupQualifier.class, DummyGemeindeLookupAdapter.class })
public class GemeindeLookupPortFactoryTest {

    @Inject
    GemeindeLookupPortFactory gemeindeLookupPortFactory;

    @InjectMock
    TenantService tenantService;

    @BeforeEach
    void setup() {
        final var portConfig = Mockito.mock(TenantPortConfig.class);
        final var gemeindeLookupPortConfig = Mockito.mock(TenantPortConfig.GemeindeLookup.class);
        final var tenantConfig = Mockito.mock(TenantConfig.class);

        Mockito.when(gemeindeLookupPortConfig.adapterType()).thenReturn(GemeindeLookupAdapterType.DUMMY);
        Mockito.when(portConfig.gemeindeLookup()).thenReturn(gemeindeLookupPortConfig);
        Mockito.when(tenantConfig.port()).thenReturn(portConfig);
        Mockito.when(tenantService.getConfigForCurrentTenant()).thenReturn(tenantConfig);
    }

    @Test
    void getSteuerdatenPort_returnsPortForConfiguredAdapterType() {
        final var port = gemeindeLookupPortFactory.getGemeindeLookupPort();

        assertThat(port, notNullValue());
        assertThat(port, instanceOf(DummyGemeindeLookupAdapter.class));
    }
}
