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

package ch.dvbern.stip.api.common.validation;

import java.util.Objects;

import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenPartnerNeglectedFieldsNullConstraintValidator
    implements ConstraintValidator<EinnahmenKostenPartnerNeglectedFieldsNullConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenPartnerNeglectedFieldsNullConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        if (gesuchFormular.getEinnahmenKostenPartner() == null) {
            return true;
        }

        final var einnahmeKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
        return Objects.isNull(einnahmeKostenPartner.getAusbildungskosten())
        && Objects.isNull(einnahmeKostenPartner.getWohnkosten())
        && Objects.isNull(einnahmeKostenPartner.getWgWohnend())
        && Objects.isNull(einnahmeKostenPartner.getAlternativeWohnformWohnend());
    }
}
