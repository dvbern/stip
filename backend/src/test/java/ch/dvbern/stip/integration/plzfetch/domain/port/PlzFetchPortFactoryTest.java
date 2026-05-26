package ch.dvbern.stip.integration.plzfetch.domain.port;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.config.type.TenantPortConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.plzfetch.adapter.dummy.service.DummyPlzFetchAdapter;
import ch.dvbern.stip.integration.plzfetch.domain.model.PlzFetchAdapterType;
import ch.dvbern.stip.integration.plzfetch.domain.qualifier.PlzFetchQualifier;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;

@QuarkusComponentTest({ PlzFetchQualifier.class, DummyPlzFetchAdapter.class })
class PlzFetchPortFactoryTest{

    @Inject
    PlzFetchPortFactory plzFetchPortFactory;

    @InjectMock
    TenantService tenantService;

    @BeforeEach
    void setUp() {
        final var portConfig = Mockito.mock(TenantPortConfig.class);
        final var plzFetchPortConfig = Mockito.mock(TenantPortConfig.PlzFetch.class);
        final var tenantConfig = Mockito.mock(TenantConfig.class);

        Mockito.when(plzFetchPortConfig.adapterType()).thenReturn(PlzFetchAdapterType.DUMMY);
        Mockito.when(portConfig.plzFetch()).thenReturn(plzFetchPortConfig);
        Mockito.when(tenantConfig.port()).thenReturn(portConfig);
        Mockito.when(tenantService.getConfigForCurrentTenant()).thenReturn(tenantConfig);
    }

    @Test
    void getPlzFetchAdpater_returnsAdapterForConfiguredAdapterType() {
        final var result = plzFetchPortFactory.getPlzFetchAdapter();

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(DummyPlzFetchAdapter.class));
    }
}
