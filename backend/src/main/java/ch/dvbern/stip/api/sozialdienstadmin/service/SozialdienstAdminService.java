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

package ch.dvbern.stip.api.sozialdienstadmin.service;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.sozialdienstadmin.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.sozialdienstadmin.repo.SozialdienstAdminRepository;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstAdminService {
    private final SozialdienstAdminRepository sozialdienstAdminRepository;
    private final SozialdienstAdminMapper sozialdienstAdminMapper;

    @Transactional
    public SozialdienstAdmin getSozialdienstAdminById(UUID id) {
        return sozialdienstAdminRepository.requireById(id);
    }

    @Transactional
    public SozialdienstAdminDto getSozialdienstAdminDtoById(UUID id) {
        return sozialdienstAdminMapper.toDto(getSozialdienstAdminById(id));
    }

    @Transactional
    public SozialdienstAdminDto updateSozialdienstAdminBenutzer(
        final UUID sozialdienstAdminId,
        SozialdienstAdminUpdateDto dto
    ) {
        var sozialdienstAdmin = sozialdienstAdminRepository.requireById(sozialdienstAdminId);
        sozialdienstAdminMapper.partialUpdate(dto, sozialdienstAdmin);
        return sozialdienstAdminMapper.toDto(sozialdienstAdmin);
    }

    @Transactional
    public SozialdienstAdmin createSozialdienstAdminBenutzer(SozialdienstAdminCreateDto dto) {
        final var sozialdienstAdmin = sozialdienstAdminMapper.toEntity(dto);
        sozialdienstAdmin.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdmin.setBenutzerStatus(BenutzerStatus.AKTIV);
        sozialdienstAdminRepository.persistAndFlush(sozialdienstAdmin);
        return sozialdienstAdmin;
    }

    @Transactional
    public void deleteSozialdienstAdminBenutzer(final String benutzerId) {
        final var sozialdienstAdmin =
            sozialdienstAdminRepository.findByKeycloakId(benutzerId).orElseThrow(NotFoundException::new);
        sozialdienstAdminRepository.delete(sozialdienstAdmin);
    }
}
