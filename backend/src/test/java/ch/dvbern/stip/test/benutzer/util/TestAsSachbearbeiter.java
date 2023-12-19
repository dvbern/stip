package ch.dvbern.stip.test.benutzer.util;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@TestSecurity(user = "sachbearbeiter", roles = ROLE_SACHBEARBEITER)
@OidcSecurity(
        claims = {
                @Claim(key = "sub", value = "ea75c9be-35a0-4ae6-9383-a3459501596b"),
                @Claim(key = "family_name", value = "Sachbearbeiter"),
                @Claim(key = "given_name", value = "Hans")
        }
)
public @interface TestAsSachbearbeiter {
}
