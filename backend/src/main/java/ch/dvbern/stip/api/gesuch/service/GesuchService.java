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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.DocumentsRequiredValidationGroup;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
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
    private final SachbearbeiterZuordnungStammdatenWorker szsWorker;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final RequiredDokumentService requiredDokumentService;

    @Transactional
    public Optional<GesuchDto> findGesuch(UUID id) {
        return gesuchRepository.findByIdOptional(id).map(this::mapWithTrancheToWorkWith);
    }

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
        updateGesuchTranche(gesuchUpdateDto.getGesuchTrancheToWorkWith(), trancheToUpdate);

        final var updatePia = gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung();
        if (updatePia != null) {
            szsWorker.queueZuweisung(gesuch, tenantId);
        }
    }

    private void updateGesuchTranche(GesuchTrancheUpdateDto trancheUpdate, GesuchTranche trancheToUpdate) {
        gesuchTrancheMapper.partialUpdate(trancheUpdate, trancheToUpdate);
        Set<ConstraintViolation<GesuchTranche>> violations = validator.validate(trancheToUpdate);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
    }

    @Transactional
    public List<GesuchDto> findAllWithPersonInAusbildung() {
        return gesuchRepository.findAllWithFormular()
            .filter(gesuch ->
                getCurrentGesuchTrancheDto(gesuch)
                    .getGesuchFormular()
                    .getPersonInAusbildung() != null
            )
            .map(this::mapWithTrancheToWorkWith)
            .toList();
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

    public List<GesuchDto> findAllForCurrentBenutzer() {
        return findAllForBenutzer(benutzerService.getOrCreateCurrentBenutzer().getId());
    }

    @Transactional
    public List<GesuchDto> findAllForBenutzer(final UUID benutzerId) {
        final var benutzer = benutzerService.getBenutzer(benutzerId);
        if (benutzer.isEmpty()) {
            throw new NotFoundException();
        }

        final var rollen = benutzer.get()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        final Function<Stream<Gesuch>, Map<UUID, Gesuch>> toMap =
            stream -> stream.collect(Collectors.toMap(AbstractEntity::getId, gesuch -> gesuch));

        final var gesuche = new HashMap<UUID, Gesuch>();
        if (rollen.contains(OidcConstants.ROLE_GESUCHSTELLER)) {
            gesuche.putAll(toMap.apply(gesuchRepository.findAllForGs(benutzerId)));
        }
        if (rollen.contains(OidcConstants.ROLE_SACHBEARBEITER)) {
            gesuche.putAll(toMap.apply(gesuchRepository.findAllForSb(benutzerId)));
        }

        return gesuche.values().stream().map(this::mapWithTrancheToWorkWith).toList();
    }

    public List<GesuchDto> findAll() {
        return gesuchRepository.findAll().stream().map(this::mapWithTrancheToWorkWith).toList();
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
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINREICHEN);
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
        validationReportDto.hasDocuments(!gesuchFormular.getTranche().getGesuch().getGesuchDokuments().isEmpty());

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

    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuch(final UUID gesuchId) {
        return gesuchDokumentRepository.findAllForGesuch(gesuchId).map(gesuchDokumentMapper::toDto).toList();
    }

    public List<DokumentTyp> getRequiredDokumentTypes(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var formular = gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular();
        return requiredDokumentService.getRequiredDokumenteForGesuch(formular);
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
        validationService.validateGesuchForStatus(gesuch, Gesuchstatus.KOMPLETT_EINGEREICHT);
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
        final var currentBenutzer = benutzerService.getOrCreateCurrentBenutzer();
        if (!gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())) {
            throw new IllegalStateException(
                "Cannot update or delete das Gesuchsformular when parent status is: " + gesuch.getGesuchStatus()
            );
        }
    }
}
