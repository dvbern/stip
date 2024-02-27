package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenRentenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenRentenRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getFamiliensituation() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }

        final var elternteilUnbekannt = gesuchFormular.getFamiliensituation().getElternteilUnbekanntVerstorben();
        // If elternteil is unbekannt and mother or father are dead
        if ((elternteilUnbekannt != null && elternteilUnbekannt) && (
            gesuchFormular.getFamiliensituation().getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN ||
            gesuchFormular.getFamiliensituation().getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN)
        ) {
            return gesuchFormular.getEinnahmenKosten().getRenten() != null;
        }
        return true;
    }
}
