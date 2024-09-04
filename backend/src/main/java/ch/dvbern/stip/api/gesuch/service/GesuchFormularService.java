package ch.dvbern.stip.api.gesuch.service;

import java.util.HashSet;

import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.CustomValidationsExceptionMapper;
import ch.dvbern.stip.api.common.exception.ValidationsExceptionMapper;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.validation.AusbildungPageValidation;
import ch.dvbern.stip.api.gesuch.validation.DocumentsRequiredValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.LebenslaufItemPageValidation;
import ch.dvbern.stip.api.gesuch.validation.PersonInAusbildungPageValidation;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchFormularService {
    private final GesuchService gesuchService;
    private final Validator validator;

    public ValidationReportDto validatePages(final GesuchFormular gesuchFormular) {
        final var validationGroups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        validationGroups.add(DocumentsRequiredValidationGroup.class);
        // Since lebenslaufItems are nullable in GesuchFormular the validator has to be added manually if it is not
        // already present
        // Only do this if we are also validating PersonInAusbildungPage and AusbildungPage and not already
        // validating LebenslaufItemPage
        // (i.e. no lebenslaufitem is present)
        if (
            validationGroups.contains(PersonInAusbildungPageValidation.class) &&
                validationGroups.contains(AusbildungPageValidation.class) &&
                !validationGroups.contains(LebenslaufItemPageValidation.class)
        ) {
            validationGroups.add(LebenslaufItemPageValidation.class);
        }

        final var violations = new HashSet<>(
            validator.validate(
                gesuchFormular,
                validationGroups.toArray(new Class<?>[0])
            )
        );
        violations.addAll(validator.validate(gesuchFormular));

        final var validationReportDto = ValidationsExceptionMapper.toDto(violations);
        final var documents = gesuchFormular.getTranche().getGesuchDokuments();
        validationReportDto.hasDocuments(documents != null && !documents.isEmpty());

        try {
            gesuchService.validateNoOtherGesuchEingereichtWithSameSvNumber(
                gesuchFormular,
                gesuchFormular.getTranche().getGesuch().getId()
            );
        } catch (CustomValidationsException exception) {
            CustomValidationsExceptionMapper
                .toDto(exception)
                .getValidationErrors()
                .forEach(validationReportDto::addValidationErrorsItem);
        }

        return validationReportDto;
    }
}
