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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumenteToUploadMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.api.steuererklaerung.service.SteuererklaerungMapper;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
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
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheValidatorService gesuchTrancheValidatorService;
    private final PersonInAusbildungMapper personInAusbildungMapper;
    private final FamiliensituationMapper familiensituationMapper;
    private final PartnerMapper partnerMapper;
    private final ElternMapper elternMapper;
    private final AuszahlungMapper auszahlungMapper;
    private final EinnahmenKostenMapper einnahmenKostenMapper;
    private final LebenslaufItemMapper lebenslaufItemMapper;
    private final GeschwisterMapper geschwisterMapper;
    private final KindMapper kindMapper;
    private final SteuererklaerungMapper steuererklaerungMapper;
    private final MailService mailService;
    private final NotificationService notificationService;
    private final DokumenteToUploadMapper dokumenteToUploadMapper;
    private final UnterschriftenblattService unterschriftenblattService;
    private final GesuchDokumentKommentarService gesuchDokumentKommentarService;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final GesuchHistoryService gesuchHistoryService;
    private final GesuchMapperUtil gesuchMapperUtil;

    public GesuchTranche getGesuchTrancheOrHistorical(final UUID gesuchTrancheId) {
        return gesuchTrancheHistoryService.getLatestTrancheForGs(gesuchTrancheId);
    }

    public GesuchTranche getGesuchTranche(final UUID gesuchTrancheId) {
        return gesuchTrancheRepository.requireById(gesuchTrancheId);
    }

    public UUID getGesuchIdOfTranche(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuch().getId();
    }

    @Transactional
    public GesuchTrancheListDto getAllTranchenAndInitalTrancheForGesuchGS(final UUID gesuchId) {
        var gesuchToWorkWith = gesuchHistoryService.getCurrentOrHistoricalGesuchForGS(gesuchId);

        final var allTranchenList = gesuchToWorkWith.getGesuchTranchen();
        final var currentTrancheFromGesuchInStatusVerfuegt =
            gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToVerfuegt(gesuchId);

        final var allTranchenOut = new ArrayList<GesuchTranche>(allTranchenList.size());
        allTranchenOut.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
                .toList()
        );
        allTranchenOut.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
                .sorted(Comparator.comparing(GesuchTranche::getTimestampMutiert))
                .toList()
        );

        return gesuchTrancheMapper.toListDto(
            allTranchenOut,
            currentTrancheFromGesuchInStatusVerfuegt.orElse(null)
        );
    }

    public GesuchTrancheListDto getAllTranchenAndInitalTrancheForGesuchSB(final UUID gesuchId) {
        final var allTranchenList = gesuchTrancheRepository.findForGesuch(gesuchId);
        final var currentTrancheFromGesuchInStatusVerfuegt =
            gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToVerfuegt(gesuchId);
        final var allTranchenOut = new ArrayList<GesuchTranche>(allTranchenList.size());
        allTranchenOut.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
                .toList()
        );
        allTranchenOut.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
                .sorted(Comparator.comparing(GesuchTranche::getTimestampMutiert))
                .toList()
        );

        return gesuchTrancheMapper.toListDto(
            allTranchenOut,
            currentTrancheFromGesuchInStatusVerfuegt.orElse(null)
        );
    }

    private DokumenteToUploadDto setFlagsOnDokumenteToUploadDto(
        final Gesuch gesuch,
        DokumenteToUploadDto dokumenteToUploadDto
    ) {
        final var needsUnterschriftenblatt = !gesuch.isVerfuegt()
        || Gesuchstatus.SACHBEARBEITER_CAN_UPLOAD_UNTERSCHRIFTENBLATT.contains(gesuch.getGesuchStatus());
        dokumenteToUploadDto.setSbCanUploadUnterschriftenblatt(needsUnterschriftenblatt);

        dokumenteToUploadDto.setGsCanDokumenteUebermitteln(
            requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(gesuch)
        );

        dokumenteToUploadDto.setSbCanFehlendeDokumenteUebermitteln(
            requiredDokumentService.getSBCanFehlendeDokumenteUebermitteln(gesuch)
        );

        dokumenteToUploadDto.setSbCanBearbeitungAbschliessen(
            requiredDokumentService.getSBCanBearbeitungAbschliessen(gesuch)
        );

        try {
            gesuchTrancheValidatorService.validateBearbeitungAbschliessen(gesuch);
        } catch (ValidationsException ex) {
            dokumenteToUploadDto.setSbCanBearbeitungAbschliessen(false);
        }
        return dokumenteToUploadDto;
    }

    @Transactional
    public DokumenteToUploadDto getDokumenteToUploadGS(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(gesuchTrancheId);

        final var required = getRequiredDokumentTypes(gesuchTranche);
        final var customRequired = getRequiredCustomDokumentTypes(gesuchTranche);
        var dokumenteToUploadDto = dokumenteToUploadMapper.toDto(required, List.of(), customRequired);
        return setFlagsOnDokumenteToUploadDto(gesuchTranche.getGesuch(), dokumenteToUploadDto);
    }

    @Transactional
    public DokumenteToUploadDto getDokumenteToUploadSB(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var required = getRequiredDokumentTypes(gesuchTranche);
        final var unterschriftenblaetter = unterschriftenblattService
            .getUnterschriftenblaetterToUpload(gesuchTranche.getGesuch());
        final var customRequired = getRequiredCustomDokumentTypes(gesuchTranche);
        var dokumenteToUploadDto = dokumenteToUploadMapper.toDto(required, unterschriftenblaetter, customRequired);
        return setFlagsOnDokumenteToUploadDto(gesuchTranche.getGesuch(), dokumenteToUploadDto);
    }

    public List<String> getAllRequiredDokumentTypes(final UUID gesuchTrancheId) {
        var allRequired = new ArrayList<String>();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var requiredDokumentTypes = getRequiredDokumentTypes(gesuchTranche);
        final var requiredCustomDokumentTypes = getRequiredCustomDokumentTypes(gesuchTranche);
        allRequired.addAll(requiredDokumentTypes.stream().map(Enum::toString).toList());
        allRequired
            .addAll(requiredCustomDokumentTypes.stream().map(CustomDokumentTyp::getType).toList());
        return allRequired;
    }

    public List<CustomDokumentTyp> getRequiredCustomDokumentTypes(final GesuchTranche gesuchTranche) {
        return requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(gesuchTranche)
            .stream()
            .toList();
    }

    public List<DokumentTyp> getRequiredDokumentTypes(final GesuchTranche gesuchTranche) {
        return requiredDokumentService.getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular());
    }

    @Transactional
    public List<GesuchDokumentDto> getAndCheckGesuchDokumentsForGesuchTrancheGS(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(gesuchTrancheId);
        removeSuperfluousDokumentsForGesuch(gesuchTranche.getGesuchFormular());

        return gesuchTranche.getGesuchDokuments()
            .stream()
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public List<GesuchDokumentDto> getAndCheckGesuchDokumentsForGesuchTrancheSB(final UUID gesuchTrancheId) {
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
                        final var dokumentObjectId =
                            gesuchDokumentService.deleteDokument(dokument.getId(), formular.getTranche().getId());
                        if (
                            dokument.getGesuchDokumente().isEmpty()
                            && (gesuchDokumentService.canDeleteDokumentFromS3(dokument, formular.getTranche()))
                        ) {
                            dokumentObjectIds.add(dokumentObjectId);
                        }
                    }
                );
            }
        );

        for (final var gesuchDokument : superfluousGesuchDokuments) {
            gesuchDokumentKommentarService.deleteForGesuchDokument(gesuchDokument.getId());
            formular.getTranche().getGesuchDokuments().remove(gesuchDokument);
        }

        if (!dokumentObjectIds.isEmpty()) {
            gesuchDokumentService.executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuchTranche(final UUID gesuchTrancheId) {
        return gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId)
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public ValidationReportDto validatePagesGS(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(gesuchTrancheId);

        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        if (gesuchFormular == null) {
            throw new NotFoundException();
        }
        return gesuchFormularService.validatePages(gesuchFormular);
    }

    @Transactional
    public ValidationReportDto validatePagesSB(final UUID gesuchTrancheId) {
        final var gesuchFormular =
            gesuchTrancheHistoryService.getLatestTrancheForGs(gesuchTrancheId).getGesuchFormular();
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
        final var allowedStates = Set.of(
            Gesuchstatus.STIPENDIENANSPRUCH,
            Gesuchstatus.KEIN_STIPENDIENANSPRUCH
        );

        if (!allowedStates.contains(gesuch.getGesuchStatus())) {
            throw new IllegalStateException("Create aenderung not allowed in current gesuch status");
        }

        if (openAenderungAlreadyExists(gesuch)) {
            throw new ForbiddenException();
        }

        final var trancheToCopy = gesuch.getTrancheValidOnDate(aenderungsantragCreateDto.getStart())
            .orElseThrow(NotFoundException::new);

        final var newTranche = GesuchTrancheCopyUtil
            .createAenderungstranche(trancheToCopy, aenderungsantragCreateDto);
        gesuch.getGesuchTranchen().add(newTranche);

        // Manually persist so that when mapping happens the IDs on the new objects are set
        gesuchRepository.persistAndFlush(gesuch);

        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(trancheToCopy, newTranche);

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

        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(trancheToCopy, newTranche);

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

        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(aenderung, newTranche);

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
        gesuchTrancheValidatorService.validateAenderungForAkzeptiert(aenderung);
        gesuchTrancheStatusService.triggerStateMachineEvent(aenderung, GesuchTrancheStatusChangeEvent.AKZEPTIERT);
        gesuchStatusService
            .triggerStateMachineEvent(aenderung.getGesuch(), GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN);

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

        final var lastFreigegebenTranche =
            gesuchTrancheHistoryRepository.getLatestWhereStatusChangedToUeberpruefen(aenderungId);
        final var lastFreigegebenFormular = lastFreigegebenTranche.getGesuchFormular();

        var gesuchTrancheUpdateDto = new GesuchTrancheUpdateDto().id(
            aenderungId
        );
        var gesuchFormularUpdateDto = new GesuchFormularUpdateDto();
        gesuchTrancheUpdateDto.setGesuchFormular(gesuchFormularUpdateDto);

        gesuchFormularUpdateDto.setPersonInAusbildung(
            personInAusbildungMapper.toUpdateDto(lastFreigegebenFormular.getPersonInAusbildung())
        );

        gesuchFormularUpdateDto
            .setFamiliensituation(familiensituationMapper.toUpdateDto(lastFreigegebenFormular.getFamiliensituation()));

        gesuchFormularUpdateDto.setPartner(partnerMapper.toUpdateDto(lastFreigegebenFormular.getPartner()));

        gesuchFormularUpdateDto.setElterns(new ArrayList<>(List.of()));
        aenderung.getGesuchFormular().getElterns().clear();
        for (final var eltern : lastFreigegebenFormular.getElterns()) {
            gesuchFormularUpdateDto.getElterns().add(elternMapper.toUpdateDto(eltern).id(null));
        }
        gesuchFormularUpdateDto.setAuszahlung(auszahlungMapper.toUpdateDto(lastFreigegebenFormular.getAuszahlung()));
        gesuchFormularUpdateDto
            .setEinnahmenKosten(einnahmenKostenMapper.toUpdateDto(lastFreigegebenFormular.getEinnahmenKosten()));

        gesuchFormularUpdateDto.setLebenslaufItems(new ArrayList<>(List.of()));
        for (final var lebenslaufItem : lastFreigegebenFormular.getLebenslaufItems()) {
            gesuchFormularUpdateDto.getLebenslaufItems().add(lebenslaufItemMapper.toUpdateDto(lebenslaufItem).id(null));
        }

        gesuchFormularUpdateDto.setGeschwisters(new ArrayList<>(List.of()));
        for (final var geschwister : lastFreigegebenFormular.getGeschwisters()) {
            gesuchFormularUpdateDto.getGeschwisters().add(geschwisterMapper.toUpdateDto(geschwister).id(null));
        }

        gesuchFormularUpdateDto.setKinds(new ArrayList<>(List.of()));
        for (final var kind : lastFreigegebenFormular.getKinds()) {
            gesuchFormularUpdateDto.getKinds().add(kindMapper.toUpdateDto(kind).id(null));
        }

        gesuchFormularUpdateDto.setSteuererklaerung(new ArrayList<>(List.of()));
        for (final var steuererklaerung : lastFreigegebenFormular.getSteuererklaerung()) {
            gesuchFormularUpdateDto.getSteuererklaerung()
                .add(steuererklaerungMapper.toUpdateDto(steuererklaerung).id(null));
        }

        gesuchTrancheMapper.partialUpdate(gesuchTrancheUpdateDto, aenderung);
        if (aenderung.getGesuchFormular().getPartner() != null) {
            aenderung.getGesuchFormular().getPartner().getAdresse().setId(null);
        }

        for (final var gesuchDokument : lastFreigegebenTranche.getGesuchDokuments()) {
            final var existingDokument = gesuchDokumentRepository.findById(gesuchDokument.getId());
            if (existingDokument == null) {
                gesuchDokumentRepository.persist((GesuchDokument) gesuchDokument.setId(null));
            }
        }

        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, aenderung.getGesuch());

        notificationService.createAenderungAbgelehntNotification(aenderung.getGesuch(), kommentarDto);

        return gesuchTrancheMapper.toDto(aenderung);
    }

    @Transactional
    public void deleteAenderung(final UUID aenderungId) {
        gesuchDokumentKommentarService.deleteForGesuchTrancheId(aenderungId);
        var aenderung = gesuchTrancheRepository.findById(aenderungId);
        aenderung.getGesuchDokuments()
            .forEach(
                gesuchDokument -> {
                    gesuchDokument.getDokumente()
                        .forEach(dokument -> dokument.getGesuchDokumente().remove(gesuchDokument));
                    gesuchDokumentRepository.deleteById(gesuchDokument.getId());
                }
            );
        if (!gesuchTrancheRepository.deleteById(aenderungId)) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public GesuchTrancheDto aenderungManuellAnpassen(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheValidatorService.validateAenderungForAkzeptiert(aenderung);
        gesuchTrancheStatusService.triggerStateMachineEvent(
            aenderung,
            GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG
        );
        gesuchStatusService
            .triggerStateMachineEvent(aenderung.getGesuch(), GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN);

        return gesuchTrancheMapper.toDto(aenderung);
    }

    private ValidationReportDto einreichenValidationReport(final GesuchTranche gesuchTranche) {
        final var documents = gesuchTranche.getGesuchDokuments();
        final var hasDocuments = documents != null && !documents.isEmpty();

        try {
            if (
                gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                && gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN
            ) {
                gesuchTrancheValidatorService.validateAenderungForAkzeptiert(gesuchTranche);
            } else {
                gesuchTrancheValidatorService.validateGesuchTrancheForEinreichen(gesuchTranche);
            }
        } catch (ValidationsException e) {
            return ValidationsExceptionMapper.toDto(e).hasDocuments(hasDocuments);
        } catch (CustomValidationsException e) {
            return CustomValidationsExceptionMapper.toDto(e).hasDocuments(hasDocuments);
        }

        return new ValidationReportDto().hasDocuments(hasDocuments);
    }

    @Transactional
    public ValidationReportDto einreichenValidierenGS(final UUID trancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(trancheId);
        return einreichenValidationReport(gesuchTranche);
    }

    public ValidationReportDto einreichenValidierenSB(final UUID trancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getLatestTrancheForGs(trancheId);
        return einreichenValidationReport(gesuchTranche);
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

    @Transactional
    public void dropGesuchTranche(final GesuchTranche gesuchTranche) {
        gesuchDokumentKommentarService.deleteForGesuchTrancheId(gesuchTranche.getId());
        gesuchTranche.getGesuchDokuments()
            .forEach(
                gesuchDokument -> {
                    gesuchDokument.getDokumente()
                        .forEach(dokument -> dokument.getGesuchDokumente().remove(gesuchDokument));
                    gesuchDokumentRepository.deleteById(gesuchDokument.getId());
                }
            );
        gesuchTranche.getGesuch().getGesuchTranchen().remove(gesuchTranche);
        gesuchTrancheRepository.delete(gesuchTranche);
    }

    @Transactional
    public GesuchDto aenderungFehlendeDokumenteEinreichen(final UUID aenderungId) {
        final var aenderungsTranche = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheValidatorService.validateGesuchTrancheForEinreichen(aenderungsTranche);
        gesuchTrancheStatusService
            .triggerStateMachineEvent(aenderungsTranche, GesuchTrancheStatusChangeEvent.UEBERPRUEFEN);
        return gesuchMapperUtil.mapWithGesuchOfTranche(aenderungsTranche);
    }

    @Transactional
    public void aenderungFehlendeDokumenteUebermitteln(final UUID aenderungId) {
        final var aenderungsTranche = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheValidatorService
            .validateGesuchTrancheForStatus(aenderungsTranche, GesuchTrancheStatus.FEHLENDE_DOKUMENTE);
        gesuchTrancheStatusService
            .triggerStateMachineEvent(aenderungsTranche, GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

}
