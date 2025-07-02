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

package ch.dvbern.stip.api.benutzer.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.common.authorization.BenutzerAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.BENUTZER_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BENUTZER_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BENUTZER_GET;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BENUTZER_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BUCHSTABENZUWEISUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BUCHSTABENZUWEISUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BUCHSTABENZUWEISUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class BenutzerResourceImpl implements BenutzerResource {
    private final BenutzerAuthorizer benutzerAuthorizer;
    private final BenutzerService benutzerService;
    private final SachbearbeiterService sachbearbeiterService;
    private final SachbearbeiterZuordnungStammdatenWorker worker;
    private final TenantService tenantService;

    @Override
    @RolesAllowed({ BUCHSTABENZUWEISUNG_CREATE, BUCHSTABENZUWEISUNG_UPDATE })
    public void createOrUpdateSachbearbeiterStammdaten(
        UUID benutzerId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto
    ) {
        benutzerAuthorizer.canCreateOrUpdateBuchstabenzuweisung();
        benutzerService.createOrUpdateSachbearbeiterStammdaten(benutzerId, sachbearbeiterZuordnungStammdatenDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @RolesAllowed({ BUCHSTABENZUWEISUNG_CREATE, BUCHSTABENZUWEISUNG_UPDATE })
    public void createOrUpdateSachbearbeiterStammdatenList(
        List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto
    ) {
        benutzerAuthorizer.canCreateOrUpdateBuchstabenzuweisung();
        benutzerService.createOrUpdateSachbearbeiterStammdaten(sachbearbeiterZuordnungStammdatenListDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @RolesAllowed(BENUTZER_DELETE)
    public void deleteBenutzer(String benutzerId) {
        benutzerAuthorizer.canDeleteBenutzer();
        benutzerService.deleteBenutzer(benutzerId);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @RolesAllowed(BUCHSTABENZUWEISUNG_READ)
    public List<BenutzerDto> getSachbearbeitende() {
        benutzerAuthorizer.canGetBuchstabenzuweisung();
        return benutzerService.getAllSachbearbeitendeMitZuordnungStammdaten();
    }

    @Override
    @RolesAllowed(BUCHSTABENZUWEISUNG_READ)
    public SachbearbeiterZuordnungStammdatenDto getSachbearbeiterStammdaten(UUID benutzerId) {
        benutzerAuthorizer.canGetBuchstabenzuweisung();
        return benutzerService.findSachbearbeiterZuordnungStammdatenWithBenutzerId(benutzerId)
            .orElseThrow(NotFoundException::new);
    }

    @Override
    @PermitAll
    public BenutzerDto prepareCurrentBenutzer() {
        benutzerAuthorizer.canPrepare();
        return benutzerService.getOrCreateAndUpdateCurrentBenutzer();
    }

    @Override
    @RolesAllowed(BENUTZER_GET)
    public SachbearbeiterDto getSachbearbeiterForManagement(UUID sachbearbeiterId) {
        benutzerAuthorizer.canGetSachbearbeiters();
        return sachbearbeiterService.getSachbearbeiterDto(sachbearbeiterId);
    }

    @Override
    @RolesAllowed(BENUTZER_GET)
    public List<SachbearbeiterDto> getSachbearbeitersForManagement() {
        benutzerAuthorizer.canGetSachbearbeiters();
        return sachbearbeiterService.getSachbearbeiterDtoList();
    }

    @Override
    @RolesAllowed(BENUTZER_CREATE)
    public SachbearbeiterDto createSachbearbeiter(SachbearbeiterUpdateDto sachbearbeiterUpdateDto) {
        benutzerAuthorizer.canCreateSachbearbeiter();
        return sachbearbeiterService.createSachbearbeiter(sachbearbeiterUpdateDto);
    }

    @Override
    @RolesAllowed(BENUTZER_UPDATE)
    public SachbearbeiterDto updateSachbearbeiter(
        UUID sachbearbeiterId,
        SachbearbeiterUpdateDto sachbearbeiterUpdateDto
    ) {
        benutzerAuthorizer.canUpdateSachbearbeiter();
        return sachbearbeiterService.updateSachbearbeiter(sachbearbeiterId, sachbearbeiterUpdateDto);
    }

    @Override
    @RolesAllowed(BENUTZER_DELETE)
    public void deleteSachbearbeiter(UUID sachbearbeiterId) {
        benutzerAuthorizer.canDeleteSachbearbeiter();
        sachbearbeiterService.deleteSachbearbeiter(sachbearbeiterId);
    }
}
