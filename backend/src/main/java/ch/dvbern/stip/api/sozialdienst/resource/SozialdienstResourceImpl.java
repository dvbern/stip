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

import ch.dvbern.stip.api.common.authorization.SozialdienstAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENSTBENUTZER_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENSTBENUTZER_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENSTBENUTZER_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENSTBENUTZER_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENST_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENST_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENST_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SOZIALDIENST_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class SozialdienstResourceImpl implements SozialdienstResource {
    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerService sozialdienstBenutzerService;
    private final SozialdienstAuthorizer sozialdienstAuthorizer;

    @Override
    @RolesAllowed(SOZIALDIENST_CREATE)
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        sozialdienstAuthorizer.canCreateSozialdienst();
        return sozialdienstService.createSozialdienst(sozialdienstCreateDto);
    }

    @Override
    @RolesAllowed(SOZIALDIENST_DELETE)
    public SozialdienstDto deleteSozialdienst(UUID sozialdienstId) {
        sozialdienstAuthorizer.canDeleteSozialdienst();
        return sozialdienstService.deleteSozialdienst(sozialdienstId);
    }

    @Override
    @RolesAllowed(SOZIALDIENST_READ)
    public List<SozialdienstDto> getAllSozialdienste() {
        sozialdienstAuthorizer.canGetAllSozialdienste();
        return sozialdienstService.getAllSozialdienst();
    }

    @Override
    @RolesAllowed(SOZIALDIENST_READ)
    public List<SozialdienstSlimDto> getAllSozialdiensteForDelegation() {
        sozialdienstAuthorizer.canGetAllSozialdiensteForDelegation();
        return sozialdienstService.getAllSozialdiensteForDelegation();
    }

    @Override
    @RolesAllowed(SOZIALDIENST_READ)
    public SozialdienstDto getSozialdienst(UUID sozialdienstId) {
        sozialdienstAuthorizer.canGetSozialdienst();
        return sozialdienstService.getSozialdienstById(sozialdienstId);
    }

    @Override
    @RolesAllowed(SOZIALDIENST_UPDATE)
    public SozialdienstBenutzerDto replaceSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminDto sozialdienstAdminDto
    ) {
        sozialdienstAuthorizer.canReplaceSozialdienstAdmin();
        return sozialdienstService.replaceSozialdienstAdmin(sozialdienstId, sozialdienstAdminDto);
    }

    @Override
    @RolesAllowed(SOZIALDIENST_UPDATE)
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        sozialdienstAuthorizer.canUpdate();
        return sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
    }

    @Override
    @RolesAllowed(SOZIALDIENST_UPDATE)
    public SozialdienstBenutzerDto updateSozialdienstAdmin(
        SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto
    ) {
        sozialdienstAuthorizer.canUpdateSozialdienstAdmin();
        return sozialdienstBenutzerService.updateSozialdienstBenutzer(sozialdienstBenutzerUpdateDto);
    }

    @Override
    @RolesAllowed(SOZIALDIENSTBENUTZER_CREATE)
    public SozialdienstBenutzerDto createSozialdienstBenutzer(
        SozialdienstBenutzerCreateDto sozialdienstBenutzerCreateDto
    ) {
        return sozialdienstBenutzerService.createSozialdienstBenutzer(
            sozialdienstService.getSozialdienstOfCurrentSozialdienstAdmin(),
            sozialdienstBenutzerCreateDto
        );
    }

    @Override
    @RolesAllowed(SOZIALDIENSTBENUTZER_READ)
    public SozialdienstBenutzerDto getSozialdienstBenutzer(UUID sozialdienstBenutzerId) {
        sozialdienstAuthorizer.canUpdateSozialdienstBenutzer(sozialdienstBenutzerId);
        return sozialdienstBenutzerService
            .getSozialdienstBenutzerDtoById(sozialdienstBenutzerId);
    }

    @Override
    @RolesAllowed(SOZIALDIENSTBENUTZER_READ)
    public List<SozialdienstBenutzerDto> getSozialdienstBenutzerList() {
        return sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstService.getSozialdienstOfCurrentSozialdienstAdmin());
    }

    @Override
    @RolesAllowed(SOZIALDIENSTBENUTZER_UPDATE)
    public SozialdienstBenutzerDto updateSozialdienstBenutzer(
        SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto
    ) {
        sozialdienstAuthorizer.canUpdateSozialdienstBenutzer(sozialdienstBenutzerUpdateDto.getId());
        return sozialdienstBenutzerService.updateSozialdienstBenutzer(sozialdienstBenutzerUpdateDto);
    }

    @Override
    @RolesAllowed(SOZIALDIENSTBENUTZER_DELETE)
    public void deleteSozialdienstBenutzer(UUID sozialdienstBenutzerId) {
        sozialdienstAuthorizer.canUpdateSozialdienstBenutzer(sozialdienstBenutzerId);
        sozialdienstBenutzerService.deleteSozialdienstBenutzer(sozialdienstBenutzerId);
    }
}
