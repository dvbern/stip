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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularValidatorService;
import ch.dvbern.stip.api.gesuchformular.validation.AenderungGesuchDokumentsAcceptedValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInBearbeitungSBValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInFreigabeValidationGroup;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheValidatorService {
    private static final Map<Gesuchstatus, List<Class<?>>> gesuchStatusToValidationGroups =
        new EnumMap<>(Gesuchstatus.class);
    private static final Map<GesuchTrancheStatus, List<Class<?>>> trancheStatusToValidationGroups =
        new EnumMap<>(GesuchTrancheStatus.class);

    private static final Map<Gesuchstatus, List<Class<?>>> exceptionalGesuchStatusToValidationGroups =
        new EnumMap<>(Gesuchstatus.class);

    static {
        exceptionalGesuchStatusToValidationGroups.put(
            Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
            List.of(GesuchNachInBearbeitungSBValidationGroup.class)
        );
        gesuchStatusToValidationGroups.put(
            Gesuchstatus.IN_BEARBEITUNG_SB,
            List.of(GesuchNachInFreigabeValidationGroup.class)
        );
        trancheStatusToValidationGroups
            .put(GesuchTrancheStatus.UEBERPRUEFEN, List.of(GesuchEinreichenValidationGroup.class));
        trancheStatusToValidationGroups.put(
            GesuchTrancheStatus.AKZEPTIERT,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class,
                GesuchNachInFreigabeValidationGroup.class,
                AenderungGesuchDokumentsAcceptedValidationGroup.class
            )
        );
        trancheStatusToValidationGroups.put(
            GesuchTrancheStatus.MANUELLE_AENDERUNG,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class,
                AenderungGesuchDokumentsAcceptedValidationGroup.class
            )
        );
        trancheStatusToValidationGroups.put(
            GesuchTrancheStatus.FEHLENDE_DOKUMENTE,
            List.of(GesuchFehlendeDokumenteValidationGroup.class)
        );
    }

    private final Validator validator;
    private final GesuchFormularValidatorService gesuchFormularValidatorService;

    public void validateGesuchTrancheForStatus(
        final GesuchTranche toValidate,
        final GesuchTrancheStatus trancheStatus
    ) {
        final var gesuchstatus = toValidate.getGesuch().getGesuchStatus();
        final var validationGroups = Stream.concat(
            Stream.of(Default.class),
            // First check for exceptional gesuch status validation groups defined by the current gesuch status
            exceptionalGesuchStatusToValidationGroups.getOrDefault(
                gesuchstatus,
                // Otherwise use regular gesuch and tranche validation groups defined by their current status
                Stream.concat(
                    gesuchStatusToValidationGroups.getOrDefault(gesuchstatus, List.of()).stream(),
                    trancheStatusToValidationGroups.getOrDefault(trancheStatus, List.of()).stream()
                ).toList()
            ).stream()
        ).toList();

        ValidatorUtil.validate(validator, toValidate.getGesuchFormular(), validationGroups);
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

}
