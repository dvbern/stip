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

import java.util.Optional;
import java.util.regex.Pattern;

import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@Execution(ExecutionMode.CONCURRENT)
class IntegrationArchitectureTest {

    private static final String DOMAIN = "ch.dvbern.stip.integration..domain..";
    private static final String ADAPTER = "ch.dvbern.stip.integration..adapter..";
    private static final String GENERATED = "ch.dvbern.stip.integration..adapter..generated..";

    private static final Pattern INTEGRATION_MODULE_PATTERN =
        Pattern.compile("^ch\\.dvbern\\.stip\\.integration\\.([^.]+)\\..*$");

    private static final Pattern INTEGRATION_DOMAIN_PATTERN =
        Pattern.compile("^ch\\.dvbern\\.stip\\.integration\\.[^.]+\\.domain(\\.|$).*$");

    private static final Pattern INTEGRATION_ADAPTER_PATTERN =
        Pattern.compile("^ch\\.dvbern\\.stip\\.integration\\.[^.]+\\.adapter(\\.|$).*$");

    @Test
    void domain_must_not_depend_on_adapters() {
        var rule = noClasses()
            .that()
            .resideInAPackage(DOMAIN)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(ADAPTER)
            .because(
                "integration domain packages define the integration API/core and must not access adapter implementations"
            );

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    @Test
    void domain_must_not_depend_on_generated_external_client_code() {
        var rule = noClasses()
            .that()
            .resideInAPackage(DOMAIN)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(GENERATED)
            .because("generated external-client code is an adapter implementation detail");

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    @Test
    void adapters_must_not_be_used_from_outside_adapters() {
        var rule = noClasses()
            .that()
            .resideOutsideOfPackages(ADAPTER)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(ADAPTER)
            .because(
                "outside packages should use integration domain ports, factories or services instead of adapter implementations"
            );

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    @Test
    void adapters_must_not_depend_on_other_integration_adapters() {
        var rule = slices()
            .matching("..stip.integration.(*).adapter..")
            .should()
            .notDependOnEachOther()
            .because("an adapter should be local to one integration module");

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    @Test
    void adapters_may_only_depend_on_their_own_domain() {
        var rule = classes()
            .that()
            .resideInAPackage(ADAPTER)
            .should(onlyDependOnOwnIntegrationModuleDomain())
            .because(
                "an adapter implements its own module's domain ports and should not couple to another integration module's domain"
            );

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    @Test
    void generated_code_must_be_adapter_internal() {
        var rule = classes()
            .that()
            .resideInAPackage(GENERATED)
            .should(onlyBeAccessedBySameIntegrationModuleAdapter())
            .because("generated external-client code should remain hidden inside the owning adapter");

        rule.check(ArchTestUtil.INTEGRATION_CLASSES);
    }

    private static ArchCondition<JavaClass> onlyDependOnOwnIntegrationModuleDomain() {
        return new ArchCondition<>("only depend on their own integration module domain") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                for (Dependency dependency : item.getDirectDependenciesFromSelf()) {
                    JavaClass target = dependency.getTargetClass();

                    if (isIntegrationDomain(target) && NotSameIntegrationModule(item, target)) {
                        events.add(
                            SimpleConditionEvent.violated(
                                dependency,
                                "%s depends on domain class %s from another integration module via %s"
                                    .formatted(
                                        item.getName(),
                                        target.getName(),
                                        dependency.getDescription()
                                    )
                            )
                        );
                    }
                }
            }
        };
    }

    private static ArchCondition<JavaClass> onlyBeAccessedBySameIntegrationModuleAdapter() {
        return new ArchCondition<>("only be accessed by the same integration module adapter") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                for (Dependency dependency : item.getDirectDependenciesToSelf()) {
                    JavaClass origin = dependency.getOriginClass();

                    if (!isIntegrationAdapter(origin) || NotSameIntegrationModule(origin, item)) {
                        events.add(
                            SimpleConditionEvent.violated(
                                dependency,
                                "generated class %s is accessed by %s via %s"
                                    .formatted(
                                        item.getName(),
                                        origin.getName(),
                                        dependency.getDescription()
                                    )
                            )
                        );
                    }
                }
            }
        };
    }

    private static boolean isIntegrationDomain(JavaClass javaClass) {
        return INTEGRATION_DOMAIN_PATTERN
            .matcher(javaClass.getPackageName())
            .matches();
    }

    private static boolean isIntegrationAdapter(JavaClass javaClass) {
        return INTEGRATION_ADAPTER_PATTERN
            .matcher(javaClass.getPackageName())
            .matches();
    }

    private static boolean NotSameIntegrationModule(JavaClass left, JavaClass right) {
        Optional<String> leftModule = integrationModuleOf(left);
        Optional<String> rightModule = integrationModuleOf(right);

        return leftModule.isEmpty() || !leftModule.equals(rightModule);
    }

    private static Optional<String> integrationModuleOf(JavaClass javaClass) {
        var matcher = INTEGRATION_MODULE_PATTERN.matcher(javaClass.getPackageName());

        if (matcher.matches()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }
}
