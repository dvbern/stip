package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.ArrayList;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheTruncateService {
    private final GesuchTrancheRepository gesuchTrancheRepository;

    void truncateExistingTranchen(final Gesuch gesuch, final GesuchTranche newTranche) {
        final var newTrancheRange = TrancheRange.from(newTranche);
        final var added = new ArrayList<GesuchTranche>();
        final var tranchenToTruncate = gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS &&
                tranche.getTyp() == GesuchTrancheTyp.TRANCHE
            )
            .toList();

        for (final var existingTranche : tranchenToTruncate) {
            if (newTranche.getId().equals(existingTranche.getId())) {
                continue;
            }

            final var existingTrancheRange = TrancheRange.from(existingTranche);

            final var overlaps = newTrancheRange.overlaps(existingTrancheRange);
            if (overlaps == OverlapType.FULL || overlaps == OverlapType.EXACT) {
                handleFull(existingTranche);
            } else if (overlaps == OverlapType.LEFT || overlaps == OverlapType.LEFT_FULL) {
                handleLeft(existingTranche, newTranche);
            } else if (overlaps == OverlapType.RIGHT || overlaps == OverlapType.RIGHT_FULL) {
                handleRight(existingTranche, newTranche);
            } else if (overlaps == OverlapType.INSIDE) {
                added.add(handleInside(existingTranche, newTranche));
            }
        }

        gesuch.getGesuchTranchen().addAll(added);

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
        existingTranche.setGueltigkeit(new DateRange(LocalDate.now(), LocalDate.now()));
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

    /**
     * Copy existing tranche and truncate one end and one start
     */
    GesuchTranche handleInside(final GesuchTranche existingTranche, final GesuchTranche newTranche) {
        final var copyGueltigkeit = new DateRange(
            newTranche.getGueltigkeit().getGueltigBis().plusMonths(1).with(firstDayOfMonth()),
            existingTranche.getGueltigkeit().getGueltigBis()
        );

        // Truncate existing
        existingTranche.getGueltigkeit().setGueltigBis(newTranche
            .getGueltigkeit()
            .getGueltigAb()
            .minusMonths(1)
            .with(lastDayOfMonth())
        );

        return GesuchTrancheCopyUtil.createNewTranche(
            existingTranche,
            copyGueltigkeit,
            existingTranche.getComment()
        );
    }

    enum OverlapType {
        EXACT,
        FULL,
        LEFT,
        LEFT_FULL,
        RIGHT,
        RIGHT_FULL,
        INSIDE,
        NONE
    }

    @Getter
    static class TrancheRange {
        private final LocalDate von;
        private final LocalDate bis;

        private TrancheRange(final LocalDate von, final LocalDate bis) {
            this.von = von;
            this.bis = bis;
        }

        public static TrancheRange from(final GesuchTranche tranche) {
            return new TrancheRange(
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

            if (DateUtil.afterOrEqual(getVon(), other.getVon()) && DateUtil.beforeOrEqual(getBis(), other.getBis())) {
                return OverlapType.INSIDE;
            }

            return OverlapType.NONE;
        }
    }
}
