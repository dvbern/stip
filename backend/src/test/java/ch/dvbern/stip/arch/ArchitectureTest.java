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

import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.Slice;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ArchitectureTest {

    public static Architectures.LayeredArchitecture LAYERS = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Resource")
        .definedBy("..resource..")
        .layer("Service")
        .definedBy("..service..")
        .layer("Repository")
        .definedBy("..repo..")
        .optionalLayer("Domain")
        .definedBy("..dto..")
        .layer("Entity")
        .definedBy("..entity..")
        .optionalLayer("Generated")
        .definedBy("..generated..")
        .optionalLayer("Type")
        .definedBy("..type..")
        .optionalLayer("Statemachines")
        .definedBy("..common.statemachines..")
        .layer("Validation")
        .definedBy("..validation..")
        .optionalLayer("Util")
        .definedBy("..util..")
        .optionalLayer("DTO")
        .definedBy("..dto..")
        .layer("Authorization")
        .definedBy("..authorization..");

    @Test
    void test_layer_boundaries() {
        var rule = LAYERS.whereLayer("Resource")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("Repository")
            .mayOnlyBeAccessedByLayers("Service", "Authorization")
            .whereLayer("Entity")
            .mayOnlyBeAccessedByLayers(
                "Service",
                "Repository",
                "Generated",
                "Statemachines",
                "Validation",
                "Util",
                "DTO",
                "Authorization"
            );

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    @Test
    void no_cycles_between_features() {
        var rule = slices().matching("..stip.api.(**).service")
            .should()
            .beFreeOfCycles()
            .because("Cycles between feature decrease maintainability. Introduce a new shared feature");
        rule.check(ArchTestUtil.APP_CLASSES);
    }

    @Test
    void no_cross_feature_repo_access() {
        var rule = slices()
            .matching("..stip.api.(*).repo")
            .that(new DescribedPredicate<>("features") {
                @Override
                public boolean test(Slice javaClasses) {
                    return !javaClasses.getNamePart(1).equals("common");
                }
            })
            .should()
            .notDependOnEachOther()
            .because("Use a service to access repos outside of the feature package");
        rule.check(ArchTestUtil.APP_CLASSES);
    }

    @Test
    void transactional_boundary() {
        var rule = classes().that()
            .resideOutsideOfPackages("..service..", "..repo..")
            .should()
            .notBeAnnotatedWith(Transactional.class);

        rule.check(ArchTestUtil.APP_CLASSES);
    }
}
