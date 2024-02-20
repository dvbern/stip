package ch.dvbern.stip.api.familiensituation.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ObhutGemeinsamBerechnungConstraintValidator
    implements ConstraintValidator<ObhutGemeinsamBerechnungConstraint, Familiensituation> {

    @Override
    public boolean isValid(
        Familiensituation familiensituation,
        ConstraintValidatorContext constraintValidatorContext) {
        if (familiensituation.getObhut() == Elternschaftsteilung.GEMEINSAM
            && familiensituation.getObhutMutter() != null
            && familiensituation.getObhutVater() != null) {
            return familiensituation.getObhutMutter()
                .add(familiensituation.getObhutVater())
                .compareTo(new BigDecimal(100)) == 0;
        }
        return true;
    }
}
