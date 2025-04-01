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

package ch.dvbern.stip.api.buchhaltung.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.authorization.BuchhaltungAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.generated.api.BuchhaltungResource;
import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
import ch.dvbern.stip.generated.dto.BuchhaltungSaldokorrekturDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.BUCHHALTUNG_ENTRY_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.BUCHHALTUNG_ENTRY_READ;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class BuchhaltungResourceImpl implements BuchhaltungResource {
    private final BuchhaltungAuthorizer buchhaltungAuthorizer;
    private final BuchhaltungService buchhaltungService;

    @Override
    @RolesAllowed(BUCHHALTUNG_ENTRY_CREATE)
    public BuchhaltungEntryDto createBuchhaltungSaldokorrektur(
        UUID gesuchId,
        BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto
    ) {
        buchhaltungAuthorizer.canCreateBuchhaltungSaldokorrektur(gesuchId);
        return buchhaltungService.createBuchhaltungSaldokorrekturForFall(gesuchId, buchhaltungSaldokorrekturDto);
    }

    @Override
    @RolesAllowed(BUCHHALTUNG_ENTRY_READ)
    public List<BuchhaltungEntryDto> getBuchhaltungEntrys(UUID gesuchId) {
        buchhaltungAuthorizer.canGetBuchhaltungEntrys();
        return buchhaltungService.getAllDtoForGesuchId(gesuchId).toList();
    }
}
