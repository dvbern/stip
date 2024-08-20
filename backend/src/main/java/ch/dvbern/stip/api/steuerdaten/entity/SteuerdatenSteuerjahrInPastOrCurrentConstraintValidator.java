package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE;

public class SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator
    implements ConstraintValidator<SteuerdatenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {
    private String property;

    @Override
    public void initialize(SteuerdatenSteuerjahrInPastOrCurrentConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

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
        final var allValid = gesuchFormular.getSteuerdaten().stream()
            .allMatch(steuerdaten -> isSteuerjahrValid(steuerdaten, gesuchsjahr));

        final var invalidSteuerdaten = gesuchFormular.getSteuerdaten().stream()
            .filter(steuerdaten ->  !isSteuerjahrValid(steuerdaten, gesuchsjahr))
            .toList();

        if (invalidSteuerdaten.isEmpty()) {
            return true;
        }

        for (final var invalid : invalidSteuerdaten) {
            final var pagePostfix = switch (invalid.getSteuerdatenTyp()) {
                case FAMILIE -> "Familie";
                case VATER -> "Vater";
                case MUTTER -> "Mutter";
            };

            GesuchValidatorUtil.addProperty(
                constraintValidatorContext,
                VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE,
                property + pagePostfix
            );
        }

        return false;
    }

    private boolean isSteuerjahrValid(Steuerdaten steuerdaten, Gesuchsjahr gesuchsjahr) {
        return (steuerdaten.getSteuerjahr() != null)
            && (steuerdaten.getSteuerjahr() <= GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr));
    }
}
