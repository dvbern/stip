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

package ch.dvbern.stip.berechnung.service;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonenImHaushaltService {
    private final Instance<PersonenImHaushaltCalculator> personenImHaushaltCalculators;
    private final Instance<PersonenImHaushaltRequestBuilder> personenImHaushaltRequestBuilders;

    public CalculatorRequest getPersonenImHaushaltRequest(
        final int majorVersion,
        final int minorVersion,
        final GesuchFormular gesuchFormular,
        final ElternTyp elternToPrioritize
    ) {
        final var builder = personenImHaushaltRequestBuilders.stream().filter(personenImHaushaltRequestBuilder -> {
            final var versionAnnotation =
                personenImHaushaltRequestBuilder.getClass().getAnnotation(CalculatorVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (builder.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a builder for version "
                + majorVersion
                + '.'
                + minorVersion
            );
        }

        return builder.get().buildRequest(gesuchFormular, elternToPrioritize);
    }

    public PersonenImHaushaltResult calculatePersonenImHaushalt(final CalculatorRequest request) {
        final var calculator = personenImHaushaltCalculators.stream()
            .filter(possibleCalculator -> {
                final var versionAnnotation = possibleCalculator.getClass().getAnnotation(CalculatorVersion.class);
                return versionAnnotation != null &&
                versionAnnotation.major() == request.majorVersion() &&
                versionAnnotation.minor() == request.minorVersion();
            })
            .findFirst();

        if (calculator.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a Personen im Haushalt Calculator for version " + request.getVersion()
            );
        }

        return calculator.get().calculatePersonenImHaushalt(request);
    }
}
