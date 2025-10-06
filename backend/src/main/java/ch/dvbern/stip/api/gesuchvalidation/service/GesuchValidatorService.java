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
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchDokumentsAcceptedValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInBearbeitungSBValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.LandMustBeGueltigValidationGroup;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchValidatorService {
    private static final Map<Gesuchstatus, List<Class<?>>> destinationValidationGroups =
        new EnumMap<>(Gesuchstatus.class);
    private static final Map<Gesuchstatus, List<Class<?>>> originValidationGroups = new EnumMap<>(Gesuchstatus.class);

    public static final List<Class<?>> SB_ABSCHLIESSEN_VALIDATION_GROUPS = List.of(
        Default.class,
        GesuchEinreichenValidationGroup.class,
        GesuchDokumentsAcceptedValidationGroup.class,
        GesuchNachInBearbeitungSBValidationGroup.class
    );
    static {
        destinationValidationGroups.put(Gesuchstatus.EINGEREICHT, List.of(GesuchEinreichenValidationGroup.class));
        destinationValidationGroups
            .put(Gesuchstatus.BEREIT_FUER_BEARBEITUNG, List.of(GesuchEinreichenValidationGroup.class));
        destinationValidationGroups.put(
            Gesuchstatus.FEHLENDE_DOKUMENTE,
            List.of(GesuchFehlendeDokumenteValidationGroup.class)
        );
        destinationValidationGroups.put(Gesuchstatus.VERFUEGT, SB_ABSCHLIESSEN_VALIDATION_GROUPS);
        destinationValidationGroups.put(Gesuchstatus.IN_FREIGABE, SB_ABSCHLIESSEN_VALIDATION_GROUPS);
        destinationValidationGroups.put(
            Gesuchstatus.VERFUEGUNG_VERSENDET,
            List.of(GesuchEinreichenValidationGroup.class, GesuchNachInBearbeitungSBValidationGroup.class)
        );

        originValidationGroups.put(Gesuchstatus.IN_BEARBEITUNG_GS, List.of(LandMustBeGueltigValidationGroup.class));
        originValidationGroups.put(Gesuchstatus.IN_BEARBEITUNG_SB, List.of(LandMustBeGueltigValidationGroup.class));
        originValidationGroups
            .put(Gesuchstatus.JURISTISCHE_ABKLAERUNG, List.of(LandMustBeGueltigValidationGroup.class));
        originValidationGroups.put(Gesuchstatus.FEHLENDE_DOKUMENTE, List.of(LandMustBeGueltigValidationGroup.class));
    }

    private final Validator validator;

    public void validateGesuchForTransition(
        final Gesuch toValidate,
        final Transition<Gesuchstatus, ?> transition
    ) {
        final var validationGroups = Stream.concat(
            Stream.of(Default.class),
            Stream.concat(
                destinationValidationGroups.getOrDefault(transition.getDestination(), List.of()).stream(),
                originValidationGroups.getOrDefault(toValidate.getGesuchStatus(), List.of()).stream()
            )
        ).toList();
        ValidatorUtil.validate(validator, toValidate, validationGroups);
    }
}
