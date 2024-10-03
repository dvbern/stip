package ch.dvbern.stip.api.common.authorization;

import java.util.Set;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.util.OidcConstants;

@Authorizer
public class BaseAuthorizer {
    public void allowAllow() {
        // noop
    }

    protected boolean isAdminOrSb(final Benutzer currentBenutzer) {
        return currentBenutzer.hasOneOfRoles(Set.of(OidcConstants.ROLE_ADMIN, OidcConstants.ROLE_SACHBEARBEITER));
    }

    protected boolean isAdmin(final Benutzer currentBenutzer) {
        return currentBenutzer.hasRole(OidcConstants.ROLE_ADMIN);
    }

    protected boolean isSachbearbeiter(final Benutzer currentBenutzer) {
        return currentBenutzer.hasRole(OidcConstants.ROLE_SACHBEARBEITER);
    }

    protected boolean isGesuchsteller(final Benutzer currentBenutzer) {
        return currentBenutzer.hasRole(OidcConstants.ROLE_GESUCHSTELLER);
    }

    protected boolean isGesuchstellerAndNotAdmin(final Benutzer currentBenutzer) {
        return isGesuchsteller(currentBenutzer) && !isAdmin(currentBenutzer);
    }
}
