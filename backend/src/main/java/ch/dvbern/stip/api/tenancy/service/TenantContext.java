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

package ch.dvbern.stip.api.tenancy.service;

import java.util.Objects;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class TenantContext {
    @Getter
    @Nullable
    private TenantIdentifier tenantIdentifier;

    public TenantContext setTenantIdentifier(final TenantIdentifier tenantIdentifier) {
        if (Objects.nonNull(this.tenantIdentifier)) {
            throw new IllegalStateException("Do never set TenantIdentifier twice");
        }
        this.tenantIdentifier = tenantIdentifier;
        return this;
    }
}
