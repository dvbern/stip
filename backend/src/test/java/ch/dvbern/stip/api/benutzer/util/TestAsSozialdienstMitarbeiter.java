package ch.dvbern.stip.api.benutzer.util;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_MITARBEITER_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "sozialdienst_mitarbeiter",
    roles = {
        "Sozialdienst_Mitarbeiter"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = SOZIALDIENST_MITARBEITER_ID),
        @Claim(key = "family_name", value = "Sozialdienst_Mitarbeiter"),
        @Claim(key = "given_name", value = "Max")
    }
)
public @interface TestAsSozialdienstMitarbeiter {
}
