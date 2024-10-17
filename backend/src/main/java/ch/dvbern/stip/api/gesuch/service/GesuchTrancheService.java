package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapper;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
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
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final GesuchFormularService gesuchFormularService;
    private final RequiredDokumentService requiredDokumentService;
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchTrancheTruncateService gesuchTrancheTruncateService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;
    private final GesuchTrancheValidatorService gesuchTrancheValidatorService;
    private final PersonInAusbildungMapper personInAusbildungMapper;
    private final AusbildungMapper ausbildungMapper;
    private final FamiliensituationMapper familiensituationMapper;
    private final PartnerMapper partnerMapper;
    private final ElternMapper elternMapper;
    private final AuszahlungMapper auszahlungMapper;
    private final EinnahmenKostenMapper einnahmenKostenMapper;
    private final LebenslaufItemMapper lebenslaufItemMapper;
    private final GeschwisterMapper geschwisterMapper;
    private final KindMapper kindMapper;
    private final SteuerdatenMapper steuerdatenMapper;

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
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (openAenderungAlreadyExists(gesuch)) {
            throw new ForbiddenException();
        }

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

    @Transactional
    public GesuchTrancheDto aenderungAkzeptieren(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService.triggerStateMachineEvent(aenderung, GesuchTrancheStatusChangeEvent.AKZETPIERT);

        final var newTranche = gesuchTrancheRepository.findMostRecentCreatedTranche(aenderung.getGesuch());
        return gesuchTrancheMapper.toDto(newTranche.orElseThrow(NotFoundException::new));
    }

    @Transactional
    public GesuchTrancheDto aenderungAblehnen(final UUID aenderungId, KommentarDto kommentarDto) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService.triggerStateMachineEventWithComment(
            aenderung,
            GesuchTrancheStatusChangeEvent.ABLEHNEN,
            kommentarDto
        );

        final var lastFreigegebenFormular = gesuchTrancheHistoryRepository.getLatestWhereStatusChanged(aenderungId).getGesuchFormular();

        var gesuchTrancheUpdateDto = new GesuchTrancheUpdateDto().id(
            aenderungId
        );
        var gesuchFormularUpdateDto = new GesuchFormularUpdateDto();
        gesuchTrancheUpdateDto.setGesuchFormular(gesuchFormularUpdateDto);

        gesuchFormularUpdateDto.setPersonInAusbildung(personInAusbildungMapper.toUpdateDto(lastFreigegebenFormular.getPersonInAusbildung()));
        gesuchFormularUpdateDto.setAusbildung(ausbildungMapper.toUpdateDto(lastFreigegebenFormular.getAusbildung())
            .ausbildungsgangId(lastFreigegebenFormular.getAusbildung().getAusbildungsgang().getId()));
        gesuchFormularUpdateDto.setFamiliensituation(familiensituationMapper.toUpdateDto(lastFreigegebenFormular.getFamiliensituation()));
        gesuchFormularUpdateDto.setPartner(partnerMapper.toUpdateDto(lastFreigegebenFormular.getPartner()));
        gesuchFormularUpdateDto.setElterns(new ArrayList<>(List.of()));
        for (final var eltern : lastFreigegebenFormular.getElterns()) {
            gesuchFormularUpdateDto.getElterns().add(elternMapper.toUpdateDto(eltern));
        }
        gesuchFormularUpdateDto.setAuszahlung(auszahlungMapper.toUpdateDto(lastFreigegebenFormular.getAuszahlung()));
        gesuchFormularUpdateDto.setEinnahmenKosten(einnahmenKostenMapper.toUpdateDto(lastFreigegebenFormular.getEinnahmenKosten()));
        gesuchFormularUpdateDto.setLebenslaufItems(new ArrayList<>(List.of()));
        for (final var lebenslaufItem : lastFreigegebenFormular.getLebenslaufItems()) {
            gesuchFormularUpdateDto.getLebenslaufItems().add(lebenslaufItemMapper.toUpdateDto(lebenslaufItem));
        }
        gesuchFormularUpdateDto.setGeschwisters(new ArrayList<>(List.of()));
        for (final var geschwister : lastFreigegebenFormular.getGeschwisters()) {
            gesuchFormularUpdateDto.getGeschwisters().add(geschwisterMapper.toUpdateDto(geschwister));
        }
        gesuchFormularUpdateDto.setKinds(new ArrayList<>(List.of()));
        for (final var kind : lastFreigegebenFormular.getKinds()) {
            gesuchFormularUpdateDto.getKinds().add(kindMapper.toUpdateDto(kind).id(null));
        }
        gesuchFormularUpdateDto.setSteuerdaten(new ArrayList<>(List.of()));
        for (final var steuerdaten : lastFreigegebenFormular.getSteuerdaten()) {
            gesuchFormularUpdateDto.getSteuerdaten().add(steuerdatenMapper.toUpdateDto(steuerdaten));
        }
        gesuchTrancheMapper.partialUpdate(gesuchTrancheUpdateDto, aenderung);

        return gesuchTrancheMapper.toDto(aenderung);
    }

    @Transactional
    public void deleteAenderung(final UUID aenderungId) {
        if(!gesuchTrancheRepository.deleteById(aenderungId)){
            throw new NotFoundException();
        }
    }

    @Transactional
    public GesuchTrancheDto aenderungManuellAnpassen(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService.triggerStateMachineEvent(
            aenderung,
            GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG
        );

        return gesuchTrancheMapper.toDto(aenderung);
    }

    public ValidationReportDto einreichenValidieren(final UUID trancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(trancheId);

        try {
            gesuchTrancheValidatorService.validateGesuchTrancheForEinreichen(gesuchTranche);
        } catch (ValidationsException e) {
            return ValidationsExceptionMapper.toDto(e);
        } catch (CustomValidationsException e) {
            return CustomValidationsExceptionMapper.toDto(e);
        }

        return new ValidationReportDto();
    }

    public boolean openAenderungAlreadyExists(final Gesuch gesuch) {
        final var allowedStates = Set.of(
            GesuchTrancheStatus.AKZEPTIERT,
            GesuchTrancheStatus.ABGELEHNT,
            GesuchTrancheStatus.MANUELLE_AENDERUNG
        );

        final var trancheInDisallowedStates = gesuch.getAenderungen()
            .filter(gesuchTranche -> !allowedStates.contains(gesuchTranche.getStatus()))
            .toList();

        return !trancheInDisallowedStates.isEmpty();
    }
}
