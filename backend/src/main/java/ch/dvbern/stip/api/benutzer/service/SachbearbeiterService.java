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

package ch.dvbern.stip.api.benutzer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.generated.dto.SachbearbeiterDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class SachbearbeiterService {
    private final SachbearbeiterRepository sachbearbeiterRepository;
    private final SachbearbeiterMapper sachbearbeiterMapper;
    private final RolleService rolleService;
    private final KeycloakBenutzerService keycloakBenutzerService;

    public SachbearbeiterDto getSachbearbeiterDto(final UUID sachbearbeiterId) {
        return sachbearbeiterMapper.toDto(sachbearbeiterRepository.requireById(sachbearbeiterId));
    }

    public List<SachbearbeiterDto> getSachbearbeiterDtoList() {
        return sachbearbeiterRepository.findAll().stream().map(sachbearbeiterMapper::toDto).toList();
    }

    @Transactional
    public SachbearbeiterDto createSachbearbeiter(final SachbearbeiterUpdateDto sachbearbeiterUpdateDto) {
        final var sachbearbeiter = sachbearbeiterMapper.toEntity(sachbearbeiterUpdateDto);
        sachbearbeiter.setBenutzerStatus(BenutzerStatus.AKTIV);
        if (Objects.isNull(sachbearbeiter.getBenutzereinstellungen())) {
            sachbearbeiter.setBenutzereinstellungen(new Benutzereinstellungen());
        }

        sachbearbeiter
            .setRollen(rolleService.mapOrCreateRoles(new HashSet<>(sachbearbeiterUpdateDto.getSachbearbeiterRollen())));

        sachbearbeiterRepository.persistAndFlush(sachbearbeiter);

        final var keycloakId = keycloakBenutzerService.createKeycloakBenutzer(
            sachbearbeiter.getVorname(),
            sachbearbeiter.getNachname(),
            sachbearbeiter.getEmail(),
            sachbearbeiterUpdateDto.getSachbearbeiterRollen()
        );
        sachbearbeiter.setKeycloakId(keycloakId);

        return sachbearbeiterMapper.toDto(sachbearbeiter);
    }

    @Transactional
    public SachbearbeiterDto updateSachbearbeiter(
        final UUID sachbearbeiterId,
        final SachbearbeiterUpdateDto sachbearbeiterUpdateDto
    ) {
        Sachbearbeiter sachbearbeiter = sachbearbeiterRepository.requireById(sachbearbeiterId);
        sachbearbeiter = sachbearbeiterMapper.partialUpdate(sachbearbeiterUpdateDto, sachbearbeiter);
        sachbearbeiter
            .setRollen(rolleService.mapOrCreateRoles(new HashSet<>(sachbearbeiterUpdateDto.getSachbearbeiterRollen())));
        keycloakBenutzerService.updateKeycloakBenutzer(
            sachbearbeiter.getKeycloakId(),
            sachbearbeiter.getVorname(),
            sachbearbeiter.getNachname(),
            sachbearbeiterUpdateDto.getSachbearbeiterRollen()
        );
        return sachbearbeiterMapper.toDto(sachbearbeiter);
    }

    @Transactional
    public void deleteSachbearbeiter(final UUID sachbearbeiterId) {
        Sachbearbeiter sachbearbeiter = sachbearbeiterRepository.requireById(sachbearbeiterId);
        sachbearbeiterRepository.deleteById(sachbearbeiterId);
        keycloakBenutzerService.deleteKeycloakBenutzer(sachbearbeiter.getKeycloakId());
    }

}
