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

package ch.dvbern.stip.api.gesuchvalidation.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchDokumentsAcceptedValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInBearbeitungSBValidationGroup;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchValidatorService {
    private static final Map<Gesuchstatus, List<Class<?>>> statusToValidationGroups = new EnumMap<>(Gesuchstatus.class);
    private final GesuchTrancheRepository gesuchTrancheRepository;
    static {
        statusToValidationGroups.put(Gesuchstatus.EINGEREICHT, List.of(GesuchEinreichenValidationGroup.class));
        statusToValidationGroups
            .put(Gesuchstatus.BEREIT_FUER_BEARBEITUNG, List.of(GesuchEinreichenValidationGroup.class));
        statusToValidationGroups.put(
            Gesuchstatus.FEHLENDE_DOKUMENTE,
            List.of(GesuchFehlendeDokumenteValidationGroup.class)
        );
        statusToValidationGroups.put(
            Gesuchstatus.VERFUEGT,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchDokumentsAcceptedValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class
            )
        );
        statusToValidationGroups.put(
            Gesuchstatus.IN_FREIGABE,
            List.of(
                GesuchEinreichenValidationGroup.class,
                GesuchDokumentsAcceptedValidationGroup.class,
                GesuchNachInBearbeitungSBValidationGroup.class
            )
        );
        statusToValidationGroups.put(
            Gesuchstatus.VERSENDET,
            List.of(GesuchEinreichenValidationGroup.class, GesuchNachInBearbeitungSBValidationGroup.class)
        );
    }

    private final Validator validator;

    public void validateGesuchForStatus(final Gesuch toValidate, final Gesuchstatus status) {
        final var validationGroups = statusToValidationGroups.getOrDefault(status, List.of());
        ValidatorUtil.validate(validator, toValidate, validationGroups);
    }

}
