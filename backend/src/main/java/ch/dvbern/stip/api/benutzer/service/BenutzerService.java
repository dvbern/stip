package ch.dvbern.stip.api.benutzer.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import ch.dvbern.stip.generated.dto.BenutzerDto;
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

    private final SachbearbeiterZuordnungStammdatenMapper sachbearbeiterZuordnungStammdatenMapper;
    private final BenutzerRepository benutzerRepository;
    private final RolleService rolleService;

    private final SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository;
    private final SecurityIdentity identity;

    private final ZuordnungRepository zuordnungRepository;

    @Transactional
    public Benutzer getCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw AppFailureMessage.missingSubject().create();
        }

        return benutzerRepository
            .findByKeycloakId(keycloakId)
            .orElseThrow(() -> new NotFoundException("Benutzer not found"));
    }

    @Transactional
    public BenutzerDto getOrCreateAndUpdateCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw AppFailureMessage.missingSubject().create();
        }

        Benutzer benutzer = benutzerRepository
            .findByKeycloakId(keycloakId)
            .orElseGet(this::createBenutzerFromJWT);
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
        Benutzer newBenutzer = new Benutzer();
        newBenutzer.setKeycloakId(jsonWebToken.getSubject());
        newBenutzer.setVorname(jsonWebToken.getClaim(Claims.given_name));
        newBenutzer.setNachname(jsonWebToken.getClaim(Claims.family_name));
        newBenutzer.setSozialversicherungsnummer(jsonWebToken.getClaim(OidcConstants.CLAIM_AHV_NUMMER));
        newBenutzer.setBenutzerStatus(BenutzerStatus.AKTIV);
        newBenutzer.setBenutzereinstellungen(new Benutzereinstellungen());

        benutzerRepository.persistAndFlush(newBenutzer);
        return newBenutzer;
    }

    public List<BenutzerDto> getAllSachbearbeitendeMitZuordnungStammdaten() {
        final var benutzers = benutzerRepository.findByRolle(OidcConstants.ROLE_SACHBEARBEITER).toList();
        final var sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdatenRepository
            .findForBenutzers(benutzers.stream().map(AbstractEntity::getId).toList())
            .collect(Collectors.toMap(stammdaten -> stammdaten.getBenutzer().getId(), stammdaten -> stammdaten));

        return benutzers
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
        UUID benutzerId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto) {
        Benutzer benutzer = benutzerRepository.requireById(benutzerId);
        SachbearbeiterZuordnungStammdaten sachbearbeiterZuordnungStammdaten =
            sachbearbeiterZuordnungStammdatenRepository.findByBenutzerId(benutzerId)
                .orElse(new SachbearbeiterZuordnungStammdaten());
        sachbearbeiterZuordnungStammdaten.setBenutzer(benutzer);
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
            zuordnungRepository.deleteByIds(zuordnungen
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
