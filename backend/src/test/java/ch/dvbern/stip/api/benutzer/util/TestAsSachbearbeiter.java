package ch.dvbern.stip.api.benutzer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "sachbearbeiter",
    roles = {
        "Sachbearbeiter",
        "GESUCH_READ",
        "GESUCH_UPDATE",
        "FALL_UPDATE",
        "GESUCH_CREATE",
        "FALL_CREATE",
        "GESUCHSPERIODE_READ",
        "GESUCH_DELETE",
        "FALL_DELETE",
        "FALL_READ",
        "AUSBILDUNG_READ"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = "ea75c9be-35a0-4ae6-9383-a3459501596b"),
        @Claim(key = "family_name", value = "Sachbearbeiter"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsSachbearbeiter {
}
