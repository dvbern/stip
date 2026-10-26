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
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import jakarta.xml.ws.soap.SOAPFaultException;

@Singleton
public class SapMetrics extends AbstractTimedMetrics {
    static final String PAYMENTS = "kstip.sap.payments";
    static final String PAYMENT_DURATION = "kstip.sap.payment.duration";
    static final String DELIVERY_ATTEMPTS = "kstip.sap.delivery.attempts";
    static final String DELIVERY_STATUS_TRANSITIONS = "kstip.sap.delivery.status.transitions";
    static final String SOAP_REQUESTS = "kstip.sap.soap.requests";
    static final String RETRIES = "kstip.sap.retries";

    public SapMetrics(final MeterRegistry meterRegistry, final TenantService tenantService) {
        super(meterRegistry, tenantService);
    }

    public void recordPayment(final BuchhaltungType paymentType, final PaymentOutcome outcome) {
        requirePaymentType(paymentType);
        recordPayment(paymentType, tag(outcome));
    }

    public void recordPayment(final BuchhaltungType paymentType, final SapStatus outcome) {
        requirePaymentType(paymentType);
        if (outcome == SapStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("IN_PROGRESS is not a payment outcome");
        }
        recordPayment(paymentType, tag(outcome));
    }

    public void recordDeliveryAttempt(final BuchhaltungType operation, final int attempt) {
        requireSapOperation(operation);
        registry().counter(
            DELIVERY_ATTEMPTS,
            Tags.of(
                "tenant",
                currentTenant(),
                "operation",
                tag(operation),
                "attempt",
                DeliveryAttempt.from(attempt).tagValue
            )
        ).increment();
    }

    public void recordStatusTransition(
        final BuchhaltungType operation,
        final @Nullable SapStatus source,
        final SapStatus destination,
        final @Nullable Duration duration
    ) {
        requireSapOperation(operation);
        registry().counter(
            DELIVERY_STATUS_TRANSITIONS,
            Tags.of(
                "tenant",
                currentTenant(),
                "operation",
                tag(operation),
                "from",
                source == null ? "unset" : tag(source),
                "to",
                tag(destination)
            )
        ).increment();

        if (
            BuchhaltungType.AUSZAHLUNGS.contains(operation)
            && destination != SapStatus.IN_PROGRESS
            && duration != null
            && !duration.isNegative()
        ) {
            timer(
                PAYMENT_DURATION,
                "Time from SAP payment delivery creation to a terminal status",
                Tags.of("payment_type", tag(operation), "outcome", tag(destination))
            ).record(duration);
        }
    }

    public void recordRetry(final BuchhaltungType operation, final RetryTrigger trigger) {
        requireSapOperation(operation);
        registry().counter(
            RETRIES,
            Tags.of("tenant", currentTenant(), "operation", tag(operation), "trigger", tag(trigger))
        ).increment();
    }

    public <T> T recordSoapRequest(final SoapOperation operation, final Supplier<T> action) {
        return recordTimed(SOAP_REQUESTS, Tags.of("operation", tag(operation)), action);
    }

    @Override
    protected String outcomeFor(final Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (
                current instanceof SocketTimeoutException
                || current instanceof HttpTimeoutException
                || current instanceof TimeoutException
            ) {
                return tag(RequestOutcome.TIMEOUT);
            }
            if (current instanceof SOAPFaultException) {
                return tag(RequestOutcome.SOAP_FAULT);
            }
            current = current.getCause();
        }
        return tag(RequestOutcome.ERROR);
    }

    private void recordPayment(final BuchhaltungType paymentType, final String outcome) {
        registry().counter(
            PAYMENTS,
            Tags.of("tenant", currentTenant(), "payment_type", tag(paymentType), "outcome", outcome)
        ).increment();
    }

    private static void requirePaymentType(final BuchhaltungType paymentType) {
        if (!BuchhaltungType.AUSZAHLUNGS.contains(paymentType)) {
            throw new IllegalArgumentException("Not a payment type: " + paymentType);
        }
    }

    private static void requireSapOperation(final BuchhaltungType operation) {
        switch (operation) {
            case AUSZAHLUNG_INITIAL, AUSZAHLUNG_REMAINDER, BUSINESSPARTNER_CREATE, BUSINESSPARTNER_CHANGE -> {
                return;
            }
            case SALDOAENDERUNG, STIPENDIUM -> throw new IllegalArgumentException("Not a SAP operation: " + operation);
        }
    }

    public enum PaymentOutcome {
        INITIATED,
        SKIPPED_NON_POSITIVE
    }

    public enum RetryTrigger {
        ORCHESTRATION
    }

    public enum SoapOperation {
        BUSINESS_PARTNER_CREATE,
        BUSINESS_PARTNER_CHANGE,
        BUSINESS_PARTNER_READ,
        BUSINESS_PARTNER_SEARCH,
        IMPORT_STATUS_READ,
        VENDOR_POSTING_CREATE
    }

    private enum RequestOutcome {
        SUCCESS,
        ERROR,
        TIMEOUT,
        SOAP_FAULT
    }

    private enum DeliveryAttempt {
        FIRST("1"),
        SECOND("2"),
        THIRD("3");

        private final String tagValue;

        DeliveryAttempt(final String tagValue) {
            this.tagValue = tagValue;
        }

        private static DeliveryAttempt from(final int attempt) {
            return switch (attempt) {
                case 1 -> FIRST;
                case 2 -> SECOND;
                case 3 -> THIRD;
                default -> throw new IllegalArgumentException("Unsupported SAP delivery attempt: " + attempt);
            };
        }
    }
}
