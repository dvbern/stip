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

package ch.dvbern.stip.api.sozialdienstbenutzer.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.KeycloakBenutzerService;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class SozialdienstBenutzerService {
    private final JsonWebToken jsonWebToken;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstAdminMapper sozialdienstAdminMapper;
    private final SozialdienstBenutzerMapper sozialdienstBenutzerMapper;
    private final MailService mailService;
    private final KeycloakBenutzerService keycloakBenutzerService;

    public Optional<SozialdienstBenutzer> getCurrentSozialdienstBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw AppFailureMessage.missingSubject().create();
        }

        return sozialdienstBenutzerRepository.findByKeycloakId(keycloakId);
    }

    @Transactional
    public SozialdienstBenutzer getSozialdienstBenutzerById(UUID id) {
        return sozialdienstBenutzerRepository.requireById(id);
    }

    @Transactional
    public SozialdienstBenutzerDto getSozialdienstBenutzerDtoById(UUID id) {
        return sozialdienstBenutzerMapper.toDto(getSozialdienstBenutzerById(id));
    }

    @Transactional
    public SozialdienstBenutzer createSozialdienstAdminBenutzer(SozialdienstAdminDto dto) {
        final var sozialdienstAdmin = sozialdienstAdminMapper.toEntity(dto);
        sozialdienstAdmin.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdmin.setBenutzerStatus(BenutzerStatus.AKTIV);

        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstAdmin);
        final var keycloakId = keycloakBenutzerService.createKeycloakBenutzer(
            sozialdienstAdmin.getVorname(),
            sozialdienstAdmin.getNachname(),
            sozialdienstAdmin.getEmail(),
            List.of(OidcConstants.ROLE_SOZIALDIENST_ADMIN)
        );

        sozialdienstAdmin.setKeycloakId(keycloakId);
        return sozialdienstAdmin;
    }

    public List<SozialdienstBenutzerDto> getSozialdienstBenutzers(Sozialdienst sozialdienst) {
        return sozialdienst.getSozialdienstBenutzers()
            .stream()
            .map(
                sozialdienstBenutzerMapper::toDto
            )
            .toList();
    }

    @Transactional
    public SozialdienstBenutzerDto createSozialdienstMitarbeiterBenutzer(
        final Sozialdienst sozialdienst,
        final SozialdienstBenutzerCreateDto sozialdienstBenutzerCreateDto
    ) {
        final var sozialdienstBenutzer = sozialdienstBenutzerMapper.toEntity(sozialdienstBenutzerCreateDto);
        sozialdienstBenutzer.setBenutzerStatus(BenutzerStatus.AKTIV);
        sozialdienstBenutzer.setBenutzereinstellungen(new Benutzereinstellungen());

        sozialdienstRepository.requireById(sozialdienst.getId())
            .getSozialdienstBenutzers()
            .add(sozialdienstBenutzer);
        WelcomeMailDto welcomeMailDto = new WelcomeMailDto();
        welcomeMailDto.setName(sozialdienstBenutzer.getNachname());
        welcomeMailDto.setVorname(sozialdienstBenutzer.getVorname());
        welcomeMailDto.setEmail(sozialdienstBenutzer.getEmail());
        welcomeMailDto.setRedirectUri(sozialdienstBenutzerCreateDto.getRedirectUri());

        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstBenutzer);

        final var keycloakId = keycloakBenutzerService.createKeycloakBenutzer(
            sozialdienstBenutzerCreateDto.getVorname(),
            sozialdienstBenutzerCreateDto.getNachname(),
            sozialdienstBenutzerCreateDto.getEmail(),
            List.of(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER)
        );

        mailService.sendBenutzerWelcomeEmail(welcomeMailDto);

        sozialdienstBenutzer.setKeycloakId(keycloakId);

        return sozialdienstBenutzerMapper.toDto(sozialdienstBenutzer);
    }

    @Transactional
    public SozialdienstBenutzerDto updateSozialdienstBenutzer(
        final SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto
    ) {
        SozialdienstBenutzer sozialdienstBenutzer =
            sozialdienstBenutzerRepository.requireById(sozialdienstBenutzerUpdateDto.getId());

        keycloakBenutzerService.updateKeycloakBenutzer(
            sozialdienstBenutzer.getKeycloakId(),
            sozialdienstBenutzerUpdateDto.getVorname(),
            sozialdienstBenutzerUpdateDto.getNachname(),
            null
        );

        return sozialdienstBenutzerMapper
            .toDto(sozialdienstBenutzerMapper.partialUpdate(sozialdienstBenutzerUpdateDto, sozialdienstBenutzer));
    }

    @Transactional
    public void deleteSozialdienstBenutzer(
        final UUID sozialdienstBenutzerId
    ) {
        SozialdienstBenutzer sozialdienstBenutzer =
            sozialdienstBenutzerRepository.requireById(sozialdienstBenutzerId);

        var sozialdienstOpt =
            sozialdienstBenutzerRepository.findSozialdienstBySozialdienstBenutzer(sozialdienstBenutzer);
        if (!sozialdienstOpt.isPresent()) {
            sozialdienstOpt =
                sozialdienstBenutzerRepository.findSozialdienstBySozialdienstAdmin(sozialdienstBenutzer);
        }
        if (!sozialdienstOpt.isPresent()) {
            throw new NotFoundException();
        }
        var sozialdienst = sozialdienstOpt.get();

        sozialdienst.getSozialdienstBenutzers().remove(sozialdienstBenutzer);
        sozialdienstBenutzerRepository.delete(sozialdienstBenutzer);

        keycloakBenutzerService.deleteKeycloakBenutzer(sozialdienstBenutzer.getKeycloakId());
    }
}
