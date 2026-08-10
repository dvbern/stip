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

package ch.dvbern.stip.api.benutzer.entity;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.JwtUtil;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;

@RequestScoped
public class CurrentBenutzerContext {
    @Getter
    @Nullable
    private UUID benutzerId;

    @Nullable
    private String benutzerFullName;

    public String getBenutzerFullName() {
        return Objects.requireNonNullElse(benutzerFullName, JwtUtil.SYSTEM_USR);
    }

    public void setCurrentBenutzer(@Nullable final UUID benutzerId, @Nullable final String benutzerFullName) {
        this.benutzerId = benutzerId;
        this.benutzerFullName = benutzerFullName;
    }

    public void clear() {
        benutzerId = null;
        benutzerFullName = null;
    }
}
