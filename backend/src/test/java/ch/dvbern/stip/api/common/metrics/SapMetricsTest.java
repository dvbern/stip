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

package ch.dvbern.stip.api.common.metrics;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class SapMetricsTest {
    private SimpleMeterRegistry registry;
    private SapMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        final var tenantService = Mockito.mock(TenantService.class);
        when(tenantService.getCurrentStringIdentifier()).thenReturn("bern");
        metrics = new SapMetrics(registry, tenantService);
    }

    @Test
    void recordPaymentUsesDomainAndMetricsEnums() {
        metrics.recordPayment(BuchhaltungType.AUSZAHLUNG_INITIAL, SapMetrics.PaymentOutcome.INITIATED);

        assertThat(
            registry.get(SapMetrics.PAYMENTS)
                .tags("tenant", "bern", "payment_type", "auszahlung_initial", "outcome", "initiated")
                .counter()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordPaymentRejectsNonPaymentBuchhaltungType() {
        assertThatThrownBy(
            () -> metrics.recordPayment(BuchhaltungType.BUSINESSPARTNER_CREATE, SapMetrics.PaymentOutcome.INITIATED)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void recordRetryUsesTypedOperationAndTrigger() {
        metrics.recordRetry(BuchhaltungType.BUSINESSPARTNER_CREATE, SapMetrics.RetryTrigger.ORCHESTRATION);

        assertThat(
            registry.get(SapMetrics.RETRIES)
                .tags(
                    "tenant",
                    "bern",
                    "operation",
                    "businesspartner_create",
                    "trigger",
                    "orchestration"
                )
                .counter()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordDeliveryAttemptRejectsUnboundedAttemptTag() {
        assertThatThrownBy(
            () -> metrics.recordDeliveryAttempt(BuchhaltungType.AUSZAHLUNG_INITIAL, 4)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void recordSoapRequestRecordsSuccessAndTimeoutSeparately() {
        assertThat(
            metrics.recordSoapRequest(SapMetrics.SoapOperation.IMPORT_STATUS_READ, () -> "response")
        ).isEqualTo("response");

        assertThatThrownBy(
            () -> metrics.recordSoapRequest(SapMetrics.SoapOperation.IMPORT_STATUS_READ, () -> {
                throw new RuntimeException(new SocketTimeoutException());
            })
        ).isInstanceOf(RuntimeException.class);

        assertThat(
            registry.get(SapMetrics.SOAP_REQUESTS)
                .tags("tenant", "bern", "operation", "import_status_read", "outcome", "success")
                .timer()
                .count()
        ).isEqualTo(1);
        assertThat(
            registry.get(SapMetrics.SOAP_REQUESTS)
                .tags("tenant", "bern", "operation", "import_status_read", "outcome", "timeout")
                .timer()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordPaymentTransitionRecordsEndToEndDuration() {
        metrics.recordStatusTransition(
            BuchhaltungType.AUSZAHLUNG_REMAINDER,
            SapStatus.IN_PROGRESS,
            SapStatus.SUCCESS,
            Duration.ofSeconds(12)
        );

        assertThat(
            registry.get(SapMetrics.DELIVERY_STATUS_TRANSITIONS)
                .tags(
                    "tenant",
                    "bern",
                    "operation",
                    "auszahlung_remainder",
                    "from",
                    "in_progress",
                    "to",
                    "success"
                )
                .counter()
                .count()
        ).isEqualTo(1);
        assertThat(
            registry.get(SapMetrics.PAYMENT_DURATION)
                .tags("tenant", "bern", "payment_type", "auszahlung_remainder", "outcome", "success")
                .timer()
                .totalTime(TimeUnit.SECONDS)
        ).isEqualTo(12);
    }

    @Test
    void businessPartnerTransitionDoesNotRecordPaymentDuration() {
        metrics.recordStatusTransition(
            BuchhaltungType.BUSINESSPARTNER_CREATE,
            null,
            SapStatus.SUCCESS,
            Duration.ofSeconds(12)
        );

        assertThat(registry.find(SapMetrics.PAYMENT_DURATION).timer()).isNull();
    }
}
