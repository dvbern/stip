package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class WohnsitzAnteilBerechnungConstraintValidator
		implements ConstraintValidator<WohnsitzAnteilBerechnungConstraint, AbstractFamilieEntity> {

	@Override
	public boolean isValid(
			AbstractFamilieEntity abstractFamilieEntity,
			ConstraintValidatorContext constraintValidatorContext) {
		if (abstractFamilieEntity.getWohnsitz() == Wohnsitz.MUTTER_VATER
				&& abstractFamilieEntity.getWohnsitzAnteilVater() != null
				&& abstractFamilieEntity.getWohnsitzAnteilMutter() != null) {
			return abstractFamilieEntity.getWohnsitzAnteilVater().add(abstractFamilieEntity.getWohnsitzAnteilMutter()).compareTo(new BigDecimal(100)) == 0;
		}
		return true;
	}
}
