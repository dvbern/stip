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

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractTimedMetrics {
    private final MeterRegistry meterRegistry;
    private final TenantService tenantService;

    protected String outcomeFor(final Throwable throwable) {
        return tag(classifyTimeout(throwable) ? Outcome.TIMEOUT : Outcome.ERROR);
    }

    protected final <T> T recordTimed(final String name, final Tags tags, final Supplier<T> action) {
        return recordTimed(name, null, tags, action);
    }

    protected final <T> T recordTimed(
        final String name,
        final @Nullable String description,
        final Tags tags,
        final Supplier<T> action
    ) {
        final var sample = Timer.start(meterRegistry);
        try {
            final var result = action.get();
            sample.stop(timer(name, description, tags.and("outcome", tag(Outcome.SUCCESS))));
            return result;
        } catch (RuntimeException | Error e) {
            sample.stop(timer(name, description, tags.and("outcome", outcomeFor(e))));
            throw e;
        }
    }

    protected final void recordTimed(final String name, final Tags tags, final Runnable action) {
        recordTimed(name, null, tags, () -> {
            action.run();
            return null;
        });
    }

    protected final void recordSize(final String name, final Tags tags, final long value) {
        DistributionSummary.builder(name)
            .description("Generated output size in bytes")
            .baseUnit("bytes")
            .tag("tenant", currentTenant())
            .tags(tags)
            .register(meterRegistry)
            .record(value);
    }

    protected final MeterRegistry registry() {
        return meterRegistry;
    }

    protected final String currentTenant() {
        try {
            return Objects.requireNonNullElse(tenantService.getCurrentStringIdentifier(), "unknown");
        } catch (RuntimeException e) {
            return "unknown";
        }
    }

    protected static String tag(final Enum<?> value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

    protected static String normalize(final String value) {
        return Objects.requireNonNullElse(value, "unset").toLowerCase();
    }

    protected final Timer timer(final String name, final @Nullable String description, final Tags tags) {
        final var builder = Timer.builder(name).tags(tags).tag("tenant", currentTenant());
        if (description != null) {
            builder.description(description);
        }
        return builder.register(meterRegistry);
    }

    private static boolean classifyTimeout(final Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            final var name = current.getClass().getSimpleName().toLowerCase();
            if (name.contains("timeout")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    protected enum Outcome {
        SUCCESS,
        ERROR,
        TIMEOUT
    }
}
