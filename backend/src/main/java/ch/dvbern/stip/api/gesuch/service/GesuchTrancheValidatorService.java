package ch.dvbern.stip.api.gesuch.service;

import java.util.EnumMap;
import java.util.Map;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheValidatorService {
    private static final Map<GesuchTrancheStatus, Class<?>> statusToValidationGroup =
        new EnumMap<>(GesuchTrancheStatus.class);

    static {
        statusToValidationGroup.put(GesuchTrancheStatus.UEBERPRUEFEN, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(GesuchTrancheStatus.AKZEPTIERT, GesuchEinreichenValidationGroup.class);
    }

    private final Validator validator;

    public void validateGesuchTrancheForStatus(final GesuchTranche toValidate, final GesuchTrancheStatus status) {
        final var validationGroup = statusToValidationGroup.getOrDefault(status, null);
        ValidatorUtil.validate(validator, toValidate.getGesuchFormular(), validationGroup);
    }
}
