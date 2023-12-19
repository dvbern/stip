package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueSvNumberConstraintValidator
		implements ConstraintValidator<UniqueSvNumberConstraint, GesuchFormular> {
	@Override
	public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
		List<String> svNumbers = new ArrayList<>();

		if (gesuchFormular.getPersonInAusbildung() != null) {
			svNumbers.add(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());
		}

		if (gesuchFormular.getElterns() != null) {
			gesuchFormular.getElterns().forEach(eltern -> svNumbers.add(eltern.getSozialversicherungsnummer()));
		}

		if (gesuchFormular.getPartner() != null) {
			svNumbers.add(gesuchFormular.getPartner().getSozialversicherungsnummer());
		}

		return !hasDuplicates(svNumbers);
	}

	private boolean hasDuplicates(List<String> svNumbers) {
		Set<String> svNumberSet = new HashSet<>();
		return svNumbers.stream()
				.anyMatch(number -> !svNumberSet.add(number));
	}
}