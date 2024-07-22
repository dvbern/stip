package ch.dvbern.stip.api.gesuch.service;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchValidatorService {
    private static final Map<Gesuchstatus, Class<?>> statusToValidationGroup = new EnumMap<>(Gesuchstatus.class);

    static {
        statusToValidationGroup.put(Gesuchstatus.EINGEREICHT, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.FEHLENDE_DOKUMENTE, GesuchFehlendeDokumenteValidationGroup.class);
    }

    private final Validator validator;

    public void validateGesuchForStatus(Gesuch toValidate, Gesuchstatus status) {
        final var concatenatedViolations = new HashSet<>(validator.validate(toValidate));

        final var validationGroup = statusToValidationGroup.getOrDefault(status, null);
        if (validationGroup != null) {
            concatenatedViolations.addAll(validator.validate(toValidate, validationGroup));
        }

        if (!concatenatedViolations.isEmpty()) {
            throw new ValidationsException(
                String.format("Das Gesuch ist nicht valid für den Status %s: ", status),
                concatenatedViolations
            );
        }
    }
}
