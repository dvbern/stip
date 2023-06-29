package ch.dvbern.stip.arch;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Path;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.arch.ArchTestUtil.ALL_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class CdiTest {

    @Test
    public void resources_are_request_scoped() {
        var rule = classes()
                .that()
                .resideInAPackage("..resource..")
                .and()
                .haveSimpleNameContaining("Resource")
                .should()
                .beAnnotatedWith(RequestScoped.class);

        rule.check(ALL_CLASSES);
    }
}
