package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
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
    private final GesuchMapperUtil gesuchMapperUtil;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final RequiredDokumentService requiredDokumentService;
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchDokumentRepository gesuchDokumentRepository;

    @Transactional
    public GesuchDto createAenderungsantrag(
        final UUID gesuchId,
        final CreateAenderungsantragRequestDto aenderungsantragCreateDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getCurrentGesuchTranche();
        final var newTranche = GesuchTrancheCopyUtil.createAenderungstranche(trancheToCopy, aenderungsantragCreateDto);
        gesuch.getGesuchTranchen().add(newTranche);

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

    public List<GesuchTrancheSlimDto> getAllTranchenForGesuch(final UUID gesuchId) {
        return gesuchTrancheRepository.findForGesuch(gesuchId).map(gesuchTrancheMapper::toSlimDto).toList();
    }

    public List<DokumentTyp> getRequiredDokumentTypes(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        return requiredDokumentService.getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular());
    }

    @Transactional
    public List<GesuchDokumentDto> getAndCheckGesuchDokumentsForGesuchTranche(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);

        removeSuperfluousDokumentsForGesuch(gesuchTranche.getGesuchFormular());
        return getGesuchDokumenteForGesuchTranche(gesuchTrancheId);
    }

    public void removeSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        List<String> dokumentObjectIds = new ArrayList<>();

        final var superfluousGesuchDokuments = requiredDokumentService.getSuperfluousDokumentsForGesuch(formular);

        superfluousGesuchDokuments.forEach(
            gesuchDokument -> {
                // Make a copy to avoid ConcurrentModificationException
                final var dokumente = new ArrayList<>(gesuchDokument.getDokumente());
                dokumente.forEach(
                    dokument -> {
                        final var dokumentObjectId = gesuchDokumentService.deleteDokument(dokument.getId());
                        if (dokument.getGesuchDokumente().isEmpty()) {
                            dokumentObjectIds.add(dokumentObjectId);
                        }
                    }
                );
            }
        );

        for (final var gesuchDokument : superfluousGesuchDokuments) {
            formular.getTranche().getGesuchDokuments().remove(gesuchDokument);
        }

        if (!dokumentObjectIds.isEmpty()) {
            gesuchDokumentService.executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    @Transactional
    public GesuchDokumentDto getGesuchDokument(final UUID gesuchTrancheId, final DokumentTyp dokumentTyp) {
        return gesuchDokumentMapper.toDto(
            gesuchDokumentRepository.findByGesuchTrancheAndDokumentType(gesuchTrancheId, dokumentTyp)
                .orElseThrow(NotFoundException::new)
        );
    }


    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuchTranche(final UUID gesuchTrancheId) {
        return gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId).map(gesuchDokumentMapper::toDto).toList();
    }

    @Transactional
    public GesuchDto createTrancheCopy(
        final UUID gesuchId,
        final CreateGesuchTrancheRequestDto createDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getTrancheValidOnDate(createDto.getStart())
            .orElseThrow(NotFoundException::new);
        final var newTranche = GesuchTrancheCopyUtil.createNewTranche(
            trancheToCopy,
            new DateRange(createDto.getStart(), createDto.getEnd()),
            createDto.getComment()
        );
        gesuch.getGesuchTranchen().add(newTranche);
        gesuchRepository.persistAndFlush(gesuch);

        // Truncate existing Tranche(n) before setting the Gesuch on the new one
        // Truncating also removes all tranchen no longer needed (i.e. those with gueltigkeit <= 0 months)
        truncateExistingTranchen(gesuch, newTranche);

        return gesuchMapperUtil.mapWithTranche(gesuch, newTranche);
    }

    void truncateExistingTranchen(final Gesuch gesuch, final GesuchTranche newTranche) {
        final var newTrancheRange = TrancheRange.from(newTranche);
        final var added = new ArrayList<GesuchTranche>();
        final var tranchenToTruncate = gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS)
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
