package ch.dvbern.stip.api.benutzer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import static ch.dvbern.stip.api.common.util.OidcConstants.CLAIM_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID;
import static ch.dvbern.stip.api.util.TestConstants.JURIST_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "jurist",
    roles = {
        "Jurist",
        "AUSBILDUNG_CREATE",
        "AUSBILDUNG_READ",
        "AUSBILDUNG_UPDATE",
        "AUSBILDUNG_DELETE"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = JURIST_ID),
        @Claim(key = CLAIM_AHV_NUMMER, value = AHV_NUMMER_VALID),
        @Claim(key = "family_name", value = "Gesuchsteller"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsJurist {
}
