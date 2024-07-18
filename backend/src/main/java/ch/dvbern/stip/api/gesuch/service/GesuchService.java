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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashSet;
import java.util.ArrayList;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.DocumentsRequiredValidationGroup;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchService {
    private final GesuchRepository gesuchRepository;
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final Validator validator;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchsperiodenService gesuchsperiodeService;
    private final GesuchValidatorService validationService;
    private final BenutzerService benutzerService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentService gesuchDokumentService;
    private final SachbearbeiterZuordnungStammdatenWorker szsWorker;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final RequiredDokumentService requiredDokumentService;
    private final ConfigService configService;

    @Transactional
    public Optional<GesuchDto> findGesuch(UUID id) {
        return gesuchRepository.findByIdOptional(id).map(this::mapWithTrancheToWorkWith);
    }

    @Transactional
    public void setAndValidateEinnahmenkostenUpdateLegality(final EinnahmenKostenUpdateDto einnahmenKostenUpdateDto, final GesuchTranche trancheToUpdate) {
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
        if(einnahmenKosten != null){
            Object steuerjahrDtoValue = einnahmenKostenUpdateDto.getSteuerjahr();
            Object steuerjahrExistingValue = einnahmenKosten.getSteuerjahr();
            Object steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = (Integer) ValidateUpdateLegalityUtil.getAndValidateLegalityValue(benutzerRollenIdentifiers,steuerjahrDtoValue,steuerjahrExistingValue,steuerjahrDefaultValue);

            Object veranlagungsCodeDtoValue = einnahmenKostenUpdateDto.getVeranlagungsCode();
            Object veranlagungsCodeExistingValue = einnahmenKosten.getVeranlagungsCode();
            Object veranlagungscodeDefaltValue = 0;
            veranlagungsCodeToSet = (Integer) ValidateUpdateLegalityUtil.getAndValidateLegalityValue(benutzerRollenIdentifiers,veranlagungsCodeDtoValue,veranlagungsCodeExistingValue,veranlagungscodeDefaltValue);
        }
        einnahmenKostenUpdateDto.setSteuerjahr(steuerjahrToSet);
        einnahmenKostenUpdateDto.setVeranlagungsCode(veranlagungsCodeToSet);
    }

    @Transactional
    public void setAndValidateSteuerdatenUpdateLegality(final List<SteuerdatenUpdateDto> steuerdatenUpdateDtos, final GesuchTranche trancheToUpdate) {
        final var gesuchsjahr = trancheToUpdate
            .getGesuch()
            .getGesuchsperiode()
            .getGesuchsjahr();

        final var steuerdatenList = trancheToUpdate.getGesuchFormular().getSteuerdaten().stream().filter(tab -> tab.getSteuerdatenTyp() != null).toList();

        for (final var steuerdatenUpdateDto : steuerdatenUpdateDtos){
            setAndValidateSteuerdatenTabUpdateLegality(steuerdatenUpdateDto, steuerdatenList.stream().filter(tab -> tab.getId().equals(steuerdatenUpdateDto.getId())).findFirst().orElse(null),gesuchsjahr);
        }
    }
    private void setAndValidateSteuerdatenTabUpdateLegality(final SteuerdatenUpdateDto steuerdatenUpdateDto, final Steuerdaten steuerdatenTabs, Gesuchsjahr gesuchsjahr){
        final var benutzerRollenIdentifiers = benutzerService.getCurrentBenutzer()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        Integer steuerjahrToSet = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
        Integer veranlagungsCodeToSet = 0;

        if(steuerdatenTabs != null){
            Object steuerjahrDtoValue = steuerdatenUpdateDto.getSteuerjahr();
            Object steuerjahrExistingValue = steuerdatenTabs.getSteuerjahr();
            Object steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = (Integer) ValidateUpdateLegalityUtil.getAndValidateLegalityValue(benutzerRollenIdentifiers,steuerjahrDtoValue,steuerjahrExistingValue,steuerjahrDefaultValue);

            Object veranlagungsCodeDtoValue = steuerdatenUpdateDto.getVeranlagungscode();
            Object veranlagungsCodeExistingValue = steuerdatenTabs.getVeranlagungsCode();
            Object veranlagungscodeDefaltValue = 0;
            veranlagungsCodeToSet = (Integer) ValidateUpdateLegalityUtil.getAndValidateLegalityValue(benutzerRollenIdentifiers,veranlagungsCodeDtoValue,veranlagungsCodeExistingValue,veranlagungscodeDefaltValue);
        }

        steuerdatenUpdateDto.setSteuerjahr(steuerjahrToSet);
        steuerdatenUpdateDto.setVeranlagungscode(veranlagungsCodeToSet);
    }

    @TransactionConfiguration(timeout = 10000)
    @Transactional
    public void updateGesuch(
        final UUID gesuchId,
        final GesuchUpdateDto gesuchUpdateDto,
        final String tenantId
    ) throws ValidationsException {
        var gesuch = gesuchRepository.requireById(gesuchId);
        preventUpdateVonGesuchIfReadOnly(gesuch);
        var trancheToUpdate = gesuch.getGesuchTrancheById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId())
            .orElseThrow(NotFoundException::new);

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
            && !gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().isEmpty()){
                setAndValidateSteuerdatenUpdateLegality(
                    gesuchUpdateDto
                        .getGesuchTrancheToWorkWith()
                        .getGesuchFormular()
                        .getSteuerdaten(),
                    trancheToUpdate);
        }
        updateGesuchTranche(gesuchUpdateDto.getGesuchTrancheToWorkWith(), trancheToUpdate);

        final var newFormular = trancheToUpdate.getGesuchFormular();
        removeSuperfluousDokumentsForGesuch(newFormular);

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
        gesuchRepository.persistAndFlush(gesuch);
        return mapWithTrancheToWorkWith(gesuch);
    }

    private void createInitialGesuchTranche(Gesuch gesuch) {
        var periode = gesuchsperiodeService
            .getGesuchsperiode(gesuch.getGesuchsperiode().getId())
            .orElseThrow(NotFoundException::new);

        var tranche = new GesuchTranche()
            .setGueltigkeit(new DateRange(periode.getGesuchsperiodeStart(), periode.getGesuchsperiodeStopp()))
            .setGesuch(gesuch)
            .setGesuchFormular(new GesuchFormular());

        gesuch.getGesuchTranchen().add(tranche);
    }

    @Transactional
    public List<GesuchDto> findAllGesucheSb() {
        return gesuchRepository.findAllFilteredForSb().map(this::mapWithTrancheToWorkWith).toList();
    }

    @Transactional
    public List<GesuchDto> findGesucheSb() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return gesuchRepository.findZugewiesenFilteredForSb(benutzer.getId())
            .map(this::mapWithTrancheToWorkWith)
            .toList();
    }

    @Transactional
    public List<GesuchDto> findGesucheGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return gesuchRepository.findForGs(benutzer.getId())
            .map(this::mapWithTrancheToWorkWith)
            .toList();
    }

    @Transactional
    public List<GesuchDto> findAllForFall(UUID fallId) {
        return gesuchRepository.findAllForFall(fallId).map(this::mapWithTrancheToWorkWith).toList();
    }

    @Transactional
    public void deleteGesuch(UUID gesuchId) {
        Gesuch gesuch = gesuchRepository.requireById(gesuchId);
        preventUpdateVonGesuchIfReadOnly(gesuch);
        gesuchRepository.delete(gesuch);
    }

    @Transactional
    public void gesuchEinreichen(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        // No need to validate the entire Gesuch here, as it's done in the state machine
        validateAdditionalEinreichenCriteria(gesuch);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINGEREICHT);
    }

    public ValidationReportDto validateGesuchEinreichen(UUID gesuchId) {
        Gesuch gesuch = gesuchRepository.requireById(gesuchId);

        try {
            validateGesuchEinreichen(gesuch);
        } catch (ValidationsException exception) {
            return ValidationsExceptionMapper.toDto(exception);
        } catch (CustomValidationsException exception) {
            return CustomValidationsExceptionMapper.toDto(exception);
        }

        return new ValidationReportDto();
    }

    public ValidationReportDto validatePages(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return validatePages(gesuch);
    }

    public ValidationReportDto validatePages(final Gesuch gesuch) {
        final var formular = gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular();

        if (formular == null) {
            throw new NotFoundException();
        }

        return validatePages(formular, gesuch.getId());
    }

    public ValidationReportDto validatePages(final @NotNull GesuchFormular gesuchFormular, UUID gesuchId) {
        final var validationGroups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        validationGroups.add(DocumentsRequiredValidationGroup.class);

        final var violations = new HashSet<>(
            validator.validate(
                gesuchFormular,
                validationGroups.toArray(new Class<?>[0])
            )
        );
        violations.addAll(validator.validate(gesuchFormular));

        final var validationReportDto = ValidationsExceptionMapper.toDto(violations);
        final var documents = gesuchFormular.getTranche().getGesuch().getGesuchDokuments();
        validationReportDto.hasDocuments(documents != null && !documents.isEmpty());

        try {
            validateNoOtherGesuchEingereichtWithSameSvNumber(gesuchFormular, gesuchId);
        } catch (CustomValidationsException exception) {
            CustomValidationsExceptionMapper
                .toDto(exception)
                .getValidationErrors()
                .forEach(validationReportDto::addValidationErrorsItem);
        }

        return validationReportDto;
    }

    private void removeSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        List<String> dokumentObjectIds = new ArrayList<>();

        requiredDokumentService.getSuperfluousDokumentsForGesuch(formular).forEach(
            gesuchDokument -> gesuchDokument.getDokumente().forEach(
                dokument -> dokumentObjectIds.add(
                    gesuchDokumentService.deleteDokument(dokument.getId())
                )
            )
        );

        if (!dokumentObjectIds.isEmpty()) {
            gesuchDokumentService.executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    public List<GesuchDokumentDto> getAndCheckGesuchDokumentsForGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var formular = gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular();

        removeSuperfluousDokumentsForGesuch(formular);
        return getGesuchDokumenteForGesuch(gesuchId);
    }

    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuch(final UUID gesuchId) {
        return gesuchDokumentRepository.findAllForGesuch(gesuchId).map(gesuchDokumentMapper::toDto).toList();
    }

    public List<DokumentTyp> getRequiredDokumentTypes(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var formular = gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular();

        return requiredDokumentService.getRequiredDokumentsForGesuch(formular);
    }

    private GesuchDto mapWithTrancheToWorkWith(Gesuch gesuch) {
        GesuchTrancheDto tranche = getCurrentGesuchTrancheDto(gesuch);
        GesuchDto gesuchDto = gesuchMapper.toDto(gesuch);
        gesuchDto.setGesuchTrancheToWorkWith(tranche);
        return gesuchDto;
    }

    private GesuchTranche getCurrentGesuchTranche(Gesuch gesuch) {
        return gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .orElseThrow();
    }

    private GesuchTrancheDto getCurrentGesuchTrancheDto(Gesuch gesuch) {
        return gesuchTrancheMapper.toDto(getCurrentGesuchTranche(gesuch));
    }

    private void validateGesuchEinreichen(Gesuch gesuch) {
        validateAdditionalEinreichenCriteria(gesuch);
        validationService.validateGesuchForStatus(gesuch, Gesuchstatus.EINGEREICHT);
    }

    private void validateAdditionalEinreichenCriteria(Gesuch gesuch) {
        validateGesuchTranchen(gesuch);
        validateNoOtherGesuchEingereichtWithSameSvNumber(gesuch);
    }

    private void validateGesuchTranchen(Gesuch gesuch) {
        gesuch.getGesuchTranchen().forEach(tranche -> {
            if (tranche.getGesuchFormular() == null || tranche.getGesuchFormular().getFamiliensituation() == null) {
                throw new ValidationsException(
                    "Es fehlt Formular Teilen um das Gesuch einreichen zu koennen",
                    new HashSet<>());
            }
        });
    }

    private void validateNoOtherGesuchEingereichtWithSameSvNumber(Gesuch gesuch) {
        validateNoOtherGesuchEingereichtWithSameSvNumber(
            getCurrentGesuchTranche(gesuch).getGesuchFormular(),
            gesuch.getId()
        );
    }

    private void validateNoOtherGesuchEingereichtWithSameSvNumber(
        final GesuchFormular gesuchFormular,
        final UUID gesuchId
    ) {
        if (gesuchFormular.getPersonInAusbildung() != null) {
            Stream<Gesuch> gesuchStream = gesuchRepository
                .findGesucheBySvNummer(
                    gesuchFormular
                        .getPersonInAusbildung()
                        .getSozialversicherungsnummer()
                );

            if (gesuchStream.anyMatch(g -> g.getGesuchStatus().isEingereicht() && g.getId() != gesuchId)) {
                throw new CustomValidationsException(
                    "Es darf nur ein Gesuch pro Gesuchsteller (Person in Ausbildung mit derselben SV-Nummer) "
                        + "eingereicht werden",
                    new CustomConstraintViolation(
                        VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE,
                        "personInAusbildung"
                    )
                );
            }
        }
    }

    private void preventUpdateVonGesuchIfReadOnly(Gesuch gesuch) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())) {
            throw new IllegalStateException(
                "Cannot update or delete das Gesuchsformular when parent status is: " + gesuch.getGesuchStatus()
            );
        }
    }
}
