package ch.dvbern.stip.api.gesuch.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchDokumentsAcceptedValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchValidatorService {
    private static final Map<Gesuchstatus, List<Class<?>>> statusToValidationGroup = new EnumMap<>(Gesuchstatus.class);

    static {
        statusToValidationGroup.put(Gesuchstatus.EINGEREICHT, List.of(GesuchEinreichenValidationGroup.class));
        statusToValidationGroup.put(Gesuchstatus.BEREIT_FUER_BEARBEITUNG, List.of(GesuchEinreichenValidationGroup.class));
        statusToValidationGroup.put(
            Gesuchstatus.FEHLENDE_DOKUMENTE,
            List.of(GesuchFehlendeDokumenteValidationGroup.class)
        );
        statusToValidationGroup.put(
            Gesuchstatus.VERFUEGT,
            List.of(GesuchEinreichenValidationGroup.class, GesuchDokumentsAcceptedValidationGroup.class)
        );
        statusToValidationGroup.put(
            Gesuchstatus.IN_FREIGABE,
            List.of(GesuchEinreichenValidationGroup.class, GesuchDokumentsAcceptedValidationGroup.class)
        );
        statusToValidationGroup.put(Gesuchstatus.VERSENDET, List.of(GesuchEinreichenValidationGroup.class));
    }

    private final Validator validator;

    public void validateGesuchForStatus(final Gesuch toValidate, final Gesuchstatus status) {
        final var validationGroups = statusToValidationGroup.getOrDefault(status, List.of());
        ValidatorUtil.validate(validator, toValidate, validationGroups);
    }
}
