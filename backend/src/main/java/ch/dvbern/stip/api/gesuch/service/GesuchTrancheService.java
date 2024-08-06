package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheService {
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
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

        final var violations = validator.validate(gesuch, GesuchEinreichenValidationGroup.class);
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

    public GesuchDto createTrancheCopy(
        final UUID gesuchId,
        final UUID originalTrancheId,
        final CreateGesuchTrancheRequestDto createDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getGesuchTrancheById(originalTrancheId).orElseThrow(NotFoundException::new);
        final var newTranche = GesuchTrancheCopyUtil.createTranche(trancheToCopy, createDto);

        // Truncate existing Tranche(n) before setting the Gesuch on the new one
        truncateExistingTranchen(gesuch, newTranche);
        newTranche.setGesuch(gesuch);

        final var violations = validator.validate(gesuch, GesuchEinreichenValidationGroup.class);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }

        gesuchRepository.persistAndFlush(gesuch);
        return null;
    }

    void truncateExistingTranchen(final Gesuch gesuch, final GesuchTranche newTranche) {
        for (final var existingTranche : gesuch.getGesuchTranchen()) {
            // Check if any Tranche is "cut in half" by the new tranche
            if (newTranche.getGueltigkeit().getGueltigAb().isAfter(existingTranche.getGueltigkeit().getGueltigAb()) &&
                newTranche.getGueltigkeit().getGueltigBis().isBefore(existingTranche.getGueltigkeit().getGueltigBis())
            ) {
                // Cut in half found, create a truncated copy
                final var copyGueltigkeit = new DateRange(
                    newTranche.getGueltigkeit().getGueltigBis().plusMonths(1).with(firstDayOfMonth()),
                    existingTranche.getGueltigkeit().getGueltigBis()
                );
                final var copyOfExisting = GesuchTrancheCopyUtil.copyTranche(
                    existingTranche,
                    copyGueltigkeit,
                    null
                );

                gesuch.getGesuchTranchen().add(copyOfExisting);

                // Truncate existing
                existingTranche.getGueltigkeit().setGueltigBis(newTranche
                    .getGueltigkeit()
                    .getGueltigAb()
                    .minusMonths(1)
                    .with(lastDayOfMonth())
                );
            } else if (
                // If start and end of newTranche equal an existing tranche
                newTranche.getGueltigkeit().getGueltigAb().equals(existingTranche.getGueltigkeit().getGueltigAb())
                && newTranche.getGueltigkeit().getGueltigBis().equals(existingTranche.getGueltigkeit().getGueltigBis())
            ) {
                // Set a 0-day gueltigkeit so this Tranche is removed in the cleanup
                existingTranche.setGueltigkeit(new DateRange());
                // Break because no other truncation needs to be made, the new tranche has replaced an existing one
                break;
            } else if (
                // If the new tranche starts after an existing one and no tranchen are between
                newTranche.getGueltigkeit().getGueltigAb().isAfter(existingTranche.getGueltigkeit().getGueltigAb())
                && gesuch.getGesuchTranchen()
                    .stream()
                    .noneMatch(tranche -> tranche.getGueltigkeit()
                        .contains(newTranche.getGueltigkeit().getGueltigAb(), true)
                    )
            ) {
                // Truncate end of existing tranche
                existingTranche.getGueltigkeit()
                    .setGueltigBis(newTranche.getGueltigkeit()
                        .getGueltigAb()
                        .minusMonths(1)
                        .with(lastDayOfMonth())
                    );

                // Find followUp tranche that contains the new endDate
                final var followUpOverlap = gesuch
                    .getGesuchTranchen()
                    .stream()
                    .filter(tranche -> tranche.getGueltigkeit().contains(newTranche.getGueltigkeit().getGueltigBis()))
                    .findFirst();

                // Find "skipped" tranchen between
                gesuch
                    .getGesuchTranchen()
                    .stream()
                    .filter(tranche -> {
                        var trancheStartAfter = DateUtil.afterOrEqual(
                            tranche.getGueltigkeit().getGueltigAb(),
                            newTranche.getGueltigkeit().getGueltigAb()
                        );

                        var trancheEndBefore = DateUtil.beforeOrEqual(
                            tranche.getGueltigkeit().getGueltigBis(),
                            newTranche.getGueltigkeit().getGueltigBis()
                        );

                        // A "skipped" tranche is one where the new tranche starts before or on
                        // and ends after or on a given tranche
                        return trancheStartAfter && trancheEndBefore;
                    })
                    .forEach(tranche -> tranche.setGueltigkeit(new DateRange()));

                // If a followUp that overlaps was found, then
                // truncate the start date to the first of next month of the new end date
                followUpOverlap.ifPresent(tranche -> tranche
                    .getGueltigkeit()
                    .setGueltigAb(newTranche.getGueltigkeit()
                        .getGueltigBis()
                        .plusMonths(1)
                        .with(firstDayOfMonth())
                    ));
            }
        }

        // Clean tranchen with less than 1 month Gueltigkeit
        final var toRemove = new ArrayList<GesuchTranche>();
        for (final var gesuchTranche : gesuch.getGesuchTranchen()) {
            if (gesuchTranche.getGueltigkeit().getMonths() <= 0) {
                gesuchTrancheRepository.delete(gesuchTranche);
                toRemove.add(gesuchTranche);
            }
        }

        gesuch.getGesuchTranchen().removeAll(toRemove);
    }
}
