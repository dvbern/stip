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

package ch.dvbern.stip.api.sozialdienst.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.SozialdienstAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerMapper;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class SozialdienstResourceImpl implements SozialdienstResource {

    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerService sozialdienstBenutzerService;
    private final SozialdienstBenutzerMapper sozialdienstBenutzerMapper;

    private final SozialdienstAuthorizer sozialdienstAuthorizer;

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        return sozialdienstService.createSozialdienst(sozialdienstCreateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto deleteSozialdienst(UUID sozialdienstId) {
        return sozialdienstService.deleteSozialdienst(sozialdienstId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_GESUCHSTELLER, OidcConstants.ROLE_ADMIN })
    @Override
    public List<SozialdienstDto> getAllSozialdienste() {
        return sozialdienstService.getAllSozialdienst();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto getSozialdienst(UUID sozialdienstId) {
        return sozialdienstService.getSozialdienstById(sozialdienstId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstBenutzerDto replaceSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminDto sozialdienstAdminDto
    ) {
        return sozialdienstService.replaceSozialdienstAdmin(sozialdienstId, sozialdienstAdminDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        return sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstBenutzerDto updateSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto
    ) {
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        return sozialdienstService.updateSozialdienstAdmin(sozialdienstAdminUpdateDto, sozialdienst);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_SOZIALDIENST_ADMIN })
    @Override
    public SozialdienstBenutzerDto createSozialdienstBenutzer(
        SozialdienstBenutzerCreateDto sozialdienstBenutzerCreateDto
    ) {
        return sozialdienstBenutzerService.createSozialdienstBenutzer(
            sozialdienstAuthorizer.getSozialdienstOfSozialdienstAdmin(),
            sozialdienstBenutzerCreateDto
        );
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_SOZIALDIENST_ADMIN })
    @Override
    public List<SozialdienstBenutzerDto> getSozialdienstBenutzer() {
        return sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstAuthorizer.getSozialdienstOfSozialdienstAdmin());
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_SOZIALDIENST_ADMIN })
    @Override
    public SozialdienstBenutzerDto updateSozialdienstBenutzer(
        SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto
    ) {
        return sozialdienstBenutzerService.updateSozialdienstBenutzer(sozialdienstBenutzerUpdateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_SOZIALDIENST_ADMIN })
    @Override
    public void deleteSozialdienstBenutzer(UUID sozialdienstBenutzerId) {
        sozialdienstBenutzerService.deleteSozialdienstBenutzer(sozialdienstBenutzerId);
    }
}
