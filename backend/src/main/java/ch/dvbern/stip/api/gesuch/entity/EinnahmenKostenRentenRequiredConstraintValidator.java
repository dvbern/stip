package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenRentenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenRentenRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenRentenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

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
            if (gesuchFormular.getEinnahmenKosten().getRenten() == null) {
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            } else {
                return true;
            }
        }
        return true;
    }
}
