package ch.dvbern.stip.arch;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.Authorizer;
import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.core.domain.AccessTarget.FieldAccessTarget;
import com.tngtech.archunit.core.domain.AccessTarget.MethodCallTarget;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class AuthorizerAnnotationTest {
    @Test
    void test_endpoint_calls_authorizer() {
        final var rule = classes()
            .that()
            .resideInAnyPackage("..resource..")
            .should(new CallAuthorizerMethod());

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    private static class CallAuthorizerMethod extends ArchCondition<JavaClass> {
        public CallAuthorizerMethod() {
            super("call authorizer method");
        }

        @Override
        public void check(JavaClass item, ConditionEvents events) {
            final var methodIds = getMethodIdentifiers(item);
            methodIds.removeAll(collectMethodsCallingAuthorizer(item));
            methodIds.removeAll(collectAllowAllAnnotations(item));

            for (final var methodId : methodIds) {
                events.add(new SimpleConditionEvent(item, false, methodId));
            }
        }

        private Set<String> getMethodIdentifiers(final JavaClass javaClass) {
            return javaClass.getMethods().stream().filter(method -> !method.isConstructor())
                .map(JavaCodeUnit::getFullName).collect(Collectors.toSet());
        }

        private Set<String> collectMethodsCallingAuthorizer(final JavaClass javaClass) {
            final var methodIdentifiers = new HashSet<String>();

            for (final var access : javaClass.getAccessesFromSelf()) {
                if (!isCallFromMethod(access)) {
                    continue;
                }

                if (isCallToAuthorizer(access)) {
                    final var callingMethod = (JavaMethod) access.getOwner();
                    methodIdentifiers.add(callingMethod.getFullName());
                }
            }

            return methodIdentifiers;
        }

        private Set<String> collectAllowAllAnnotations(final JavaClass javaClass) {
            final var methodIdentifiers = new HashSet<String>();

            for (final var method : javaClass.getMethods()) {
                if (method.isAnnotatedWith(AllowAll.class)) {
                    methodIdentifiers.add(method.getFullName());
                }
            }

            return methodIdentifiers;
        }

        private boolean isCallFromMethod(final JavaAccess<?> access) {
            return access.getOrigin() instanceof JavaMethod;
        }

        private boolean isCallToAuthorizer(final JavaAccess<?> access) {
            if (access.getTarget() instanceof FieldAccessTarget) {
                return ((FieldAccessTarget) access.getTarget())
                    .getType()
                    .toErasure()
                    .isAnnotatedWith(Authorizer.class);
            } else if (access.getTarget() instanceof MethodCallTarget) {
                return access.getTarget().getOwner().isAnnotatedWith(Authorizer.class);
            }

            return false;
        }
    }
}