package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator
    implements ConstraintValidator<EinnahmenKostenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getEinnahmenKosten() == null ||
            gesuchFormular.getEinnahmenKosten().getSteuerjahr() == null
        ) {
            return true;
        }

        final var gesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr();
        return gesuchFormular.getEinnahmenKosten().getSteuerjahr() <= GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
    }
}
