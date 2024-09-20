package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheService {
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchMapperUtil gesuchMapperUtil;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final GesuchFormularService gesuchFormularService;
    private final RequiredDokumentService requiredDokumentService;
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheTruncateService gesuchTrancheTruncateService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;

    public GesuchDto getAenderungsantrag(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuchMapperUtil.mapWithTranche(
            gesuch,
            gesuch.getGesuchTranchen()
                .stream()
                .filter(tranche ->
                    tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS &&
                    tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                )
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
        return gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId)
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public ValidationReportDto validatePages(final UUID gesuchTrancheId) {
        final var gesuchFormular = gesuchTrancheRepository.requireById(gesuchTrancheId).getGesuchFormular();
        if (gesuchFormular == null) {
            throw new NotFoundException();
        }
        return gesuchFormularService.validatePages(gesuchFormular);
    }

    @Transactional
    public GesuchTrancheDto createAenderungsantrag(
        final UUID gesuchId,
        final CreateAenderungsantragRequestDto aenderungsantragCreateDto
    ) {
        if(openAenderungAlreadyExists(gesuchId)){
            throw new ForbiddenException();
        }
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getCurrentGesuchTranche();

        final var newTranche = GesuchTrancheCopyUtil.createAenderungstranche(trancheToCopy, aenderungsantragCreateDto);
        gesuch.getGesuchTranchen().add(newTranche);

        // Manually persist so that when mapping happens the IDs on the new objects are set
        gesuchRepository.persistAndFlush(gesuch);
        return gesuchTrancheMapper.toDto(newTranche);
    }

    @Transactional
    public GesuchTrancheDto createTrancheCopy(
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
        gesuchTrancheTruncateService.truncateExistingTranchen(gesuch, newTranche);

        return gesuchTrancheMapper.toDto(newTranche);
    }

    @Transactional
    public void aenderungEinbinden(
        final GesuchTranche aenderung
    ) {
        final var gesuch = aenderung.getGesuch();
        final var newTranche = GesuchTrancheCopyUtil.createNewTranche(aenderung);
        gesuch.getGesuchTranchen().add(newTranche);
        gesuchRepository.persistAndFlush(gesuch);

        gesuchTrancheTruncateService.truncateExistingTranchen(gesuch, newTranche);
    }

    @Transactional
    public void aenderungEinreichen(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService.triggerStateMachineEvent(aenderung, GesuchTrancheStatusChangeEvent.UEBERPRUEFEN);
    }

    public boolean openAenderungAlreadyExists(UUID gesuchId){
        final var tranchenAndAenderungen = getAllTranchenForGesuch(gesuchId);
        return tranchenAndAenderungen != null
            && tranchenAndAenderungen.stream().filter(item -> item.getTyp() == GesuchTrancheTyp.AENDERUNG)
            .anyMatch(item -> item.getStatus() != GesuchTrancheStatus.AKZEPTIERT)
            && tranchenAndAenderungen.stream().filter(item -> item.getTyp() == GesuchTrancheTyp.AENDERUNG)
            .anyMatch(item -> item.getStatus() != GesuchTrancheStatus.ABGELEHNT);
    }

}
