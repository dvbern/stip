package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator implements ConstraintValidator<SteuerdatenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getSteuerdaten() == null ||
            gesuchFormular.getSteuerdaten().isEmpty()
        ) {
            return true;
        }

        // This is fine, the @NotNull constraints on the properties will trigger
        if (gesuchFormular.getTranche() == null ||
            gesuchFormular.getTranche().getGesuch() == null
        ) {
            return true;
        }

        final var gesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr();
        AtomicBoolean isValid = new AtomicBoolean(true);
        gesuchFormular.getSteuerdaten().forEach(steuerdaten -> {
            if (!isSteuerjahrValid(steuerdaten, gesuchsjahr)) {
                isValid.set(false);
            }
        });
        return isValid.get();
    }

    private boolean isSteuerjahrValid(Steuerdaten steuerdaten, Gesuchsjahr gesuchsjahr) {
        return steuerdaten.getSteuerjahr() <= GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
    }
}
