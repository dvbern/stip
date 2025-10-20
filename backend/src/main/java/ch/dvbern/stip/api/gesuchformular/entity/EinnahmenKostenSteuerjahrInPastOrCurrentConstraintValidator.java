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

package ch.dvbern.stip.api.gesuchformular.entity;

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchformular.type.EinnahmenKostenType;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator
    implements ConstraintValidator<EinnahmenKostenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {

    private String propertyPath;
    protected EinnahmenKostenType einnahmenKostenType;

    @Override
    public void initialize(EinnahmenKostenSteuerjahrInPastOrCurrentConstraint constraintAnnotation) {
        propertyPath = constraintAnnotation.property();
        einnahmenKostenType = constraintAnnotation.einnahmenKostenType();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (
            gesuchFormular.getEinnahmenKosten() == null ||
            gesuchFormular.getEinnahmenKosten().getSteuerjahr() == null
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

        final var einnahmeKosten = einnahmenKostenType.getProducer().apply(gesuchFormular);
        if (Objects.isNull(einnahmeKosten)) {
            return true;
        }

        final var gesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr();
        final var isValid =
            einnahmeKosten.getSteuerjahr() <= GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
        if (!isValid) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, propertyPath);
        }

        return true;
    }
}
