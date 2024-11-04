/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
