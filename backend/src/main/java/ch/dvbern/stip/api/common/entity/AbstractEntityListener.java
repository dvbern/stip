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

package ch.dvbern.stip.api.common.entity;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AbstractEntityListener {
    @Inject
    Instance<JsonWebToken> token;

    @PrePersist
    protected void prePersist(AbstractEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setTimestampErstellt(now);
        entity.setTimestampMutiert(now);

        final var currentBenutzername = getBenutzername();
        entity.setUserErstellt(currentBenutzername);
        entity.setUserMutiert(currentBenutzername);
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setTimestampMutiert(LocalDateTime.now());
        entity.setUserMutiert(getBenutzername());
    }

    private String getBenutzername() {
        if (token != null && token.isResolvable()) {
            final var jwt = token.get();
            final var givenName = jwt.getClaim(Claims.given_name);
            final var familyName = jwt.getClaim(Claims.family_name);
            if (givenName == null && familyName == null) {
                return "System";
            }

            return givenName + " " + familyName;
        } else {
            return "System";
        }
    }
}
