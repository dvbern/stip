package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenDarlehenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenDarlehenRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenDarlehenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    private static boolean isVolljaehrig(LocalDate geburtsdatum) {
        if (geburtsdatum == null) {
            return false;
        }
        LocalDate volljaehrigCompareDate = LocalDate.now().minusYears(18);
        return geburtsdatum.isBefore(volljaehrigCompareDate) || geburtsdatum.isEqual(volljaehrigCompareDate);
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }
        if (isVolljaehrig(gesuchFormular.getPersonInAusbildung().getGeburtsdatum())) {
            if (gesuchFormular.getEinnahmenKosten().getWillDarlehen() == null) {
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            } else {
                return true;
            }
        }
        return true;
    }
}
