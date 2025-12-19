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

import java.util.HashSet;

import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.validation.DocumentsRequiredValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchNachInBearbeitungSBValidationGroup;
import ch.dvbern.stip.api.gesuchformular.validation.LebenslaufItemPageValidation;
import ch.dvbern.stip.api.gesuchformular.validation.PersonInAusbildungPageValidation;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchFormularService {
    private final Validator validator;
    private final GesuchFormularValidatorService gesuchFormularValidatorService;

    public ValidationReportDto validatePages(final GesuchFormular gesuchFormular) {
        final var validationGroups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        validationGroups.add(DocumentsRequiredValidationGroup.class);
        // Since lebenslaufItems are nullable in GesuchFormular the validator has to be added manually if it is not
        // already present
        // Only do this if we are also validating PersonInAusbildungPage and not already
        // validating LebenslaufItemPage
        // (i.e. no lebenslaufitem is present)
        if (
            validationGroups.contains(PersonInAusbildungPageValidation.class) &&
            !validationGroups.contains(LebenslaufItemPageValidation.class)
        ) {
            validationGroups.add(LebenslaufItemPageValidation.class);
        }
        if (gesuchFormular.getTranche().getGesuch().getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB) {
            validationGroups.add(GesuchNachInBearbeitungSBValidationGroup.class);
        }

        final var violations = new HashSet<>(
            validator.validate(
                gesuchFormular,
                validationGroups.toArray(new Class<?>[0])
            )
        );
        violations.addAll(validator.validate(gesuchFormular));

        final var documents = gesuchFormular.getTranche().getGesuchDokuments();
        final Boolean hasDocuments = documents != null && !documents.isEmpty();
        final var validationReportDto = ValidationsExceptionMapper.toDto(violations, hasDocuments);

        try {
            gesuchFormularValidatorService.validateNoOtherGesuchWithSameSvNumber(
                gesuchFormular.getPersonInAusbildung(),
                gesuchFormular.getTranche().getGesuch().getId()
            );
        } catch (CustomValidationsException exception) {
            ValidationsExceptionMapper
                .toDto(exception)
                .getValidationErrors()
                .forEach(validationReportDto::addValidationErrorsItem);
        }

        return validationReportDto;
    }
}
