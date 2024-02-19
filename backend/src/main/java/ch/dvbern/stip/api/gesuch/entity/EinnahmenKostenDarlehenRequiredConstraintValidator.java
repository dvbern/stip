package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class EinnahmenKostenDarlehenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenDarlehenRequiredConstraint, GesuchFormular> {
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
            return gesuchFormular.getEinnahmenKosten().getWillDarlehen() != null;
        }
        return true;
    }
}
