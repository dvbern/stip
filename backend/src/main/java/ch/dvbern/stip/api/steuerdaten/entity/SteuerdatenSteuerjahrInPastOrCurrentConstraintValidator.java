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

package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE;

public class SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator
implements ConstraintValidator<SteuerdatenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {
    private String property;

    @Override
    public void initialize(SteuerdatenSteuerjahrInPastOrCurrentConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (
            gesuchFormular.getSteuerdaten() == null ||
            gesuchFormular.getSteuerdaten().isEmpty()
        ) {
            return true;
        }

        // This is fine, the @NotNull constraints on the properties will trigger
        if (
            gesuchFormular.getTranche() == null ||
            gesuchFormular.getTranche().getGesuch() == null
        ) {
            return true;
        }

        final var gesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr();
        final var invalidSteuerdaten = gesuchFormular.getSteuerdaten()
            .stream()
            .filter(steuerdaten -> !isSteuerjahrValid(steuerdaten, gesuchsjahr))
            .toList();

        if (invalidSteuerdaten.isEmpty()) {
            return true;
        }

        for (final var invalid : invalidSteuerdaten) {
            final var pagePostfix = switch (invalid.getSteuerdatenTyp()) {
                case FAMILIE -> "";
                case VATER -> "Vater";
                case MUTTER -> "Mutter";
            };

            GesuchValidatorUtil.addProperty(
                constraintValidatorContext,
                VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE,
                property + pagePostfix
            );
        }

        return false;
    }

    private boolean isSteuerjahrValid(Steuerdaten steuerdaten, Gesuchsjahr gesuchsjahr) {
        return (steuerdaten.getSteuerjahr() != null)
        && (steuerdaten.getSteuerjahr() <= GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr));
    }
}
