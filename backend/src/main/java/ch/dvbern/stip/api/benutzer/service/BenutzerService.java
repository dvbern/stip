package ch.dvbern.stip.api.benutzer.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzer.type.BenutzerTyp;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.benutzereinstellungen.repo.BenutzereinstellungenRepository;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerService {

    private static final Logger LOG = Logger.getLogger(BenutzerService.class);

    private final JsonWebToken jsonWebToken;

    private final BenutzerMapper benutzerMapper;

    private final SachbearbeiterZuordnungStammdatenMapper sachbearbeiterZuordnungStammdatenMapper;
    private final BenutzerRepository benutzerRepository;
    private final BenutzereinstellungenRepository benutzereinstellungenRepository;

    private final SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository;
    private final SecurityIdentity identity;

    @Transactional
    public BenutzerDto getCurrentBenutzer() {
        return benutzerMapper.toDto(getOrCreateCurrentBenutzer());
    }

    @SuppressWarnings("java:S1135")
    @Transactional
    public Benutzer getOrCreateCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            // TODO KSTIP-782: use error handling and remove SuppressWarnings
            throw new BadRequestException();
        }
        Benutzer benutzer = benutzerRepository
            .findByKeycloakId(keycloakId)
            .orElseGet(this::createBenutzerFromJWT);
        benutzer = updateBenutzerTypFromJWT(benutzer);
        benutzerRepository.persist(benutzer);

        return benutzer;
    }

    @Transactional
    public Benutzer updateBenutzerTypFromJWT(Benutzer benutzer) {
        if (identity.hasRole(BenutzerTyp.GESUCHSTELLER.getRoleName())) {
            benutzer.setBenutzerTyp(BenutzerTyp.GESUCHSTELLER);
        } else if (identity.hasRole(BenutzerTyp.SACHBEARBEITER.getRoleName())) {
            benutzer.setBenutzerTyp(BenutzerTyp.SACHBEARBEITER);
        } else if (identity.hasRole(BenutzerTyp.ADMIN.getRoleName())) {
            benutzer.setBenutzerTyp(BenutzerTyp.ADMIN);
        } else {
            LOG.error("A user without a role has signed in, they are (by default) allowed as Gesuchsteller");
        }

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

        benutzerRepository.persist(newBenutzer);
        return newBenutzer;
    }

    public Optional<BenutzerDto> getBenutzer(UUID id) {
        var optionalFall = benutzerRepository.findByIdOptional(id);
        return optionalFall.map(benutzerMapper::toDto);
    }

    @Transactional
    public List<BenutzerDto> getAllBenutzer() {
        return benutzerRepository.findAll().stream().map(benutzerMapper::toDto).toList();
    }

    public List<BenutzerDto> getAllSachbearbeitendeMitZuordnungStammdaten() {
        List<BenutzerDto> benutzerDtoList =
            benutzerRepository.findByBenutzerTyp(BenutzerTyp.SACHBEARBEITER).map(benutzerMapper::toDto).toList();
        benutzerDtoList.forEach(benutzerDto -> {
            sachbearbeiterZuordnungStammdatenRepository.findByBenutzerId(benutzerDto.getId())
                .ifPresent(sachbearbeiterZuordnungStammdaten -> benutzerDto.setSachbearbeiterZuordnungStammdaten(
                    sachbearbeiterZuordnungStammdatenMapper.toDto(sachbearbeiterZuordnungStammdaten)));
        });
        return benutzerDtoList;
    }

    @Transactional
    public void updateCurrentBenutzer(BenutzerUpdateDto benutzerUpdateDto) {
        final var benutzer = getOrCreateCurrentBenutzer();
        benutzerMapper.partialUpdate(benutzerUpdateDto, benutzer);
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
}
