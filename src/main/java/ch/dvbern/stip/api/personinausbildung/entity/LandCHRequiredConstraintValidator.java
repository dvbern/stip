package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class LandCHRequiredConstraintValidator implements ConstraintValidator<LandCHRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getAdresse().getLand() == Land.CH) {
			return StringUtils.isNotEmpty(personInAusbildung.getHeimatort());
		}
		return true;
	}
}
