package ch.dvbern.stip.api.gesuch.service;

import java.util.EnumMap;
import java.util.Map;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchValidatorService {
    private static final Map<Gesuchstatus, Class<?>> statusToValidationGroup = new EnumMap<>(Gesuchstatus.class);

    static {
        statusToValidationGroup.put(Gesuchstatus.EINGEREICHT, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.BEREIT_FUER_BEARBEITUNG, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.FEHLENDE_DOKUMENTE, GesuchFehlendeDokumenteValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.VERFUEGT, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.IN_FREIGABE, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(Gesuchstatus.VERSENDET, GesuchEinreichenValidationGroup.class);
    }

    private final Validator validator;

    public void validateGesuchForStatus(final Gesuch toValidate, final Gesuchstatus status) {
        final var validationGroup = statusToValidationGroup.getOrDefault(status, null);
        ValidatorUtil.validate(validator, toValidate, validationGroup);
    }
}
