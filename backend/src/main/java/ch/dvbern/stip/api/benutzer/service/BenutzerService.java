package ch.dvbern.stip.api.benutzer.service;

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
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	public BenutzerDto getCurrentBenutzer() {
		return benutzerMapper.toDto(getOrCreateCurrentBenutzer());
	}

    @SuppressWarnings("java:S1135")
	public Benutzer getOrCreateCurrentBenutzer() {
		final var keycloakId = jsonWebToken.getSubject();

		if (keycloakId == null) {
            // TODO KSTIP-782: use error handling and remove SuppressWarnings
			throw new BadRequestException();
		}
		Benutzer benutzer = benutzerRepository
				.findByKeycloakId(keycloakId)
				.orElseGet(this::createBenutzerFromJWT);
		benutzer = updateBenutzerTypFromJWT(benutzer, jsonWebToken);

		return benutzer;
	}

	@Transactional
	public Benutzer updateBenutzerTypFromJWT(Benutzer benutzer, JsonWebToken jsonWebToken) {
		HashSet<String> group = jsonWebToken.getClaim(Claims.groups);
		if(!group.isEmpty()) {
			String groupOnly = group.iterator().next().toUpperCase();
			if (!groupOnly.equals(benutzer.getBenutzerTyp().name())) {
				benutzer = benutzerRepository.findById(benutzer.getId());
				benutzer.setBenutzerTyp(BenutzerTyp.valueOf(groupOnly));
				benutzerRepository.persist(benutzer);
			}
		}
		else {
			LOG.warn("Einen Benutzer ohne Rollen wuerde angemeldet, er ist per default als Gesuchsteller erlaubt");
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
				sachbearbeiterZuordnungStammdaten);
		sachbearbeiterZuordnungStammdatenRepository.persist(sachbearbeiterZuordnungStammdaten);
	}
}
