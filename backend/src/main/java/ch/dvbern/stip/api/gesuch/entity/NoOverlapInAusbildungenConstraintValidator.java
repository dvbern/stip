package ch.dvbern.stip.api.gesuch.entity;

import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoOverlapInAusbildungenConstraintValidator
    implements ConstraintValidator<NoOverlapInAusbildungenConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(NoOverlapInAusbildungenConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getAusbildung() == null) {
            return true;
        }

        final var ausbildung = gesuchFormular.getAusbildung();
        if (ausbildung.getAusbildungBegin() == null || ausbildung.getAusbildungEnd() == null) {
            return true;
        }

        if (hasOverlap(ausbildung, gesuchFormular.getLebenslaufItems().stream())) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        } else {
            return true;
        }
    }

    public boolean hasOverlap(final Ausbildung ausbildung, final Stream<LebenslaufItem> lebenslaufItems) {
        final var ausbildungStart = ausbildung.getAusbildungBegin();
        final var ausbildungEnd = ausbildung.getAusbildungEnd();
        return lebenslaufItems.filter(x -> x.getBildungsart() != null).anyMatch(item -> {
            if (
                (ausbildungStart.isBefore(item.getBis()) || ausbildungStart.isEqual(item.getBis())) &&
                (ausbildungEnd.isAfter(item.getVon()) || ausbildungEnd.isEqual(item.getVon()))
            ) {
                return true;
            }

            return false;
        });
    }
}
