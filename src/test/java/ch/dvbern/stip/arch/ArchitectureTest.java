package ch.dvbern.stip.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.arch.ArchTestUtil.ALL_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchitectureTest {


    public static Architectures.LayeredArchitecture LAYERS = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Resource")
            .definedBy("..resource..")
            .layer("Service")
            .definedBy("..service..")
            .layer("Repository")
            .definedBy("..repo..")
            .layer("Domain")
            .definedBy("..dto..")
            .layer("Entity")
            .definedBy("..entity..");

    @Test
    public void test_layer_boundaries() {
        var rule = LAYERS.whereLayer("Resource")
                .mayNotBeAccessedByAnyLayer()
                .whereLayer("Domain")
                .mayOnlyBeAccessedByLayers("Resource", "Service")
                .whereLayer("Repository")
                .mayOnlyBeAccessedByLayers("Service")
                .whereLayer("Entity")
                .mayOnlyBeAccessedByLayers("Service", "Repository");

        rule.check(ALL_CLASSES);
    }

    @Test
    public void no_cycles_between_features() {
        var rule = slices().matching("..stip.(**)")
                .should()
                .beFreeOfCycles()
                .because("Cycles between feature decrease maintainability. Introduce a new shared feature");
        rule.check(ALL_CLASSES);
    }

    @Test
    public void no_cross_feature_repo_access() {
        var rule = slices().matching("..stip.(*).repo")
                .should()
                .notDependOnEachOther()
                .because("Use a service to access repos outside of the feature package");
        rule.check(ALL_CLASSES);
    }

    @Test
    public void transactional_boundary() {
        var rule = classes().that()
                .resideOutsideOfPackages("..service..", "..repo..")
                .should()
                .notBeAnnotatedWith(Transactional.class);

        rule.check(ALL_CLASSES);
    }
}
