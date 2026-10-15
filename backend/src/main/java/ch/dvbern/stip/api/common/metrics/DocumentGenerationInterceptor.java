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

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@RecordedDocumentGeneration
public class DocumentGenerationInterceptor {

    @Inject
    DocumentMetrics documentMetrics;

    @AroundInvoke
    public Object record(final InvocationContext context) throws Exception {
        final var method = context.getMethod();
        final var annotation = method.getAnnotation(RecordedDocumentGeneration.class);
        if (annotation == null) {
            throw new IllegalStateException("@RecordedDocumentGeneration is missing on " + method);
        }
        return documentMetrics.recordDocumentGeneration(annotation.value(), annotation.engine(), () -> {
            try {
                return context.proceed();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
