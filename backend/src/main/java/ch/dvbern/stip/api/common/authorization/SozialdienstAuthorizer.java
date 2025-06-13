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

package ch.dvbern.stip.api.common.authorization;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class SozialdienstAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    public void canCreateSozialdienst() {
        permitAll();
    }

    public void canDeleteSozialdienst() {
        permitAll();
    }

    @Transactional
    public void canGetAllSozialdienste() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isAdmin(currentBenutzer)) {
            return;
        }

        forbidden();
    }

    public void canGetAllSozialdiensteForDelegation() {
        permitAll();
    }

    public void canGetSozialdienst() {
        permitAll();
    }

    public void canReplaceSozialdienstAdmin() {
        permitAll();
    }

    public void canUpdate() {
        permitAll();
    }

    @Transactional
    public void canUpdateSozialdienstAdmin() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isAdmin(currentBenutzer)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateSozialdienstBenutzer(final UUID sozialdienstBenutzerToUpdateID) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isAdmin(currentBenutzer)) {
            return;
        }

        final var sozialdienstBenutzerToUpdate =
            sozialdienstBenutzerRepository.findById(sozialdienstBenutzerToUpdateID);
        final var sozialdienstOfUser = sozialdienstBenutzerRepository
            .findSozialdienstBySozialdienstBenutzer(sozialdienstBenutzerToUpdate)
            .orElseThrow(NotFoundException::new);
        final var sozialdienstAdmin = sozialdienstBenutzerRepository
            .findByKeycloakId(currentBenutzer.getKeycloakId())
            .orElseThrow(NotFoundException::new);
        final var sozialdienstOfAdmin = sozialdienstRepository.getSozialdienstBySozialdienstAdmin(sozialdienstAdmin);

        if (!Objects.equals(sozialdienstOfUser.getId(), sozialdienstOfAdmin.getId())) {
            throw new UnauthorizedException();
        }
    }

    public void canCreateSozialdienstBenutzer() {
        permitAll();
    }

    public void canGetSozialdienstBenutzer() {
        permitAll();
    }
}
