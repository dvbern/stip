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

package ch.dvbern.stip.api.gesuchformular.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchFormularValidatorService {
    private final GesuchRepository gesuchRepository;

    public void validateNoOtherGesuchWithSameSvNumber(final PersonInAusbildung pia, final UUID gesuchId) {
        if (pia == null) {
            return;
        }

        final var svNummer = pia.getSozialversicherungsnummer();
        final var gesuche = gesuchRepository.findGesucheBySvNummer(svNummer);
        if (gesuche.anyMatch(gesuch -> gesuch.getGesuchStatus().isEingereicht() && !gesuch.getId().equals(gesuchId))) {
            throw new CustomValidationsException(
                "Person in Ausbildung (via SV-Nummer) already has a eingereicht Gesuch",
                new CustomConstraintViolation(
                    VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE,
                    "personInAusbildung"
                )
            );
        }
    }
}
