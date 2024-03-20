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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchFormularRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
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
    private final GesuchFormularRepository gesuchFormularRepository;

    @Transactional
    public Optional<GesuchDto> findGesuch(UUID id) {
        return gesuchRepository.findByIdOptional(id).map(this::mapWithTrancheToWorkWith);
    }

    @Transactional
    public void updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) throws ValidationsException {
        var gesuch = gesuchRepository.requireById(gesuchId);
        preventUpdateVonGesuchIfReadOnly(gesuch);
        var trancheToUpdate = gesuch.getGesuchTrancheById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId())
            .orElseThrow(NotFoundException::new);
        updateGesuchTranche(gesuchUpdateDto.getGesuchTrancheToWorkWith(), trancheToUpdate);
    }

    private void updateGesuchTranche(GesuchTrancheUpdateDto trancheUpdate, GesuchTranche trancheToUpdate) {
        resetFieldsOnUpdate(trancheToUpdate.getGesuchFormular(), trancheUpdate.getGesuchFormular());
        gesuchTrancheMapper.partialUpdate(trancheUpdate, trancheToUpdate);

        Set<ConstraintViolation<GesuchTranche>> violations = validator.validate(trancheToUpdate);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
    }

    @Transactional
    public List<GesuchDto> findAllWithPersonInAusbildung() {
        return gesuchRepository.findAllWithFormular()
            .filter(gesuch -> this.getCurrentGesuchTranche(gesuch).getGesuchFormular().getPersonInAusbildung() != null)
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
            .setGueltigkeit(new DateRange(periode.getGueltigAb(), periode.getGueltigBis()))
            .setGesuch(gesuch);

        gesuch.getGesuchTranchen().add(tranche);
    }

    @Transactional
    public List<GesuchDto> findAllForBenutzer(UUID benutzerId) {
        return gesuchRepository.findAllForBenutzer(benutzerId).map(this::mapWithTrancheToWorkWith).toList();
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

        return validatePages(formular);
    }

    public ValidationReportDto validatePages(final @NotNull GesuchFormular gesuchFormular) {
        final var validationGroups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        final var violations = new HashSet<>(
            validator.validate(
                gesuchFormular,
                validationGroups.toArray(new Class<?>[0])
            )
        );
        violations.addAll(validator.validate(gesuchFormular));

        return ValidationsExceptionMapper.constraintViolationstoDto(violations);
    }

    private GesuchDto mapWithTrancheToWorkWith(Gesuch gesuch) {
        GesuchTrancheDto tranche = getCurrentGesuchTranche(gesuch);
        GesuchDto gesuchDto = gesuchMapper.toDto(gesuch);
        gesuchDto.setGesuchTrancheToWorkWith(tranche);
        return gesuchDto;
    }

    private GesuchTrancheDto getCurrentGesuchTranche(Gesuch gesuch) {
        return gesuch.getGesuchTrancheValidOnDate(LocalDate.now())
            .map(gesuchTrancheMapper::toDto)
            .orElseThrow();
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
        if (getCurrentGesuchTranche(gesuch).getGesuchFormular().getPersonInAusbildung() != null) {
            Stream<Gesuch> gesuchStream = gesuchRepository
                .findGesucheBySvNummer(getCurrentGesuchTranche(gesuch).getGesuchFormular()
                    .getPersonInAusbildung()
                    .getSozialversicherungsnummer());

            if (gesuchStream.anyMatch(g -> g.getGesuchStatus().isEingereicht())) {
                throw new CustomValidationsException(
                    "Es darf nur ein Gesuch pro Gesuchsteller (Person in Ausbildung mit derselben SV-Nummer) "
                        + "eingereicht werden",
                    new CustomConstraintViolation(VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE)
                );
            }
        }
    }

    private void resetFieldsOnUpdate(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (toUpdate == null || update == null) {
            return;
        }

        if (hasGeburtsdatumOfPersonInAusbildungChanged(toUpdate, update)) {
            update.setLebenslaufItems(new ArrayList<>());
        }

        if (hasZivilstandChangedToOnePerson(toUpdate, update)) {
            toUpdate.setPartner(null);
            update.setPartner(null);
        }
        resetAuswaertigesMittagessen(toUpdate, update);
        resetEltern(toUpdate, update);
        resetAlimente(toUpdate, update);
    }

    private void resetAlimente(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (hasGerichtlicheAlimenteregelungChanged(toUpdate, update)) {
            if (toUpdate.getEinnahmenKosten() != null) {
                toUpdate.getEinnahmenKosten().setAlimente(null);
            }

            if (update.getEinnahmenKosten() != null) {
                update.getEinnahmenKosten().setAlimente(null);
            }
        }
    }

    private boolean hasGerichtlicheAlimenteregelungChanged(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (update.getFamiliensituation() == null) {
            return false;
        }

        if (toUpdate.getFamiliensituation() == null) {
            return update.getFamiliensituation().getGerichtlicheAlimentenregelung() != null;
        }

        if (toUpdate.getFamiliensituation().getGerichtlicheAlimentenregelung() == null) {
            return update.getFamiliensituation().getGerichtlicheAlimentenregelung() != null;
        }

        return !toUpdate.getFamiliensituation().getGerichtlicheAlimentenregelung()
            .equals(update.getFamiliensituation().getGerichtlicheAlimentenregelung());
    }

    private void resetEltern(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (update.getElterns() == null) {
            return;
        }

        if (hasAlimenteAufteilungChangedToBoth(toUpdate, update)) {
            update.setElterns(new ArrayList<>());
            return;
        }

        if (isElternteilVerstorbenOrUnbekannt(update)) {
            resetVerstorbenOrUnbekannteElternteile(update);
        }
    }

    private boolean isElternteilVerstorbenOrUnbekannt(GesuchFormularUpdateDto update) {
        return update.getFamiliensituation() != null
            && Boolean.TRUE.equals(update.getFamiliensituation().getElternteilUnbekanntVerstorben());
    }

    private void resetVerstorbenOrUnbekannteElternteile(GesuchFormularUpdateDto update) {
        if (update.getFamiliensituation().getMutterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH) {
            update.getElterns().removeIf(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER);
        }

        if (update.getFamiliensituation().getVaterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH) {
            update.getElterns().removeIf(eltern -> eltern.getElternTyp() == ElternTyp.VATER);
        }
    }

    private void resetAuswaertigesMittagessen(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (!isUpdateToEigenerHaushalt(update)) {
            return;
        }

        if (toUpdate.getEinnahmenKosten() != null) {
            toUpdate.getEinnahmenKosten().setAuswaertigeMittagessenProWoche(null);
        }

        if (update.getEinnahmenKosten() != null) {
            update.getEinnahmenKosten().setAuswaertigeMittagessenProWoche(null);
        }
    }

    private boolean hasAlimenteAufteilungChangedToBoth(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (toUpdate.getFamiliensituation() == null || update.getFamiliensituation() == null) {
            return false;
        }

        return toUpdate.getFamiliensituation().getWerZahltAlimente() != Elternschaftsteilung.GEMEINSAM &&
            update.getFamiliensituation().getWerZahltAlimente() == Elternschaftsteilung.GEMEINSAM;
    }

    private boolean hasGeburtsdatumOfPersonInAusbildungChanged(
        GesuchFormular toUpdate,
        GesuchFormularUpdateDto update) {
        if (toUpdate.getPersonInAusbildung() == null
            || toUpdate.getPersonInAusbildung().getGeburtsdatum() == null
            || update.getPersonInAusbildung() == null) {
            return false;
        }

        return !toUpdate.getPersonInAusbildung()
            .getGeburtsdatum()
            .equals(update.getPersonInAusbildung().getGeburtsdatum());
    }

    private boolean isUpdateToEigenerHaushalt(GesuchFormularUpdateDto update) {
        if (update.getPersonInAusbildung() == null) {
            return false;
        }

        return update.getPersonInAusbildung().getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT;
    }

    private boolean hasZivilstandChangedToOnePerson(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (toUpdate.getPersonInAusbildung() == null
            || toUpdate.getPersonInAusbildung().getZivilstand() == null
            || update.getPersonInAusbildung() == null
            || update.getPersonInAusbildung().getZivilstand() == null) {
            return false;
        }

        return toUpdate.getPersonInAusbildung().getZivilstand().hasPartnerschaft() &&
            !update.getPersonInAusbildung().getZivilstand().hasPartnerschaft();
    }

    private void preventUpdateVonGesuchIfReadOnly(Gesuch gesuch) {
        final var currentBenutzer = benutzerService.getOrCreateCurrentBenutzer();
        if (!gesuch.getGesuchStatus().benutzerCanEdit(currentBenutzer.getBenutzerTyp())) {
            throw new IllegalStateException("Cannot update or delete das Gesuchsformular when parent status is: "
                + gesuch.getGesuchStatus());
        }
    }
}
