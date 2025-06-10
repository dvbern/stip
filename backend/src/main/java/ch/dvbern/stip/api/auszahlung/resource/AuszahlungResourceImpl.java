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

package ch.dvbern.stip.api.auszahlung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.service.AuszahlungService;
import ch.dvbern.stip.api.common.authorization.AuszahlungAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.OidcPermissions;
import ch.dvbern.stip.generated.api.AuszahlungResource;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@Validated
@RequestScoped
@RequiredArgsConstructor
public class AuszahlungResourceImpl implements AuszahlungResource {
    private final AuszahlungAuthorizer auszahlungAuthorizer;
    private final AuszahlungService auszahlungService;

    @RolesAllowed(OidcPermissions.AUSZAHLUNG_CREATE)
    @Override
    public UUID createAuszahlungForGesuch(UUID gesuchId, AuszahlungDto auszahlungDto) {
        auszahlungAuthorizer.canCreateAuszahlungForGesuch(gesuchId);
        return auszahlungService.createAuszahlungForGesuch(gesuchId, auszahlungDto);
    }

    @RolesAllowed(OidcPermissions.AUSZAHLUNG_READ)
    @Override
    public AuszahlungDto getAuszahlungForGesuch(UUID gesuchId) {
        auszahlungAuthorizer.canReadAuszahlungForGesuch(gesuchId);
        return auszahlungService.getAuszahlungForGesuch(gesuchId);
    }

    @RolesAllowed(OidcPermissions.AUSZAHLUNG_UPDATE)
    @Override
    public AuszahlungDto updateAuszahlungForGesuch(UUID gesuchId, AuszahlungUpdateDto auszahlungUpdateDto) {
        auszahlungAuthorizer.canUpdateAuszahlungForGesuch(gesuchId);
        return auszahlungService.updateAuszahlungForGesuch(gesuchId, auszahlungUpdateDto);
    }
}
