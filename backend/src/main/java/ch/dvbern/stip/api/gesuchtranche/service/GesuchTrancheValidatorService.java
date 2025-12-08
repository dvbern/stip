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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularValidatorService;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchDokumentsAcceptedValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInBearbeitungSBValidationGroup;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchvalidation.service.GesuchValidatorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheValidatorService {
    private static final Map<GesuchTrancheStatus, List<Class<?>>> statusToValidationGroups =
        new EnumMap<>(GesuchTrancheStatus.class);

    static {
        statusToValidationGroups.put(GesuchTrancheStatus.UEBERPRUEFEN, List.of(GesuchEinreichenValidationGroup.class));
        statusToValidationGroups.put(
            GesuchTrancheStatus.AKZEPTIERT,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class,
                GesuchDokumentsAcceptedValidationGroup.class
            )
        );
        statusToValidationGroups.put(
            GesuchTrancheStatus.MANUELLE_AENDERUNG,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class,
                GesuchDokumentsAcceptedValidationGroup.class
            )
        );
        statusToValidationGroups.put(
            GesuchTrancheStatus.FEHLENDE_DOKUMENTE,
            List.of(GesuchFehlendeDokumenteValidationGroup.class)
        );
    }

    private final Validator validator;
    private final GesuchFormularValidatorService gesuchFormularValidatorService;

    public void validateGesuchTrancheForStatus(final GesuchTranche toValidate, final GesuchTrancheStatus status) {
        final var validationGroups = Stream.concat(
            Stream.of(Default.class),
            statusToValidationGroups.getOrDefault(status, List.of()).stream()
        ).toList();
        ValidatorUtil.validate(validator, toValidate.getGesuchFormular(), validationGroups);
    }

    public void validateGesuchTrancheForCurrentStatus(final GesuchTranche toValidate) {
        validateGesuchTrancheForStatus(toValidate, toValidate.getStatus());
    }

    public void validateAenderungForAkzeptiert(final GesuchTranche toValidate) {
        validateAdditionalEinreichenCriteria(toValidate);
        validateGesuchTrancheForStatus(toValidate, GesuchTrancheStatus.AKZEPTIERT);
    }

    public void validateGesuchTrancheForEinreichen(final GesuchTranche toValidate) {
        validateAdditionalEinreichenCriteria(toValidate);
        validateGesuchTrancheForStatus(toValidate, GesuchTrancheStatus.UEBERPRUEFEN);
    }

    public void validateAdditionalEinreichenCriteria(final GesuchTranche toValidate) {
        if (toValidate.getGesuchFormular() == null) {
            throw new ValidationsException("GesuchFormular must be set", Set.of());
        }

        if (toValidate.getGesuch() == null) {
            throw new ValidationsException("Gesuch must be set", Set.of());
        }

        final var gesuch = toValidate.getGesuch();
        // 1 Tranche and it's a TRANCHE means first einreichen
        if (gesuch.getGesuchTranchen().size() == 1 && toValidate.getTyp() == GesuchTrancheTyp.TRANCHE) {
            gesuchFormularValidatorService.validateNoOtherGesuchWithSameSvNumber(
                toValidate.getGesuchFormular().getPersonInAusbildung(),
                gesuch.getId()
            );
        }
    }

    @Transactional
    public void validateBearbeitungAbschliessen(final Gesuch gesuch) {
        final var validationGroups = new ArrayList<>(
            GesuchValidatorService.SB_ABSCHLIESSEN_VALIDATION_GROUPS
        );

        Set<ConstraintViolation<Gesuch>> violations =
            validator.validate(gesuch, validationGroups.toArray(new Class<?>[0]));
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
    }

}
