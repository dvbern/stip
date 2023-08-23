package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenDarlehenRequiredConstraintValidator
		implements ConstraintValidator<EinnahmenKostenDarlehenRequiredConstraint, GesuchFormular> {
	@Override
	public boolean isValid(
			GesuchFormular gesuchFormular,
			ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
			return true;
		} else if (isVolljaehrig(gesuchFormular.getPersonInAusbildung().getGeburtsdatum())) {
			return gesuchFormular.getEinnahmenKosten().getWillDarlehen() != null;
		}
		return true;
	}

	private static boolean isVolljaehrig(LocalDate geburtsdatum) {
		if (geburtsdatum == null) {
			return false;
		}
		LocalDate localDate = LocalDate.now();
		return localDate.minusDays(geburtsdatum.getDayOfMonth())
				.minusMonths(geburtsdatum.getMonthValue())
				.minusYears(geburtsdatum.getYear())
				.getYear() >= 18;
	}
}
