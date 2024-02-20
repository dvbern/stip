package ch.dvbern.stip.arch;

import ch.dvbern.stip.arch.util.ArchTestUtil;
import jakarta.enterprise.context.RequestScoped;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class CdiTest {

    @Test
    void resources_are_request_scoped() {
        var rule = classes()
            .that()
            .resideInAPackage("..resource..")
            .and()
            .haveSimpleNameEndingWith("ResourceImpl")
            .should()
            .beAnnotatedWith(RequestScoped.class);

        rule.check(ArchTestUtil.APP_CLASSES);
    }
}
