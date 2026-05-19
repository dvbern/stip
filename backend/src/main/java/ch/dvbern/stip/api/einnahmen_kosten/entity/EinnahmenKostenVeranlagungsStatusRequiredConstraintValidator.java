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

package ch.dvbern.stip.api.einnahmen_kosten.entity;

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.type.EinnahmenKostenType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenVeranlagungsStatusRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenVeranlagungsStatusRequiredConstraint, GesuchFormular> {

    private String propertyPath;
    protected EinnahmenKostenType einnahmenKostenType;

    @Override
    public void initialize(EinnahmenKostenVeranlagungsStatusRequiredConstraint constraintAnnotation) {
        propertyPath = constraintAnnotation.property();
        einnahmenKostenType = constraintAnnotation.einnahmenKostenType();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        final var einnahmenKosten = einnahmenKostenType.getProducer().apply(gesuchFormular);
        if (Objects.nonNull(einnahmenKosten) && !Objects.nonNull(einnahmenKosten.getVeranlagungsStatus())) {
            return GesuchValidatorUtil.addProperty(context, propertyPath);
        }

        return true;
    }
}
