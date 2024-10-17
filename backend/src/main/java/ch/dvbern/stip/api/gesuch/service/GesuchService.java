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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GsDashboardDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchService {
    private final GesuchRepository gesuchRepository;
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final Validator validator;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;
    private final GesuchsperiodenService gesuchsperiodeService;
    private final BenutzerService benutzerService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final GesuchDokumentService gesuchDokumentService;
    private final SachbearbeiterZuordnungStammdatenWorker szsWorker;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final NotificationService notificationService;
    private final BerechnungService berechnungService;
    private final GesuchMapperUtil gesuchMapperUtil;
    private final GesuchTrancheService gesuchTrancheService;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchTrancheValidatorService gesuchTrancheValidatorService;
    private final GesuchNummerService gesuchNummerService;
    private final GsDashboardMapper gsDashboardMapper;
    private final ConfigService configService;

    @Transactional
    public Optional<GesuchDto> findGesuchWithOldestTranche(UUID id) {
        return gesuchRepository.findByIdOptional(id).map(gesuchMapperUtil::mapWithOldestTranche);
    }

    @Transactional
    public Optional<GesuchDto> findGesuchWithTranche(final UUID gesuchId, final UUID gesuchTrancheId) {
        return gesuchRepository.findByIdOptional(gesuchId)
            .map(gesuch -> gesuchMapperUtil.mapWithTranche(
                gesuch,
                gesuch.getGesuchTrancheById(gesuchTrancheId).orElseThrow(NotFoundException::new))
            );
    }

    @Transactional
    public void setAndValidateEinnahmenkostenUpdateLegality(
        final EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        final GesuchTranche trancheToUpdate
    ) {
        final var benutzerRollenIdentifiers = benutzerService.getCurrentBenutzer()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        final var gesuchsjahr = trancheToUpdate
            .getGesuch()
            .getGesuchsperiode()
            .getGesuchsjahr();
        Integer steuerjahrToSet = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
        Integer veranlagungsCodeToSet = 0;

        final var einnahmenKosten = trancheToUpdate.getGesuchFormular().getEinnahmenKosten();
        if (einnahmenKosten != null) {
            final Integer steuerjahrDtoValue = einnahmenKostenUpdateDto.getSteuerjahr();
            final Integer steuerjahrExistingValue = einnahmenKosten.getSteuerjahr();
            final Integer steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = ValidateUpdateLegalityUtil
                .getAndValidateLegalityValue(benutzerRollenIdentifiers,
                    steuerjahrDtoValue, steuerjahrExistingValue, steuerjahrDefaultValue);

            final Integer veranlagungsCodeDtoValue = einnahmenKostenUpdateDto.getVeranlagungsCode();
            final Integer veranlagungsCodeExistingValue = einnahmenKosten.getVeranlagungsCode();
            final Integer veranlagungscodeDefaltValue = 0;
            veranlagungsCodeToSet = ValidateUpdateLegalityUtil
                .getAndValidateLegalityValue(benutzerRollenIdentifiers,
                    veranlagungsCodeDtoValue, veranlagungsCodeExistingValue, veranlagungscodeDefaltValue);
        }
        einnahmenKostenUpdateDto.setSteuerjahr(steuerjahrToSet);
        einnahmenKostenUpdateDto.setVeranlagungsCode(veranlagungsCodeToSet);
    }

    @Transactional
    public void setAndValidateSteuerdatenUpdateLegality(
        final List<SteuerdatenUpdateDto> steuerdatenUpdateDtos,
        final GesuchTranche trancheToUpdate
    ) {
        final var gesuchsjahr = trancheToUpdate
            .getGesuch()
            .getGesuchsperiode()
            .getGesuchsjahr();

        final var steuerdatenList = trancheToUpdate.getGesuchFormular().getSteuerdaten().stream()
            .filter(tab -> tab.getSteuerdatenTyp() != null).toList();

        for (final var steuerdatenUpdateDto : steuerdatenUpdateDtos) {
            setAndValidateSteuerdatenTabUpdateLegality(steuerdatenUpdateDto,
                steuerdatenList.stream().filter(tab -> tab.getId().equals(steuerdatenUpdateDto.getId())).
                    findFirst().orElse(null), gesuchsjahr);
        }
    }

    private void setAndValidateSteuerdatenTabUpdateLegality(
        final SteuerdatenUpdateDto steuerdatenUpdateDto,
        final Steuerdaten steuerdatenTabs,
        Gesuchsjahr gesuchsjahr
    ) {
        final var benutzerRollenIdentifiers = benutzerService.getCurrentBenutzer()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        Integer steuerjahrToSet = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
        Integer veranlagungsCodeToSet = 0;

        if (steuerdatenTabs != null) {
            final Integer steuerjahrDtoValue = steuerdatenUpdateDto.getSteuerjahr();
            final Integer steuerjahrExistingValue = steuerdatenTabs.getSteuerjahr();
            final Integer steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = ValidateUpdateLegalityUtil.
                getAndValidateLegalityValue(benutzerRollenIdentifiers,
                    steuerjahrDtoValue, steuerjahrExistingValue, steuerjahrDefaultValue);

            final Integer veranlagungsCodeDtoValue = steuerdatenUpdateDto.getVeranlagungsCode();
            final Integer veranlagungsCodeExistingValue = steuerdatenTabs.getVeranlagungsCode();
            final Integer veranlagungscodeDefaltValue = 0;
            veranlagungsCodeToSet = ValidateUpdateLegalityUtil.
                getAndValidateLegalityValue(benutzerRollenIdentifiers, veranlagungsCodeDtoValue,
                    veranlagungsCodeExistingValue, veranlagungscodeDefaltValue);
        }

        steuerdatenUpdateDto.setSteuerjahr(steuerjahrToSet);
        steuerdatenUpdateDto.setVeranlagungsCode(veranlagungsCodeToSet);
    }

    @Transactional
    public void updateGesuch(
        final UUID gesuchId,
        final GesuchUpdateDto gesuchUpdateDto,
        final String tenantId
    ) throws ValidationsException {
        var gesuch = gesuchRepository.requireById(gesuchId);
        var trancheToUpdate = gesuch.getGesuchTrancheById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId())
            .orElseThrow(NotFoundException::new);
        if (trancheToUpdate.getTyp() == GesuchTrancheTyp.TRANCHE) {
            preventUpdateVonGesuchIfReadOnly(gesuch);
        } else if (trancheToUpdate.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            preventUpdateVonAenderungIfReadOnly(trancheToUpdate);
        }

        if (gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten() != null) {
            setAndValidateEinnahmenkostenUpdateLegality(
                gesuchUpdateDto
                    .getGesuchTrancheToWorkWith()
                    .getGesuchFormular()
                    .getEinnahmenKosten(),
                trancheToUpdate
            );
        }
        if (gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten() != null
            && !gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().isEmpty()) {
            setAndValidateSteuerdatenUpdateLegality(
                gesuchUpdateDto
                    .getGesuchTrancheToWorkWith()
                    .getGesuchFormular()
                    .getSteuerdaten(),
                trancheToUpdate);
        }
        updateGesuchTranche(gesuchUpdateDto.getGesuchTrancheToWorkWith(), trancheToUpdate);

        final var newFormular = trancheToUpdate.getGesuchFormular();
        gesuchTrancheService.removeSuperfluousDokumentsForGesuch(newFormular);

        final var updatePia = gesuchUpdateDto
            .getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung();
        if (updatePia != null) {
            szsWorker.queueZuweisung(gesuch, tenantId);
        }
    }

    private void updateGesuchTranche(final GesuchTrancheUpdateDto trancheUpdate, final GesuchTranche trancheToUpdate) {
        gesuchTrancheMapper.partialUpdate(trancheUpdate, trancheToUpdate);
        Set<ConstraintViolation<GesuchTranche>> violations = validator.validate(trancheToUpdate);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
    }

    @Transactional
    public GesuchDto createGesuch(GesuchCreateDto gesuchCreateDto) {
        Gesuch gesuch = gesuchMapper.toNewEntity(gesuchCreateDto);
        createInitialGesuchTranche(gesuch);
        gesuch.setGesuchNummer(gesuchNummerService.createGesuchNummer(gesuch.getGesuchsperiode().getId()));
        gesuchRepository.persistAndFlush(gesuch);
        return gesuchMapperUtil.mapWithTranche(
            gesuch, gesuch.getNewestGesuchTranche().orElseThrow(IllegalStateException::new)
        );
    }

    private void createInitialGesuchTranche(Gesuch gesuch) {
        var periode = gesuchsperiodeService
            .getGesuchsperiode(gesuch.getGesuchsperiode().getId())
            .orElseThrow(NotFoundException::new);

        var tranche = new GesuchTranche()
            .setGueltigkeit(new DateRange(periode.getGesuchsperiodeStart(), periode.getGesuchsperiodeStopp()))
            .setGesuch(gesuch)
            .setGesuchFormular(new GesuchFormular())
            .setTyp(GesuchTrancheTyp.TRANCHE);

        gesuch.getGesuchTranchen().add(tranche);
    }

    @Transactional
    public List<GesuchDto> findGesucheSB(GetGesucheSBQueryType getGesucheSBQueryType) {
        final var meId = benutzerService.getCurrentBenutzer().getId();
        return switch (getGesucheSBQueryType) {
            case ALLE_BEARBEITBAR -> map(gesuchRepository.findAlleBearbeitbar());
            case ALLE_BEARBEITBAR_MEINE -> map(gesuchRepository.findAlleMeineBearbeitbar(meId));
            case ALLE_MEINE -> map(gesuchRepository.findAlleMeine(meId));
            case ALLE -> map(gesuchRepository.findAlle()
                .filter(gesuch -> gesuch.getNewestGesuchTranche()
                    .orElseThrow(NotFoundException::new)
                    .getGesuchFormular()
                    .getPersonInAusbildung() != null
                )
            );
        };
    }

    private List<GesuchDto> map(final Stream<Gesuch> gesuche) {
        List<GesuchDto> gesuchDtos = new ArrayList<>();
        gesuche.forEach(gesuch -> {
            if (gesuch.getAenderungZuUeberpruefen().isPresent()) {
                gesuchDtos.addAll(gesuchMapperUtil.mapWithAenderung(gesuch));
            } else{
                gesuchDtos.add(gesuchMapperUtil.mapWithNewestTranche(gesuch));
            }
        });
        return gesuchDtos;
    }

    @Transactional
    public List<GesuchDto> findGesucheGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return gesuchRepository.findForGs(benutzer.getId())
            .map(gesuchMapperUtil::mapWithNewestTranche)
            .toList();
    }

    public List<GsDashboardDto> findGsDashboard() {
        List<GsDashboardDto> gsDashboardDtos = new ArrayList<>();
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var gesuche = gesuchRepository.findForGs(benutzer.getId()).toList();

        for (var gesuch : gesuche) {
            final var gesuchTranchen = gesuchTrancheService.getAllTranchenForGesuch(gesuch.getId());

            final var offeneAenderung = gesuchTranchen.stream()
                .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.AENDERUNG)
                    && tranche.getStatus().equals(GesuchTrancheStatus.IN_BEARBEITUNG_GS))
                .findFirst().orElse(null);

            final var missingDocumentsTrancheIdAndCount = gesuchTranchen.stream()
                .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.TRANCHE))
                .map(tranche -> ImmutablePair.of(
                    tranche.getId(),
                    gesuchTrancheService.getRequiredDokumentTypes(tranche.getId()).size()
                ))
                .filter(pair -> pair.getRight() > 0)
                .findFirst();

            gsDashboardDtos.add(gsDashboardMapper.toDto(gesuch, offeneAenderung, missingDocumentsTrancheIdAndCount));
        }

        return gsDashboardDtos;
    }

    @Transactional
    public List<GesuchDto> findAllForFall(UUID fallId) {
        return gesuchRepository.findAllForFall(fallId).map(gesuchMapperUtil::mapWithNewestTranche).toList();
    }

    @Transactional
    public void deleteGesuch(UUID gesuchId) {
        Gesuch gesuch = gesuchRepository.requireById(gesuchId);
        preventUpdateVonGesuchIfReadOnly(gesuch);
        gesuchDokumentService.removeAllGesuchDokumentsForGesuch(gesuchId);
        notificationService.deleteNotificationsForGesuch(gesuchId);
        gesuch.getGesuchTranchen().forEach(
            gesuchTranche -> gesuchDokumentKommentarRepository.deleteAllForGesuchTranche(gesuchTranche.getId())
        );
        gesuchRepository.delete(gesuch);
    }

    @Transactional
    public void gesuchEinreichen(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuch.getGesuchTranchen().size() != 1) {
            throw new IllegalStateException("Gesuch kann only be eingereicht with exactly 1 Tranche");
        }

        // No need to validate the entire Gesuch here, as it's done in the state machine
        gesuchTrancheValidatorService.validateAdditionalEinreichenCriteria(gesuch.getGesuchTranchen().get(0));
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINGEREICHT);
    }

    @Transactional
    public void bearbeitungAbschliessen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
            gesuch,
            configService.getCurrentDmnMajorVersion(),
            configService.getCurrentDmnMinorVersion()
        );

        if (stipendien.getBerechnung() <= 0) {
            // Keine Stipendien, next Status = Verfuegt
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERFUEGT);
        } else {
            // Yes Stipendien, next Status = In Freigabe
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.IN_FREIGABE);
        }
    }

    @Transactional
    public void gesuchZurueckweisen(final UUID gesuchId, final KommentarDto kommentarDto) {
        // TODO KSTIP-1130: Juristische Notiz erstellen anhand Kommentar
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.IN_BEARBEITUNG_GS);
    }

    @Transactional
    public void juristischAbklaeren(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG);
    }

    @Transactional
    public GesuchDto gesuchStatusToInBearbeitung(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.IN_BEARBEITUNG_SB);
        return gesuchMapperUtil.mapWithNewestTranche(gesuch);
    }

    @Transactional
    public GesuchDto gesuchStatusToBereitFuerBearbeitung(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
        return gesuchMapperUtil.mapWithNewestTranche(gesuch);
    }

    @Transactional
    public GesuchDto gesuchStatusToVerfuegt(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERFUEGT);
        return gesuchMapperUtil.mapWithNewestTranche(gesuch);
    }

    @Transactional
    public GesuchDto gesuchStatusToVersendet(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERSENDET);
        return gesuchMapperUtil.mapWithNewestTranche(gesuch);
    }

    @Transactional
    public void gesuchFehlendeDokumente(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        return gesuch.getGesuchTranchen().stream().filter(
                gesuchTranche -> gesuchTranche.getStatus() != GesuchTrancheStatus.ABGELEHNT
            ).flatMap(
                gesuchTranche -> gesuchDokumentRepository.findAllForGesuchTranche(gesuchTranche.getId())
            ).map(gesuchDokumentMapper::toDto)
            .toList();
    }

    private void preventUpdateVonGesuchIfReadOnly(Gesuch gesuch) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())) {
            throw new IllegalStateException(
                "Cannot update or delete das Gesuchsformular when parent status is: " + gesuch.getGesuchStatus()
            );
        }
    }

    private void preventUpdateVonAenderungIfReadOnly(GesuchTranche gesuchTranche) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!gesuchTrancheStatusService.benutzerCanEdit(currentBenutzer, gesuchTranche.getStatus())) {
            throw new IllegalStateException(
                "Cannot update or delete the Aenderung when status is: " + gesuchTranche.getStatus()
            );
        }
    }

    public BerechnungsresultatDto getBerechnungsresultat(UUID gesuchId) {
        final var gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
        return berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
    }

    public GesuchWithChangesDto getGsTrancheChanges(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        final var initialRevision = gesuchTrancheHistoryRepository.getInitialRevision(aenderungId);
        return gesuchMapperUtil.toWithChangesDto(aenderung.getGesuch(), aenderung, initialRevision);
    }

    public GesuchWithChangesDto getSbTrancheChanges(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        final var initialRevision = gesuchTrancheHistoryRepository.getInitialRevision(aenderungId);
        final var latestWhereStatusChanged = gesuchTrancheHistoryRepository.getLatestWhereStatusChanged(aenderungId);
        return gesuchMapperUtil.toWithChangesDto(
            aenderung.getGesuch(),
            aenderung,
            List.of(initialRevision, latestWhereStatusChanged)
        );
    }
}
