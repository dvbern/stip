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

        return ConditionEvaluationResult.disabled(String.format(
            "Stepwise test disabled due to previous failure in '%s'",
            value
        ));
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
    public @interface AlwaysRun{}
}
