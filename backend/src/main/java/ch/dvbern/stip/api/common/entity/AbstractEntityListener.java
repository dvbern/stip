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
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.CurrentBenutzerContext;
import ch.dvbern.stip.api.common.util.BusinessDateConstants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@ApplicationScoped
public class AbstractEntityListener {
    @Inject
    CurrentBenutzerContext currentBenutzerContext;

    private final ZoneId zoneId = BusinessDateConstants.ZUERICH_ZONE;

    @PrePersist
    protected void prePersist(AbstractEntity entity) {
        LocalDateTime now = ZonedDateTime.now(zoneId).toLocalDateTime();
        entity.setTimestampErstellt(now);
        entity.setTimestampMutiert(now);

        final String currentBenutzername = currentBenutzerContext.getBenutzerFullName();
        final UUID currentBenutzerUUID = currentBenutzerContext.getBenutzerId();

        entity.setUserErstellt(currentBenutzername);
        entity.setUserMutiert(currentBenutzername);

        entity.setUserErstelltId(currentBenutzerUUID);
        entity.setUserMutiertId(currentBenutzerUUID);

    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setTimestampMutiert(ZonedDateTime.now(zoneId).toLocalDateTime());
        entity.setUserMutiert(currentBenutzerContext.getBenutzerFullName());
        entity.setUserMutiertId(currentBenutzerContext.getBenutzerId());
    }
}
