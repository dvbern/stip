package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.util.OidcConstants;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class ValidateUpdateLegalityUtil {
    public Object getAndValidateLegalityValue(final Set<String> benutzerRollenIdentifiers, final Object dtoValue,
                                              final Object existingValue, final Object defaultValue ){
        if (!CollectionUtils.containsAny(benutzerRollenIdentifiers,  Arrays.asList(OidcConstants.ROLE_SACHBEARBEITER,
            OidcConstants.ROLE_ADMIN))) {
            return Objects.requireNonNullElse(
                existingValue,
                defaultValue
            );
        } else{
            if(dtoValue == null){
                return Objects.requireNonNullElse(
                    existingValue,
                    defaultValue
                );
            }else{
                return Objects.requireNonNullElse(
                    dtoValue,
                    defaultValue
                );
            }
        }
    }
}
