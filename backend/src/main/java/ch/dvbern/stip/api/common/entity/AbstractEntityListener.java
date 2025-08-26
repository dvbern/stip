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
import java.time.ZoneId;
import java.time.ZonedDateTime;

import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.util.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AbstractEntityListener {
    @Inject
    Instance<JsonWebToken> token;

    private final ZoneId zoneId = DateUtil.ZUERICH_ZONE;

    @PrePersist
    protected void prePersist(AbstractEntity entity) {
        LocalDateTime now = ZonedDateTime.now(zoneId).toLocalDateTime();
        entity.setTimestampErstellt(now);
        entity.setTimestampMutiert(now);

        final var currentBenutzername = JwtUtil.extractUsernameFromJwt(token);
        entity.setUserErstellt(currentBenutzername);
        entity.setUserMutiert(currentBenutzername);
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setTimestampMutiert(ZonedDateTime.now(zoneId).toLocalDateTime());
        entity.setUserMutiert(JwtUtil.extractUsernameFromJwt(token));
    }
}
