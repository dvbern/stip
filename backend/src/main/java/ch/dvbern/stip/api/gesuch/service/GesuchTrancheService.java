package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
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
import lombok.Getter;
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
        final var newTrancheRange = TrancheRange.from(newTranche);
        for (final var existingTranche : gesuch.getGesuchTranchen()) {
            final var existingTrancheRange = TrancheRange.from(existingTranche);

            OverlapType overlaps = newTrancheRange.overlaps(existingTrancheRange);
            if (overlaps == OverlapType.FULL || overlaps == OverlapType.EXACT) {
                handleFull(existingTranche);
            } else if (overlaps == OverlapType.LEFT || overlaps == OverlapType.LEFT_FULL) {
                handleLeft(existingTranche, newTranche);
            } else if (overlaps == OverlapType.RIGHT || overlaps == OverlapType.RIGHT_FULL) {
                handleRight(existingTranche, newTranche);
            }
        }

        final var toRemove = new ArrayList<GesuchTranche>();
        for (final var tranche : gesuch.getGesuchTranchen()) {
            if (tranche.getGueltigkeit().getMonths() <= 0) {
                toRemove.add(tranche);
                gesuchTrancheRepository.delete(tranche);
            }
        }

        gesuch.getGesuchTranchen().removeAll(toRemove);
    }

    /**
     * Set Gueltigkeit on existing tranche to a date range with less than 1 month, so it's cleaned up
     */
    void handleFull(final GesuchTranche existingTranche) {
        existingTranche.setGueltigkeit(new DateRange());
    }

    /**
     * Truncate existing tranche end date
     */
    void handleLeft(final GesuchTranche existingTranche, final GesuchTranche newTranche) {
        existingTranche.getGueltigkeit()
            .setGueltigBis(newTranche.getGueltigkeit()
                .getGueltigAb()
                .minusMonths(1)
                .with(lastDayOfMonth())
            );
    }

    /**
     * Truncate existing tranche start date
     */
    void handleRight(final GesuchTranche existingTranche, final GesuchTranche newTranche) {
        existingTranche.getGueltigkeit()
            .setGueltigAb(newTranche.getGueltigkeit()
                .getGueltigBis()
                .plusMonths(1)
                .with(firstDayOfMonth())
            );
    }

    enum OverlapType {
        EXACT,
        FULL,
        LEFT,
        LEFT_FULL,
        RIGHT,
        RIGHT_FULL,
        NONE
    }

    enum ConsecutiveType {
        BEFORE,
        AFTER,
        NONE
    }

    @Getter
    static class TrancheRange {
        private LocalDate von;
        private LocalDate bis;
        private GesuchTranche tranche;

        private TrancheRange(final GesuchTranche tranche, final LocalDate von, final LocalDate bis) {
            this.tranche = tranche;
            this.von = von;
            this.bis = bis;
        }

        public static TrancheRange from(final GesuchTranche tranche) {
            return new TrancheRange(
                tranche,
                tranche.getGueltigkeit().getGueltigAb(),
                tranche.getGueltigkeit().getGueltigBis()
            );
        }

        public OverlapType overlaps(final TrancheRange other) {
            if (getVon().equals(other.getVon()) && getBis().equals(other.getBis())) {
                return OverlapType.EXACT;
            }

            if (other.getVon().isBefore(getVon()) && other.getBis().isAfter(getVon())) {
                if (other.getBis().isBefore(getBis())) {
                    return OverlapType.LEFT;
                } else if (other.getBis().isEqual(getBis())) {
                    return OverlapType.LEFT_FULL;
                }
            }

            if (other.getVon().isBefore(getBis()) && other.getBis().isAfter(getBis())) {
                if (other.getVon().isAfter(getVon())) {
                    return OverlapType.RIGHT;
                } else if (other.getVon().isEqual(getVon())) {
                    return OverlapType.RIGHT_FULL;
                }
            }

            if (DateUtil.beforeOrEqual(getVon(), other.getVon()) && DateUtil.afterOrEqual(getBis(), other.getBis())) {
                return OverlapType.FULL;
            }

            return OverlapType.NONE;
        }

        public ConsecutiveType consecutive(final TrancheRange other) {
            if (getVon().isEqual(other.getBis())) {
                return ConsecutiveType.BEFORE;
            } else if (getBis().isEqual(other.getVon())) {
                return ConsecutiveType.AFTER;
            }

            return ConsecutiveType.NONE;
        }
    }
}
