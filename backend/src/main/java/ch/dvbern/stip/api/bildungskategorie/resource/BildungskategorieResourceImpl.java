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

package ch.dvbern.stip.api.bildungskategorie.resource;

import java.util.List;

import ch.dvbern.stip.api.bildungskategorie.service.BildungskategorieService;
import ch.dvbern.stip.api.common.authorization.BildungskategorieAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.generated.api.BildungskategorieResource;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class BildungskategorieResourceImpl implements BildungskategorieResource {
    private final BildungskategorieAuthorizer bildungskategorieAuthorizer;
    private final BildungskategorieService bildungskategorieService;

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public List<BildungskategorieDto> getBildungskategorien() {
        bildungskategorieAuthorizer.canGet();
        return bildungskategorieService.findAll();
    }
}
