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

package ch.dvbern.stip.api.sozialdienst.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstadmin.service.SozialdienstAdminService;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class SozialdienstService {
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstMapper sozialdienstMapper;
    private final SozialdienstAdminService sozialdienstAdminService;

    @Transactional
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto dto) {
        var sozialdienst = sozialdienstMapper.toEntity(dto);
        final var adminDto = sozialdienstAdminService.createSozialdienstAdminBenutzer(dto.getSozialdienstAdmin());
        sozialdienst.setSozialdienstAdmin(sozialdienstAdminService.getSozialdienstAdminById(adminDto.getId()));
        sozialdienstRepository.persistAndFlush(sozialdienst);
        var result = sozialdienstMapper.toDto(sozialdienst);
        result.setSozialdienstAdmin(sozialdienstAdminService.getSozialdienstAdminDtoById(adminDto.getId()));
        return result;
    }

    @Transactional
    public SozialdienstDto getSozialdienstById(UUID id) {
        var entity = sozialdienstRepository.requireById(id);
        var result = sozialdienstMapper.toDto(entity);
        result.setSozialdienstAdmin(
            sozialdienstAdminService.getSozialdienstAdminDtoById(entity.getSozialdienstAdmin().getId())
        );
        return result;
    }

    @Transactional
    public List<SozialdienstDto> getAllSozialdienst() {
        final var entities = sozialdienstRepository.findAll();
        return entities.stream().map(entity -> {
            var sozialdienstDto = sozialdienstMapper.toDto(entity);
            var adminDto = sozialdienstAdminService.getSozialdienstAdminDtoById(entity.getSozialdienstAdmin().getId());
            sozialdienstDto.setSozialdienstAdmin(adminDto);
            return sozialdienstDto;
        }).toList();
    }

    @Transactional
    public SozialdienstDto deleteSozialdienst(UUID id) {
        final var entity = sozialdienstRepository.requireById(id);
        sozialdienstRepository.delete(entity);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(dto.getId());
        sozialdienstMapper.partialUpdate(dto, sozialdienst);
        var result = sozialdienstMapper.toDto(sozialdienst);
        result.setSozialdienstAdmin(
            sozialdienstAdminService.getSozialdienstAdminDtoById(sozialdienst.getSozialdienstAdmin().getId())
        );
        return result;
    }

    @Transactional
    public SozialdienstAdminDto updateSozialdienstAdmin(
        SozialdienstAdminUpdateDto dto,
        SozialdienstDto sozialdienstDto
    ) {
        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstDto.getId());
        final var adminKeykloakId = sozialdienst.getSozialdienstAdmin().getKeycloakId();
        var sozialdienstAdmin =
            sozialdienstAdminService.getSozialdienstAdminById(sozialdienst.getSozialdienstAdmin().getId());
        var responseDto = sozialdienstAdminService.updateSozialdienstAdminBenutzer(sozialdienstAdmin.getId(), dto);
        responseDto.setKeycloakId(adminKeykloakId);
        return responseDto;
    }

    @Transactional
    public SozialdienstAdminDto replaceSozialdienstAdmin(UUID sozialdienstId, SozialdienstAdminCreateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var benutzerToDelete = sozialdienst.getSozialdienstAdmin();
        sozialdienstAdminService.deleteSozialdienstAdminBenutzer(benutzerToDelete.getKeycloakId());

        final var newSozialdienstAdmin = sozialdienstAdminService.createSozialdienstAdminBenutzer(dto);
        sozialdienst.setSozialdienstAdmin(newSozialdienstAdmin);
        return sozialdienstAdminService.getSozialdienstAdminDtoById(newSozialdienstAdmin.getId());
    }
}
