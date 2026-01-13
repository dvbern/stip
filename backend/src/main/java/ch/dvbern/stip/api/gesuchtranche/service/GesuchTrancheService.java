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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.service.AuszahlungValidatorService;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.jahreswert.JahreswertUtil;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumenteToUploadMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.util.IsDokumentOfVersteckterElternteilUtil;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
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
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
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
import ch.dvbern.stip.generated.dto.PatchAenderungsInfoRequestDto;
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
    private final DokumentRepository dokumentRepository;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchTrancheTruncateService gesuchTrancheTruncateService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheValidatorService gesuchTrancheValidatorService;
    private final PersonInAusbildungMapper personInAusbildungMapper;
    private final FamiliensituationMapper familiensituationMapper;
    private final PartnerMapper partnerMapper;
    private final ElternMapper elternMapper;
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
    private final BenutzerService benutzerService;
    private final StatusprotokollService statusprotokollService;
    private final GesuchTrancheCopyService gesuchTrancheCopyService;
    private final GesuchTrancheOverrideDokumentService gesuchTrancheOverrideDokumentService;
    private final AuszahlungValidatorService auszahlungValidatorService;

    public GesuchTranche getGesuchTrancheOrHistorical(final UUID gesuchTrancheId) {
        return gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId);
    }

    public GesuchTranche getGesuchTranche(final UUID gesuchTrancheId) {
        return gesuchTrancheRepository.requireById(gesuchTrancheId);
    }

    public UUID getGesuchIdOfTranche(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuch().getId();
    }

    private GesuchTrancheListDto getAllTranchenAndInitialTrancheForGesuchTranche(
        List<GesuchTranche> allTranchenList,
        UUID gesuchId
    ) {
        final var allTranchenFromGesuchInStatusVerfuegt =
            gesuchTrancheHistoryRepository.getAllTranchenWhereGesuchStatusFirstChangedToVerfuegt(gesuchId);

        final var allTrancheTranchen = new ArrayList<GesuchTranche>();
        allTrancheTranchen.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
                .sorted(Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigAb()))
                .toList()
        );
        final var aenderungen = new ArrayList<GesuchTranche>();
        aenderungen.addAll(
            allTranchenList.stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
                .sorted(Comparator.comparing(GesuchTranche::getTimestampMutiert))
                .toList()
        );

        final var abgelehnteAenderungen = gesuchTrancheHistoryRepository.getAllAbgelehnteAenderungs(gesuchId);

        return gesuchTrancheMapper.toListDto(
            allTrancheTranchen,
            allTranchenFromGesuchInStatusVerfuegt,
            aenderungen,
            abgelehnteAenderungen
        );
    }

    @Transactional
    public GesuchTrancheListDto getAllTranchenAndInitalTrancheForGesuchGS(final UUID gesuchId) {
        var gesuchToWorkWith = gesuchHistoryService.getCurrentOrHistoricalGesuchForGS(gesuchId);

        return getAllTranchenAndInitialTrancheForGesuchTranche(gesuchToWorkWith.getGesuchTranchen(), gesuchId);
    }

    @Transactional
    public GesuchTrancheListDto getAllTranchenAndInitalTrancheForGesuchSB(final UUID gesuchId) {
        return getAllTranchenAndInitialTrancheForGesuchTranche(
            gesuchTrancheRepository.findForGesuch(gesuchId),
            gesuchId
        );
    }

    private DokumenteToUploadDto setFlagsOnDokumenteToUploadDto(
        final Gesuch gesuch,
        DokumenteToUploadDto dokumenteToUploadDto
    ) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var needsUnterschriftenblatt = !gesuch.isVerfuegt()
        || Gesuchstatus.SACHBEARBEITER_CAN_UPLOAD_UNTERSCHRIFTENBLATT.contains(gesuch.getGesuchStatus());
        dokumenteToUploadDto.setSbCanUploadUnterschriftenblatt(needsUnterschriftenblatt);

        dokumenteToUploadDto.setGsCanDokumenteUebermitteln(
            requiredDokumentService
                .getGSCanFehlendeDokumenteEinreichen(gesuch, benutzer)
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
        final var gesuchTranche = gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId);
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

        final var versteckteEltern = gesuchTranche.getGesuchFormular().getVersteckteEltern();
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> !IsDokumentOfVersteckterElternteilUtil
                    .isVerstecktesDokument(versteckteEltern, gesuchDokument)
            )
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public List<GesuchDokumentDto> getAndCheckGesuchDokumentsForGesuchTrancheSB(final UUID gesuchTrancheId) {
        var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        // query GesuchTrancheHistory, if requested tranche might have been overwritten
        if (gesuchTranche == null) {
            return gesuchTrancheHistoryService
                .getLatestTranche(gesuchTrancheId)
                .getGesuchDokuments()
                .stream()
                .map(gesuchDokumentMapper::toDto)
                .toList();
        }
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
                            gesuchDokumentService.deleteDokument(dokument.getId());
                        if (
                            gesuchDokumentService.canDeleteDokumentFromS3(dokument, formular.getTranche())
                        ) {
                            dokumentObjectIds.add(dokumentObjectId);
                        }
                    }
                );
            }
        );

        for (final var gesuchDokument : superfluousGesuchDokuments) {
            gesuchDokument.getGesuchDokumentKommentare().clear();
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
            gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId).getGesuchFormular();
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

        final var trancheToCopy = gesuch.getEingereichteGesuchTrancheValidOnDate(aenderungsantragCreateDto.getStart())
            .orElseThrow(NotFoundException::new);

        final var newTranche = gesuchTrancheCopyService
            .createAenderungstranche(trancheToCopy, aenderungsantragCreateDto);
        gesuch.getGesuchTranchen().add(newTranche);

        // Manually persist so that when mapping happens the IDs on the new objects are set
        gesuchRepository.persistAndFlush(gesuch);
        statusprotokollService.createStatusprotokoll(
            GesuchTrancheStatus.IN_BEARBEITUNG_GS.toString(),
            null,
            StatusprotokollEntryTyp.AENDERUNG,
            null,
            gesuch
        );

        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(trancheToCopy, newTranche);

        return gesuchTrancheMapper.toDtoWithoutVersteckteEltern(newTranche);
    }

    @Transactional
    public GesuchTrancheDto createTrancheCopy(
        final UUID gesuchId,
        final CreateGesuchTrancheRequestDto createDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var trancheToCopy = gesuch.getEingereichteGesuchTrancheValidOnDate(createDto.getStart())
            .orElseThrow(NotFoundException::new);
        final var newTranche = gesuchTrancheCopyService.createNewTranche(
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

        return gesuchTrancheMapper.toDtoWithVersteckteEltern(newTranche);
    }

    @Transactional
    public void aenderungEinbinden(
        final GesuchTranche aenderung
    ) {
        final var gesuch = aenderung.getGesuch();
        final var newTranche = gesuchTrancheCopyService.createNewTranche(aenderung);
        gesuch.getGesuchTranchen().add(newTranche);
        gesuchRepository.persistAndFlush(gesuch);

        gesuchDokumentKommentarService.copyKommentareFromTrancheToTranche(aenderung, newTranche);

        gesuchTrancheTruncateService.truncateExistingTranchen(gesuch, newTranche);
        gesuchTrancheOverrideDokumentService.overrideJahreswertDokumente(gesuch, newTranche);
        JahreswertUtil.synchroniseJahreswerte(newTranche);
    }

    @Transactional
    public void aenderungEinreichen(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService.triggerStateMachineEvent(aenderung, GesuchTrancheStatusChangeEvent.UEBERPRUEFEN);
        notificationService.createAenderungEingereichtNotification(aenderung.getGesuch());
        mailService.sendStandardNotificationEmailForGesuch(aenderung.getGesuch());
    }

    @Transactional
    public GesuchTrancheDto aenderungAkzeptieren(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheValidatorService.validateAenderungForAkzeptiert(aenderung);
        gesuchTrancheStatusService.triggerStateMachineEvent(aenderung, GesuchTrancheStatusChangeEvent.AKZEPTIERT);
        gesuchStatusService
            .triggerStateMachineEvent(aenderung.getGesuch(), GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN);

        final var newTranche = gesuchTrancheRepository.findMostRecentCreatedTranche(aenderung.getGesuch());
        return gesuchTrancheMapper.toDtoWithVersteckteEltern(newTranche.orElseThrow(NotFoundException::new));
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
                .add(steuererklaerungMapper.toUpdateDto(steuererklaerung));
        }

        gesuchTrancheMapper.partialUpdate(gesuchTrancheUpdateDto, aenderung, false);
        if (aenderung.getGesuchFormular().getPartner() != null) {
            aenderung.getGesuchFormular().getPartner().getAdresse().setId(null);
        }

        for (final var gesuchDokument : lastFreigegebenTranche.getGesuchDokuments()) {
            final var existingDokument = gesuchDokumentRepository.findById(gesuchDokument.getId());
            if (existingDokument == null) {
                gesuchDokument.getDokumente().forEach(dokument -> dokument.setId(null));
                gesuchDokumentRepository.persist((GesuchDokument) gesuchDokument.setId(null));
            } else if (
                gesuchDokument.getStatus() != existingDokument.getStatus() &&
                gesuchDokument.getStatus() == GesuchDokumentStatus.AUSSTEHEND
            ) {
                existingDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
            }
        }

        mailService.sendStandardNotificationEmailForGesuch(aenderung.getGesuch());

        notificationService.createAenderungAbgelehntNotification(aenderung.getGesuch(), aenderung, kommentarDto);

        return gesuchTrancheMapper.toDtoWithVersteckteEltern(aenderung);
    }

    @Transactional
    public void deleteAenderung(final UUID aenderungId) {
        gesuchDokumentKommentarService.deleteForGesuchTrancheId(aenderungId);
        var aenderung = gesuchTrancheRepository.findById(aenderungId);
        aenderung.getGesuchDokuments()
            .forEach(
                gesuchDokument -> {
                    gesuchDokument.getDokumente()
                        .forEach(dokumentRepository::delete);
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

        return gesuchTrancheMapper.toDtoWithVersteckteEltern(aenderung);
    }

    private ValidationReportDto bearbeitungAbschliessenValidationReport(final GesuchTranche gesuchTranche) {
        final var documents = gesuchTranche.getGesuchDokuments();
        final var hasDocuments = documents != null && !documents.isEmpty();

        try {
            gesuchTrancheValidatorService.validateAenderungForAkzeptiert(gesuchTranche);
        } catch (ValidationsException e) {
            return ValidationsExceptionMapper.toDto(e, hasDocuments);
        } catch (CustomValidationsException e) {
            return ValidationsExceptionMapper.toDto(e, hasDocuments);
        }

        return ValidationsExceptionMapper.toDto(hasDocuments);
    }

    private ValidationReportDto einreichenValidationReport(final GesuchTranche gesuchTranche) {
        final var documents = gesuchTranche.getGesuchDokuments();
        final var hasDocuments = documents != null && !documents.isEmpty();

        CustomConstraintViolation auszahlungConstraintViolation =
            auszahlungValidatorService.getZahlungsverbindungCustomConstraintViolation(gesuchTranche.getGesuch());

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
            return ValidationsExceptionMapper.toDto(e, auszahlungConstraintViolation, hasDocuments);
        } catch (CustomValidationsException e) {
            return ValidationsExceptionMapper.toDto(e, auszahlungConstraintViolation, hasDocuments);
        }

        return ValidationsExceptionMapper.toDto(auszahlungConstraintViolation, hasDocuments);
    }

    @Transactional
    public ValidationReportDto einreichenValidierenGS(final UUID trancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(trancheId);
        return einreichenValidationReport(gesuchTranche);
    }

    @Transactional
    public ValidationReportDto einreichenValidierenSB(final UUID trancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getLatestTranche(trancheId);
        return bearbeitungAbschliessenValidationReport(gesuchTranche);
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
                        .forEach(dokumentRepository::delete);
                    gesuchDokumentRepository.deleteById(gesuchDokument.getId());
                }
            );
        gesuchTranche.getGesuch().getGesuchTranchen().remove(gesuchTranche);
        gesuchTrancheRepository.delete(gesuchTranche);
    }

    @Transactional
    public void dropGesuchTranche(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        dropGesuchTranche(gesuchTranche);
    }

    @Transactional
    public GesuchDto aenderungFehlendeDokumenteEinreichen(final UUID aenderungId) {
        final var aenderungsTranche = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheStatusService
            .triggerStateMachineEvent(aenderungsTranche, GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN);
        return gesuchMapperUtil.mapWithGesuchOfTranche(aenderungsTranche, false);
    }

    @Transactional
    public void aenderungFehlendeDokumenteUebermitteln(final UUID aenderungId) {
        final var aenderungsTranche = gesuchTrancheRepository.requireAenderungById(aenderungId);
        gesuchTrancheValidatorService
            .validateGesuchTrancheForStatus(aenderungsTranche, GesuchTrancheStatus.FEHLENDE_DOKUMENTE);
        gesuchTrancheStatusService
            .triggerStateMachineEvent(aenderungsTranche, GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

    @Transactional
    public GesuchDto updateGueltigkeitOfAenderung(
        final UUID aenderungId,
        final PatchAenderungsInfoRequestDto patchAenderungsInfoRequestDto
    ) {
        final var aenderungsTranche = gesuchTrancheRepository.requireAenderungById(aenderungId);

        // validate (modified) guetligkeit start of aenderung
        aenderungsTranche.getGesuch()
            .getEingereichteGesuchTrancheValidOnDate(patchAenderungsInfoRequestDto.getStart())
            .orElseThrow(NotFoundException::new);
        var rawGueltigkeit =
            new DateRange(patchAenderungsInfoRequestDto.getStart(), patchAenderungsInfoRequestDto.getEnd());
        var gueltigkeit =
            gesuchTrancheCopyService.validateAndCreateClampedDateRange(rawGueltigkeit, aenderungsTranche.getGesuch());

        aenderungsTranche.setGueltigkeit(gueltigkeit);
        aenderungsTranche.setComment(patchAenderungsInfoRequestDto.getComment());
        return gesuchMapperUtil.mapWithGesuchOfTranche(aenderungsTranche, true);
    }

    @Transactional
    public List<ElternTyp> setVersteckteEltern(final UUID gesuchTrancheId, final List<ElternTyp> elternTyps) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        gesuchTranche.getGesuchFormular().setVersteckteEltern(new LinkedHashSet<>(elternTyps));

        return new ArrayList<>(gesuchTranche.getGesuchFormular().getVersteckteEltern());
    }
}
