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

package ch.dvbern.stip.stipdecision.resource;

import java.util.List;

import ch.dvbern.stip.generated.api.StipDecisionResource;
import ch.dvbern.stip.generated.dto.StipDecisionTextDto;
import ch.dvbern.stip.stipdecision.service.StipDecisionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
public class StipDecisionResourceImpl implements StipDecisionResource {
    private final StipDecisionService decisionService;

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    public List<StipDecisionTextDto> getAll() {
        return decisionService.getAll();
    }
}
