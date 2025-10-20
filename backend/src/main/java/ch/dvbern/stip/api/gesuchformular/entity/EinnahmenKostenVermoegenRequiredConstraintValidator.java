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

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchformular.type.EinnahmenKostenType;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenVermoegenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenVermoegenRequiredConstraint, GesuchFormular> {
    private String property = "";
    protected EinnahmenKostenType einnahmenKostenTyp;

    @Override
    public void initialize(EinnahmenKostenVermoegenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
        einnahmenKostenTyp = constraintAnnotation.einnahmenKostenType();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        final var einnahmenKosten = einnahmenKostenTyp.getProducer().apply(gesuchFormular);
        if (einnahmenKosten == null) {
            return true;
        }
        final var hasVermoegen = einnahmenKosten.getVermoegen() != null;
        final boolean personOfEinnahmeKostenWasOlderThan18 =
            einnahmenKostenTyp.equals(EinnahmenKostenType.GESUCHSTELLER)
                ? GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)
                : GesuchFormularCalculationUtil.wasPartnerOlderThan18(gesuchFormular);
        if (personOfEinnahmeKostenWasOlderThan18) {
            if (!hasVermoegen) {
                return GesuchValidatorUtil.addProperty(
                    context,
                    property
                );
            }
        } else {
            if (hasVermoegen) {
                return GesuchValidatorUtil.addProperty(
                    context,
                    property
                );
            }
        }
        return true;
    }
}
