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
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularValidatorService;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheValidatorService {
    private static final Map<GesuchTrancheStatus, Class<?>> statusToValidationGroup =
        new EnumMap<>(GesuchTrancheStatus.class);

    static {
        statusToValidationGroup.put(GesuchTrancheStatus.IN_BEARBEITUNG_GS, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(GesuchTrancheStatus.UEBERPRUEFEN, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(GesuchTrancheStatus.AKZEPTIERT, GesuchEinreichenValidationGroup.class);
    }

    private final Validator validator;
    private final GesuchRepository gesuchRepository;
    private final GesuchFormularValidatorService gesuchFormularValidatorService;

    public void validateGesuchTrancheForStatus(final GesuchTranche toValidate, final GesuchTrancheStatus status) {
        final var validationGroup = statusToValidationGroup.getOrDefault(status, null);
        if (validationGroup != null) {
            ValidatorUtil.validate(validator, toValidate.getGesuchFormular(), validationGroup);
        }
    }

    public void validateGesuchTrancheForEinreichen(final GesuchTranche toValidate) {
        validateAdditionalEinreichenCriteria(toValidate);
        validateGesuchTrancheForStatus(toValidate, GesuchTrancheStatus.AKZEPTIERT);
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