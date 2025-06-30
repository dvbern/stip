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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.time.LocalDate;
import java.util.ArrayList;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheCopyUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheTruncateService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentKommentarService gesuchDokumentKommentarService;
    private final GesuchDokumentService gesuchDokumentService;
    private final DokumentRepository dokumentRepository;

    void truncateExistingTranchen(final Gesuch gesuch, final GesuchTranche newTranche) {
        final var newTrancheRange = TrancheRange.from(newTranche);
        final var added = new ArrayList<GesuchTranche>();
        final var tranchenToTruncate = gesuch.getGesuchTranchen()
            .stream()
            .filter(
                tranche -> tranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS &&
                tranche.getTyp() == GesuchTrancheTyp.TRANCHE
            )
            .toList();

        for (final var existingTranche : tranchenToTruncate) {
            if (newTranche.getId().equals(existingTranche.getId())) {
                continue;
            }

            final var existingTrancheRange = TrancheRange.from(existingTranche);

            final var overlaps = newTrancheRange.overlaps(existingTrancheRange);
            switch (overlaps) {
                case FULL, EXACT -> handleFull(existingTranche);
                case LEFT, LEFT_FULL -> handleLeft(existingTranche, newTranche);
                case RIGHT, RIGHT_FULL -> handleRight(existingTranche, newTranche);
                case INSIDE -> {
                    final var newNewTranche = handleInside(existingTranche, newTranche);
                    added.add(newNewTranche);
                }
                case NONE -> {
                    // No action
                }
            }
        }

        gesuch.getGesuchTranchen().addAll(added);

        final var toRemove = new ArrayList<GesuchTranche>();
        final var tranchenToCheck = new ArrayList<GesuchTranche>();
        tranchenToCheck.addAll(
            gesuch.getGesuchTranchen()
                .stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
                .toList()
        );

        for (final var tranche : tranchenToCheck) {
            if (tranche.getGueltigkeit().months() <= 0) {
                toRemove.add(tranche);
                gesuchDokumentKommentarService.deleteForGesuchTrancheId(tranche.getId());

                var gesuchDokuments = new ArrayList<GesuchDokument>();
                gesuchDokuments.addAll(tranche.getGesuchDokuments());
                for (var dokument : gesuchDokuments) {
                    gesuchDokumentService.removeGesuchDokument(dokument);
                    tranche.getGesuchDokuments().remove(dokument);
                }

                gesuch.getGesuchTranchen().remove(tranche);
                gesuchTrancheRepository.delete(tranche);
            }
        }
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
            .setGueltigBis(
                newTranche.getGueltigkeit()
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
            .setGueltigAb(
                newTranche.getGueltigkeit()
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

        final var newNewTranche = GesuchTrancheCopyUtil.createNewTranche(
            existingTranche,
            copyGueltigkeit,
            existingTranche.getComment()
        );

        // Truncate existing
        existingTranche.getGueltigkeit()
            .setGueltigBis(
                newTranche
                    .getGueltigkeit()
                    .getGueltigAb()
                    .minusMonths(1)
                    .with(lastDayOfMonth())
            );
        gesuchTrancheRepository.persist(newNewTranche);
        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(existingTranche, newNewTranche);

        return newNewTranche;
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
