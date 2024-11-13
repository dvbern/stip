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

package ch.dvbern.stip.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class StepwiseExtension implements ExecutionCondition, TestExecutionExceptionHandler {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        final var namespace = namespaceFor(context);
        final var store = storeFor(context, namespace);
        final var value = store.get(StepwiseExtension.class, String.class);

        final var testMethod = context.getTestMethod();
        if (testMethod.isPresent() && testMethod.get().getAnnotation(AlwaysRun.class) != null) {
            return ConditionEvaluationResult.enabled("Running test annotated with 'AlwaysRun'");
        }

        if (value == null) {
            return ConditionEvaluationResult.enabled("No test failures in stepwise tests");
        }

        return ConditionEvaluationResult.disabled(
            String.format(
                "Stepwise test disabled due to previous failure in '%s'",
                value
            )
        );
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        final var namespace = namespaceFor(context);
        final var store = storeFor(context, namespace);
        store.put(StepwiseExtension.class, context.getDisplayName());
        throw throwable;
    }

    private ExtensionContext.Namespace namespaceFor(ExtensionContext extensionContext) {
        return ExtensionContext.Namespace.create(StepwiseExtension.class, extensionContext.getParent());
    }

    private ExtensionContext.Store storeFor(ExtensionContext extensionContext, ExtensionContext.Namespace namespace) {
        return extensionContext.getParent().get().getStore(namespace);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AlwaysRun {
    }
}
