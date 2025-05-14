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

import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class SteuererklaerungTabsRequiredConstraintValidator
    implements ConstraintValidator<SteuererklaerungTabsRequiredConstraint, GesuchFormular> {
    private String property;

    @Inject
    SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    @Override
    public void initialize(SteuererklaerungTabsRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        if (gesuchFormular.getFamiliensituation() == null) {
            return true;
        }

        final var requiredTabs = new HashSet<>(
            steuerdatenTabBerechnungsService.calculateTabs(gesuchFormular.getFamiliensituation())
        );
        final var actualTabs = gesuchFormular.getSteuererklaerung()
            .stream()
            .map(Steuererklaerung::getSteuerdatenTyp)
            .collect(Collectors.toSet());

        if (requiredTabs.size() != actualTabs.size() || !requiredTabs.containsAll(actualTabs)) {
            requiredTabs.stream().filter(tab -> !actualTabs.contains(tab)).forEach(tab -> {
                GesuchValidatorUtil
                    .addProperty(context, property.concat(StringUtils.capitalize(tab.name().toLowerCase())));
            });
            return false;
        }

        return true;
    }
}
