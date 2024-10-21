package ch.dvbern.stip.api.benutzer.util;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_ADMIN_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "sozialdienst_admin",
    roles = {
        "Sozialdienst_Admin"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = SOZIALDIENST_ADMIN_ID),
        @Claim(key = "family_name", value = "Sozialdienst_Admin"),
        @Claim(key = "given_name", value = "Maximilian")
    }
)
public @interface TestAsSozialdienstAdmin {
}
