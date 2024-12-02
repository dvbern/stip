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
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerResourceImpl implements BenutzerResource {

    private final BenutzerService benutzerService;
    private final SachbearbeiterZuordnungStammdatenWorker worker;
    private final TenantService tenantService;

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    @AllowAll
    public void createOrUpdateSachbearbeiterStammdaten(
        UUID benutzerId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto
    ) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(benutzerId, sachbearbeiterZuordnungStammdatenDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    @AllowAll
    public void createOrUpdateSachbearbeiterStammdatenList(
        List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto
    ) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(sachbearbeiterZuordnungStammdatenListDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @AllowAll
    public void deleteBenutzer(String benutzerId) {
        benutzerService.deleteBenutzer(benutzerId);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @AllowAll
    public List<BenutzerDto> getSachbearbeitende() {
        return benutzerService.getAllSachbearbeitendeMitZuordnungStammdaten();
    }

    @Override
    @AllowAll
    @RolesAllowed(STAMMDATEN_READ)
    public SachbearbeiterZuordnungStammdatenDto getSachbearbeiterStammdaten(UUID benutzerId) {
        return benutzerService.findSachbearbeiterZuordnungStammdatenWithBenutzerId(benutzerId)
            .orElseThrow(NotFoundException::new);
    }

    @Override
    @AllowAll
    public BenutzerDto prepareCurrentBenutzer() {
        return benutzerService.getOrCreateAndUpdateCurrentBenutzer();
    }
}
