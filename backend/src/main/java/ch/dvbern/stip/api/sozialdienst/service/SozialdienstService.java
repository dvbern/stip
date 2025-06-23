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

import ch.dvbern.stip.api.auszahlung.service.ZahlungsverbindungService;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class SozialdienstService {
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstMapper sozialdienstMapper;
    private final SozialdienstBenutzerService sozialdienstBenutzerService;
    private final ZahlungsverbindungService zahlungsverbindungService;

    public Sozialdienst getSozialdienstOfCurrentSozialdienstBenutzer() {
        final var sozialdienstBenutzer =
            sozialdienstBenutzerService.getCurrentSozialdienstBenutzer().orElseThrow(NotFoundException::new);
        return sozialdienstRepository.getSozialdienstByBenutzer(sozialdienstBenutzer);
    }

    @Transactional
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto dto) {
        var sozialdienst = sozialdienstMapper.toEntity(dto);
        final var admin = sozialdienstBenutzerService.createSozialdienstAdminBenutzer(dto.getSozialdienstAdmin());
        sozialdienst.setSozialdienstAdmin(sozialdienstBenutzerService.getSozialdienstBenutzerById(admin.getId()));
        sozialdienst.getSozialdienstBenutzers().add(admin);

        final var zahlungsverbindung = zahlungsverbindungService.createZahlungsverbindung(dto.getZahlungsverbindung());
        sozialdienst.setZahlungsverbindung(zahlungsverbindung);
        sozialdienstRepository.persistAndFlush(sozialdienst);
        return sozialdienstMapper.toDto(sozialdienst);
    }

    @Transactional
    public SozialdienstDto getSozialdienstById(UUID id) {
        var entity = sozialdienstRepository.requireById(id);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public List<SozialdienstDto> getAllSozialdienst() {
        final var entities = sozialdienstRepository.findAll();
        return entities.stream().map(sozialdienstMapper::toDto).toList();
    }

    @Transactional
    public List<SozialdienstSlimDto> getAllSozialdiensteForDelegation() {
        final var entities = sozialdienstRepository.getSozialdiensteWithMitarbeiter();
        return entities.map(sozialdienstMapper::toSlimDto).toList();
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
        return sozialdienstMapper.toDto(sozialdienst);
    }

    @Transactional
    public SozialdienstBenutzerDto replaceSozialdienstAdmin(UUID sozialdienstId, SozialdienstAdminDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var benutzerToDelete = sozialdienst.getSozialdienstAdmin();
        sozialdienstBenutzerService.deleteSozialdienstBenutzer(benutzerToDelete.getId());

        final var newSozialdienstAdmin = sozialdienstBenutzerService.createSozialdienstAdminBenutzer(dto);
        sozialdienst.setSozialdienstAdmin(newSozialdienstAdmin);
        return sozialdienstBenutzerService.getSozialdienstBenutzerDtoById(newSozialdienstAdmin.getId());
    }

    @Transactional
    public boolean isCurrentBenutzerMitarbeiterOfSozialdienst(final UUID sozialdienstId) {
        final var currentBenutzer = sozialdienstBenutzerService.getCurrentSozialdienstBenutzer();
        return currentBenutzer.map(benutzer -> isBenutzerMitarbeiterOfSozialdienst(sozialdienstId, benutzer))
            .orElse(false);
    }

    @Transactional
    public boolean isBenutzerMitarbeiterOfSozialdienst(
        final UUID sozialdienstId,
        final SozialdienstBenutzer benutzer
    ) {
        final var sozialdienstOfBenutzer = sozialdienstRepository.getSozialdienstByBenutzer(benutzer);
        return sozialdienstOfBenutzer.getId().equals(sozialdienstId);
    }
}
