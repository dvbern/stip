package ch.dvbern.stip.api.benutzer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(user = "admin", roles = { ROLE_ADMIN, ROLE_SACHBEARBEITER })
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = "c1dd0d38-0beb-4694-af37-10bb7da5b12a"),
        @Claim(key = "family_name", value = "Admin"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsAdmin {
}
