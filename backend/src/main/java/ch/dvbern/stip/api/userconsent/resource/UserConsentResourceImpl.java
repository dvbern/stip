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

package ch.dvbern.stip.api.userconsent.resource;

import ch.dvbern.stip.api.common.authorization.UserConsentAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.userconsent.service.UserConsentService;
import ch.dvbern.stip.generated.api.UserConsentResource;
import ch.dvbern.stip.generated.dto.CreateUserConsentDto;
import ch.dvbern.stip.generated.dto.UserConsentDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class UserConsentResourceImpl implements UserConsentResource {

    private final UserConsentService userConsentService;
    private final UserConsentAuthorizer userConsentAuthorizer;

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public UserConsentDto getUserConsent() {
        userConsentAuthorizer.canRead();
        return userConsentService.getUserConsent();
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public UserConsentDto createUserConsent(@Valid @NotNull CreateUserConsentDto createUserConsentDto) {
        userConsentAuthorizer.canCreate();
        return userConsentService.createUserConsent(createUserConsentDto.getConsentGiven());
    }
}
