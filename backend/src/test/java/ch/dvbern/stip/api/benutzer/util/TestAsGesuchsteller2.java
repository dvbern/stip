package ch.dvbern.stip.api.benutzer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import static ch.dvbern.stip.api.common.util.OidcConstants.CLAIM_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "gesuchsteller2",
    roles = {
        "Gesuchsteller",
        "GESUCH_READ",
        "GESUCH_UPDATE",
        "FALL_UPDATE",
        "GESUCH_CREATE",
        "GESUCH_DELETE",
        "FALL_CREATE",
        "GESUCHSPERIODE_READ",
        "FALL_READ",
        "AUSBILDUNG_READ",
        "STAMMDATEN_READ"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = GESUCHSTELLER_2_TEST_ID),
        @Claim(key = CLAIM_AHV_NUMMER, value = GESUCHSTELLER_2_TEST_AHV_NUMMER),
        @Claim(key = "family_name", value = "Gesuchsteller 2"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsGesuchsteller2 {
}
