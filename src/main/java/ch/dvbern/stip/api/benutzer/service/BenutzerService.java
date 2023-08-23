package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerService {

    private final JsonWebToken jsonWebToken;

    private final BenutzerMapper benutzerMapper;
    private final BenutzerRepository benutzerRepository;


    public BenutzerDto getCurrentBenutzer() {
        return benutzerMapper.toDto(getOrCreateCurrentBenutzer());
    }

    public Benutzer getOrCreateCurrentBenutzer() {
        final var keycloakId = jsonWebToken.getSubject();

        if (keycloakId == null) {
            throw new BadRequestException(); // TODO: use error handling
        }

        return benutzerRepository
                .findByKeycloakId(keycloakId)
                .orElseGet(this::createBenutzerFromJWT);
    }

    @Transactional
    public Benutzer createBenutzerFromJWT() {
        Benutzer newBenutzer = new Benutzer();
        newBenutzer.setKeycloakId(jsonWebToken.getSubject());
        newBenutzer.setVorname(jsonWebToken.getClaim(Claims.given_name));
        newBenutzer.setNachname(jsonWebToken.getClaim(Claims.family_name));
        newBenutzer.setSozialversicherungsnummer(jsonWebToken.getClaim(OidcConstants.CLAIM_AHV_NUMMER));
        newBenutzer.setBenutzerStatus(BenutzerStatus.AKTIV);

        benutzerRepository.persist(newBenutzer);
        return newBenutzer;
    }

    public Optional<BenutzerDto> getBenutzer(UUID id) {
        var optionalFall = benutzerRepository.findByIdOptional(id);
        return optionalFall.map(benutzerMapper::toDto);
    }

    public List<BenutzerDto> getAllBenutzer() {
        return benutzerRepository.findAll().stream().map(benutzerMapper::toDto).toList();
    }

    @Transactional
    public void updateCurrentBenutzer(BenutzerUpdateDto benutzerUpdateDto) {
        final var benutzer = getOrCreateCurrentBenutzer();
        benutzerMapper.partialUpdate(benutzerUpdateDto, benutzer);
    }
}
