package ch.dvbern.stip.api.gesuch.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheService {
    private final GesuchRepository gesuchRepository;
    private final Validator validator;
    private final GesuchMapperUtil gesuchMapperUtil;

    @Transactional
    public GesuchDto createAenderungsantrag(
        final UUID gesuchId,
        final CreateAenderungsantragRequestDto aenderungsantragCreateDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getCurrentGesuchTranche();
        final var newTranche = GesuchTrancheCopyUtil.createAenderungstranche(trancheToCopy, aenderungsantragCreateDto);
        gesuch.getGesuchTranchen().add(newTranche);

        final var violations = validator.validate(gesuch);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }

        // Manually persist so that when mapping happens the IDs on the new objects are set
        gesuchRepository.persistAndFlush(gesuch);
        return gesuchMapperUtil.mapWithTranche(gesuch, newTranche);
    }

    public GesuchDto getAenderungsantrag(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuchMapperUtil.mapWithTranche(
            gesuch,
            gesuch.getGesuchTranchen()
                .stream()
                .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS)
                .findFirst()
                .orElseThrow(NotFoundException::new)
        );
    }
}
