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

package ch.dvbern.stip.integration.steuerdaten.adapter.nesko.service;

import java.math.BigDecimal;
import java.util.Map;

import ch.dvbern.stip.api.config.type.TenantAdapterConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.BusinessFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdaten;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftPort;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.VeranlagungsStatusType;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import ch.dvbern.stip.integration.steuerdaten.domain.service.SteuerdatenAccessService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.soap.SOAPFaultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusComponentTest
class NeskoSteuerdatenAdapterTest {

    private static final String DEFAULT_SVN = "756.1234.5678.97";
    private static final int DEFAULT_STEUERJAHR = 2023;
    private static final SteuerdatenTyp DEFAULT_STEUERDATEN_TYP = SteuerdatenTyp.FAMILIE;
    private static final String DEFAULT_FALL_NR = "F-001";
    private static final String DEFAULT_GESUCH_NR = "G-001";
    private static final BigDecimal DEFAULT_EFF_SATZ_VALUE = BigDecimal.valueOf(1000);

    @Inject
    @SteuerdatenAdapterQualifier(SteuerdatenAdapterType.NESKO)
    NeskoSteuerdatenAdapter neskoSteuerdatenAdapter;

    @InjectMock
    StipendienAuskunftPortFactory stipendienAuskunftPortFactory;

    @InjectMock
    SteuerdatenAccessService steuerdatenAccessService;

    @InjectMock
    TenantService tenantService;

    @InjectMock
    GetSteuerdatenResponse getSteuerdatenResponseMock;

    @InjectMock
    private StipendienAuskunftPort portMock;

    @InjectMock
    private TenantAdapterConfig.SteuerdatenAdapter adapterConfigMock;

    @BeforeEach
    void setUp() {
        final var adapterConfig = mock(TenantAdapterConfig.class);
        when(adapterConfig.steuerdaten()).thenReturn(Map.of(SteuerdatenAdapterType.NESKO, adapterConfigMock));

        final var tenantConfig = mock(TenantConfig.class);
        when(tenantConfig.adapter()).thenReturn(adapterConfig);

        when(tenantService.getConfigForCurrentTenant()).thenReturn(tenantConfig);
        when(stipendienAuskunftPortFactory.create(adapterConfigMock)).thenReturn(portMock);
    }

    @Test
    void getSteuerdaten_happyPath_returnsPortData() throws Exception {
        final var effSatz = new EffSatzType();
        effSatz.setEffektiv(DEFAULT_EFF_SATZ_VALUE);
        effSatz.setSatzbestimmend(DEFAULT_EFF_SATZ_VALUE);

        final var steuerdaten = new SteuerdatenType();
        steuerdaten.setStatusVeranlagung(VeranlagungsStatusType.RECHTSKRAEFTIG);
        steuerdaten.setTotalEinkuenfte(effSatz);
        steuerdaten.setSteuerbaresVermoegenKanton(effSatz);

        final var response = new GetSteuerdatenResponse();
        response.setSteuerjahr(DEFAULT_STEUERJAHR);
        response.setSteuerdaten(steuerdaten);
        when(portMock.getSteuerdaten(any())).thenReturn(response);

        final var result = neskoSteuerdatenAdapter.getSteuerdaten(
            DEFAULT_SVN,
            DEFAULT_STEUERJAHR,
            DEFAULT_STEUERDATEN_TYP,
            DEFAULT_FALL_NR,
            DEFAULT_GESUCH_NR
        );

        assertThat(result, is(notNullValue()));
        verify(steuerdatenAccessService)
            .logAccess(SteuerdatenAdapterType.NESKO, DEFAULT_FALL_NR, DEFAULT_GESUCH_NR, DEFAULT_SVN);
    }

    @Test
    void getSteuerdaten_portReturnsNull_returnsNull() throws Exception {
        when(portMock.getSteuerdaten(any())).thenReturn(null);

        final var result = neskoSteuerdatenAdapter.getSteuerdaten(
            DEFAULT_SVN, DEFAULT_STEUERJAHR, DEFAULT_STEUERDATEN_TYP, DEFAULT_FALL_NR, DEFAULT_GESUCH_NR
        );

        assertThat(result, is(nullValue()));
    }

    @Test
    void getSteuerdaten_svnStripping_removesDotsBeforeCall() throws Exception {
        when(portMock.getSteuerdaten(any())).thenReturn(null);

        neskoSteuerdatenAdapter.getSteuerdaten(DEFAULT_SVN, DEFAULT_STEUERJAHR, DEFAULT_STEUERDATEN_TYP, DEFAULT_FALL_NR, DEFAULT_GESUCH_NR);

        final var captor = org.mockito.ArgumentCaptor.forClass(
            GetSteuerdaten.class
        );
        verify(portMock).getSteuerdaten(captor.capture());
        assertThat(captor.getValue().getSozialversicherungsnummer(), is(Long.valueOf(DEFAULT_SVN.replace(".", ""))));
        assertThat(captor.getValue().getSteuerjahr(), is(DEFAULT_STEUERJAHR));
    }

    @Test
    void getSteuerdaten_soapFaultException_throwsInternalServerError() throws Exception {
        final var soapEx = mock(SOAPFaultException.class);
        when(soapEx.getMessage()).thenReturn("Some unexpected SOAP error");
        when(portMock.getSteuerdaten(any())).thenThrow(soapEx);

        assertThrows(
            InternalServerErrorException.class,
            () -> neskoSteuerdatenAdapter.getSteuerdaten(
                DEFAULT_SVN,
                DEFAULT_STEUERJAHR,
                DEFAULT_STEUERDATEN_TYP,
                DEFAULT_FALL_NR,
                DEFAULT_GESUCH_NR
            )
        );
    }

    @Test
    void getSteuerdaten_businessFaultException_throwsInternalServerError() throws Exception {
        final var businessFault = mock(BusinessFault.class);
        when(businessFault.getMessage()).thenReturn("Some unknown business fault");
        when(portMock.getSteuerdaten(any())).thenThrow(businessFault);

        assertThrows(
            InternalServerErrorException.class,
            () -> neskoSteuerdatenAdapter.getSteuerdaten(
                DEFAULT_SVN,
                DEFAULT_STEUERJAHR,
                DEFAULT_STEUERDATEN_TYP,
                DEFAULT_FALL_NR,
                DEFAULT_GESUCH_NR
            )
        );
    }

    @Test
    void getSteuerdaten_logsAccessBeforeCallingPort() throws Exception {
        when(portMock.getSteuerdaten(any())).thenReturn(null);
        final var order = Mockito.inOrder(steuerdatenAccessService, portMock);

        neskoSteuerdatenAdapter.getSteuerdaten("756.0000.0000.00", DEFAULT_STEUERJAHR, SteuerdatenTyp.VATER, "F-002", "G-002");

        order.verify(steuerdatenAccessService).logAccess(any(), any(), any(), any());
        order.verify(portMock).getSteuerdaten(any());
    }
}
