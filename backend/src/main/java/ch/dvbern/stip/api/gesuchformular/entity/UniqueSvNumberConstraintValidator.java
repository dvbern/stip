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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueSvNumberConstraintValidator
implements ConstraintValidator<UniqueSvNumberConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        List<String> svNumbers = new ArrayList<>();

        if (gesuchFormular.getPersonInAusbildung() != null) {
            svNumbers.add(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());
        }

        if (gesuchFormular.getElterns() != null) {
            gesuchFormular.getElterns().forEach(eltern -> svNumbers.add(eltern.getSozialversicherungsnummer()));
        }

        if (gesuchFormular.getPartner() != null) {
            svNumbers.add(gesuchFormular.getPartner().getSozialversicherungsnummer());
        }

        return !hasDuplicates(svNumbers);
    }

    private boolean hasDuplicates(List<String> svNumbers) {
        Set<String> svNumberSet = new HashSet<>();
        return svNumbers.stream()
            .anyMatch(number -> !svNumberSet.add(number) && number != null);
    }
}
