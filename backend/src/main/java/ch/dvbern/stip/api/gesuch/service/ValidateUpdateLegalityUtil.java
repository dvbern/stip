package ch.dvbern.stip.api.gesuch.service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.common.util.OidcConstants;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

@UtilityClass
public class ValidateUpdateLegalityUtil {
    public <T> T getAndValidateLegalityValue(
        final Set<String> benutzerRollenIdentifiers,
        final T dtoValue,
        final T existingValue,
        final T defaultValue
    ){
        if (!CollectionUtils.containsAny(benutzerRollenIdentifiers,  Arrays.asList(OidcConstants.ROLE_SACHBEARBEITER,
            OidcConstants.ROLE_ADMIN))) {
            return Objects.requireNonNullElse(
                existingValue,
                defaultValue
            );
        }

        return Objects.requireNonNullElse(
            dtoValue,
            defaultValue
        );
    }
}
