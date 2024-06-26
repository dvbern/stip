package ch.dvbern.stip.api.gesuch.entity;

import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SteuerdatenTabsRequiredConstraintValidator
    implements ConstraintValidator<SteuerdatenTabsRequiredConstraint, GesuchFormular> {
    private String property;

    @Inject
    SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    @Override
    public void initialize(SteuerdatenTabsRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        if (gesuchFormular.getFamiliensituation() == null) {
            return true;
        }

        final var requiredTabs = new HashSet<>(
            steuerdatenTabBerechnungsService.calculateTabs(gesuchFormular.getFamiliensituation())
        );
        final var actualTabs = gesuchFormular.getSteuerdaten()
            .stream()
            .map(Steuerdaten::getSteuerdatenTyp)
            .collect(Collectors.toSet());

        if (requiredTabs.size() != actualTabs.size() || !requiredTabs.containsAll(actualTabs)) {
            return GesuchValidatorUtil.addProperty(context, property);
        }

        return true;
    }
}
