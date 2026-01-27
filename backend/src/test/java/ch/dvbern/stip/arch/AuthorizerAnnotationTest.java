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

package ch.dvbern.stip.arch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.authorization.Authorizer;
import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.core.domain.AccessTarget.FieldAccessTarget;
import com.tngtech.archunit.core.domain.AccessTarget.MethodCallTarget;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@Execution(ExecutionMode.CONCURRENT)
class AuthorizerAnnotationTest {
    private static final Set<String> ANONYMOUS_METHODS = new HashSet<>(
        List.of(
            "ch.dvbern.stip.api.tenancy.resource.TenantResourceImpl.getCurrentTenant",
            "ch.dvbern.stip.api.dokument.resource.DokumentResourceImpl.getDokument",
            "ch.dvbern.stip.api.verfuegung.resource.VerfuegungResourceImpl.getVerfuegungDokument",
            "ch.dvbern.stip.api.config.resource.ConfigResourceImpl.getDeploymentConfig",
            "ch.dvbern.stip.api.massendruck.resource.MassendruckJobResourceImpl.downloadMassendruckDocument",
            "ch.dvbern.stip.api.datenschutzbrief.resource.DatenschutzbriefRessourceImpl.getDatenschutzbrief",
            "ch.dvbern.stip.api.darlehen.resource.DarlehenResourceImpl.downloadDarlehenDokument",
            "ch.dvbern.stip.api.demo.resource.DemoDataResourceImpl.getDemoDataDokument"
        )
    );

    @Test
    void test_endpoint_calls_authorizer() {
        final var rule = classes()
            .that()
            .resideInAnyPackage("..resource..")
            .should(new CallAuthorizerMethod());

        rule.check(ArchTestUtil.APP_CLASSES);

        if (!ANONYMOUS_METHODS.isEmpty()) {
            final var msg = String.format(
                "Methods that are marked as anonymous in this test now call an authorizer:\n%s",
                Arrays.toString(ANONYMOUS_METHODS.toArray())
            );
            Assertions.fail(msg);
        }
    }

    private static class CallAuthorizerMethod extends ArchCondition<JavaClass> {
        public CallAuthorizerMethod() {
            super("call authorizer method");
        }

        @Override
        public void check(JavaClass item, ConditionEvents events) {
            final var methodIds = getMethodIdentifiers(item);
            methodIds.removeAll(collectMethodsCallingAuthorizer(item));

            final var removed = new HashSet<String>();
            for (final var anonymousMethod : ANONYMOUS_METHODS) {
                if (methodIds.remove(anonymousMethod)) {
                    removed.add(anonymousMethod);
                }
            }

            ANONYMOUS_METHODS.removeAll(removed);

            for (final var methodId : methodIds) {
                events.add(new SimpleConditionEvent(item, false, methodId));
            }
        }

        private Set<String> getMethodIdentifiers(final JavaClass javaClass) {
            return javaClass.getMethods()
                .stream()
                .filter(method -> !method.isConstructor())
                .map(method -> toFullyQualifiedName(javaClass, method))
                .collect(Collectors.toSet());
        }

        private Set<String> collectMethodsCallingAuthorizer(final JavaClass javaClass) {
            final var methodIdentifiers = new HashSet<String>();

            for (final var access : javaClass.getAccessesFromSelf()) {
                if (!isCallFromMethod(access)) {
                    continue;
                }

                if (isCallToAuthorizer(access)) {
                    final var callingMethod = (JavaMethod) access.getOwner();
                    methodIdentifiers.add(toFullyQualifiedName(javaClass, callingMethod));
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

        private String toFullyQualifiedName(final JavaClass parent, final JavaMethod method) {
            return parent.getFullName() + "." + method.getName();
        }
    }
}
