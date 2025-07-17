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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.service.NotificationMapper;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.NotificationDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class BenutzerService {
    private final JsonWebToken jsonWebToken;

    private final BenutzerMapper benutzerMapper;
    private final NotificationMapper notificationMapper;

    private final SachbearbeiterZuordnungStammdatenMapper sachbearbeiterZuordnungStammdatenMapper;
    private final BenutzerRepository benutzerRepository;
    private final SachbearbeiterRepository sachbearbeiterRepository;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final NotificationRepository notificationRepository;
    private final RolleService rolleService;

    private final SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository;
    private final SecurityIdentity identity;

    private final ZuordnungRepository zuordnungRepository;

    @Transactional
    public List<NotificationDto> getNotificationsForCurrentUser() {
        return getNotificationsForUser(getCurrentBenutzer().getId());
    }

    @Transactional
    public List<NotificationDto> getNotificationsForUser(final UUID userId) {
        return notificationRepository.getAllForUser(userId).map(notificationMapper::toDto).toList();
    }

    private Benutzer getBenutzerByKeycloakId(final String keycloakId) {
        final var benutzerOpt = benutzerRepository.findByKeycloakId(keycloakId);
        if (benutzerOpt.isPresent()) {
            return benutzerOpt.get();
        }

        final var sozBenutzerOpt = sozialdienstBenutzerRepository.findByKeycloakId(keycloakId);
        if (sozBenutzerOpt.isPresent()) {
            return sozBenutzerOpt.get();
        }

        final var sachbearbeiterBenutzerOpt = sachbearbeiterRepository.findByKeycloakId(keycloakId);
        if (sachbearbeiterBenutzerOpt.isPresent()) {
            return sachbearbeiterBenutzerOpt.get();
        }

        throw new NotFoundException("Benutzer not found");
    }

    @Transactional
    public Benutzer getCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw AppFailureMessage.missingSubject().create();
        }

        return getBenutzerByKeycloakId(keycloakId);
    }

    @Transactional
    public BenutzerDto getOrCreateAndUpdateCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw AppFailureMessage.missingSubject().create();
        }
        Benutzer benutzer = null;
        try {
            benutzer = getBenutzerByKeycloakId(keycloakId);
        } catch (NotFoundException e) {
            benutzer = createBenutzerFromJWT();
        }

        benutzer = updateBenutzerTypFromJWT(benutzer);
        benutzerRepository.persistAndFlush(benutzer);

        return benutzerMapper.toDto(benutzer);
    }

    @Transactional
    public Benutzer updateBenutzerTypFromJWT(Benutzer benutzer) {
        final var roles = rolleService.mapOrCreateRoles(identity.getRoles());
        benutzer.setRollen(roles);
        return benutzer;
    }

    @Transactional
    public Benutzer createBenutzerFromJWT() {
        Benutzer newBenutzer;
        if (!Collections.disjoint(identity.getRoles(), OidcConstants.POSSIBLE_SB_ROLES)) {
            Sachbearbeiter newSachbearbeiter = new Sachbearbeiter();
            newSachbearbeiter.setKeycloakId(jsonWebToken.getSubject());
            newSachbearbeiter.setVorname(jsonWebToken.getClaim(Claims.given_name));
            newSachbearbeiter.setNachname(jsonWebToken.getClaim(Claims.family_name));
            String email = Objects.isNull(jsonWebToken.getClaim(Claims.email)) ? Constants.DVB_MAILBUCKET_MAIL
                : jsonWebToken.getClaim(Claims.email);
            newSachbearbeiter.setEmail(email);
            newSachbearbeiter.setBenutzerStatus(BenutzerStatus.AKTIV);
            newSachbearbeiter.setBenutzereinstellungen(new Benutzereinstellungen());
            newSachbearbeiter.setFunktionDe("Sachbearbeiter");
            newSachbearbeiter.setFunktionFr("Sachbearbeiter");
            newSachbearbeiter.setTelefonnummer("+41 31 633 83 40");

            sachbearbeiterRepository.persistAndFlush(newSachbearbeiter);
            newBenutzer = newSachbearbeiter;
        } else {
            newBenutzer = new Benutzer();
            newBenutzer.setKeycloakId(jsonWebToken.getSubject());
            newBenutzer.setVorname(jsonWebToken.getClaim(Claims.given_name));
            newBenutzer.setNachname(jsonWebToken.getClaim(Claims.family_name));
            newBenutzer.setBenutzerStatus(BenutzerStatus.AKTIV);
            newBenutzer.setBenutzereinstellungen(new Benutzereinstellungen());

            benutzerRepository.persistAndFlush(newBenutzer);
        }

        return newBenutzer;
    }

    public List<BenutzerDto> getAllSachbearbeitendeMitZuordnungStammdaten() {
        final var sachbearbeiters = sachbearbeiterRepository.findByRolle(OidcConstants.ROLE_SACHBEARBEITER).toList();
        final var sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdatenRepository
            .findForBenutzers(sachbearbeiters.stream().map(AbstractEntity::getId).toList())
            .collect(Collectors.toMap(stammdaten -> stammdaten.getBenutzer().getId(), stammdaten -> stammdaten));

        return sachbearbeiters
            .stream()
            .map(benutzer -> {
                var szs = sachbearbeiterZuordnungStammdaten.get(benutzer.getId());
                if (szs == null) {
                    szs = new SachbearbeiterZuordnungStammdaten();
                }

                final var dto = benutzerMapper.toDto(benutzer);
                dto.setSachbearbeiterZuordnungStammdaten(sachbearbeiterZuordnungStammdatenMapper.toDto(szs));
                return dto;
            })
            .toList();
    }

    public Optional<SachbearbeiterZuordnungStammdatenDto> findSachbearbeiterZuordnungStammdatenWithBenutzerId(UUID id) {
        return sachbearbeiterZuordnungStammdatenRepository.findByBenutzerId(id)
            .map(sachbearbeiterZuordnungStammdatenMapper::toDto);
    }

    @Transactional
    public void createOrUpdateSachbearbeiterStammdaten(
        UUID sachbearbeiterId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto
    ) {
        Sachbearbeiter sachbearbeiter = sachbearbeiterRepository.requireById(sachbearbeiterId);
        SachbearbeiterZuordnungStammdaten sachbearbeiterZuordnungStammdaten =
            sachbearbeiterZuordnungStammdatenRepository.findByBenutzerId(sachbearbeiterId)
                .orElse(new SachbearbeiterZuordnungStammdaten());
        sachbearbeiterZuordnungStammdaten.setBenutzer(sachbearbeiter);
        sachbearbeiterZuordnungStammdatenMapper.partialUpdate(
            sachbearbeiterZuordnungStammdatenDto,
            sachbearbeiterZuordnungStammdaten
        );
        sachbearbeiterZuordnungStammdatenRepository.persist(sachbearbeiterZuordnungStammdaten);
    }

    @Transactional
    public void createOrUpdateSachbearbeiterStammdaten(
        List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdaten
    ) {
        for (final var entry : sachbearbeiterZuordnungStammdaten) {
            createOrUpdateSachbearbeiterStammdaten(entry.getSachbearbeiter(), entry.getZuordnung());
        }
    }

    public String getCurrentBenutzername() {
        return getCurrentBenutzer().getFullName();
    }

    @Transactional
    public void deleteBenutzer(final String benutzerId) {
        final var benutzer = benutzerRepository.findByKeycloakId(benutzerId).orElseThrow(NotFoundException::new);
        benutzer.getRollen().clear();
        final var zuordnungen = zuordnungRepository.findByBenutzerId(benutzer.getId()).toList();
        if (!zuordnungen.isEmpty()) {
            zuordnungRepository.deleteByIds(
                zuordnungen
                    .stream()
                    .map(AbstractEntity::getId)
                    .toList()
            );
        }

        final var buchstabenZuordnung = sachbearbeiterZuordnungStammdatenRepository.findByBenutzerId(benutzer.getId());
        buchstabenZuordnung.ifPresent(sachbearbeiterZuordnungStammdatenRepository::delete);

        benutzerRepository.delete(benutzer);
    }
}
