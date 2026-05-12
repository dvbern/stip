package ch.dvbern.stip.integration.steuerdaten.domain.port;

import ch.dvbern.stip.api.config.type.PortConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.dummy.service.DummySteuerdatenAdapter;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import io.quarkus.test.component.TestConfigProperty;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusComponentTest({SteuerdatenAdapterQualifier.class, DummySteuerdatenAdapter.class})
class SteuerdatenPortFactoryTest {

    @Inject
    SteuerdatenPortFactory steuerdatenPortFactory;

    @InjectMock
    TenantService tenantService;

    @BeforeEach
    void setUp() {
        final var portConfig = Mockito.mock(PortConfig.class);
        final var steuerdatenPortConfig = Mockito.mock(PortConfig.Steuerdaten.class);
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
