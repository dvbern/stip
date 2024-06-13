package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzOrtService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class VermoegenVorjahrRequiredConstraintValidator
    implements ConstraintValidator<VermoegenVorjahrRequiredConstraint, PersonInAusbildung> {
    // For now, PLZ like 3xxx are in Bern
    // Replace once we have address data from the canton
    //private static final Pattern IS_BERN = Pattern.compile("3\\d{3}$");
    @Inject
    PlzOrtService plzOrtService;

    @Override
    public boolean isValid(PersonInAusbildung pia, ConstraintValidatorContext context) {
        final var addr = pia.getAdresse();
        if (addr != null && !plzOrtService.isInBern(addr)) {
            return pia.getVermoegenVorjahr() != null;
        }

        if (pia.getNiederlassungsstatus() == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B ||
            pia.getNiederlassungsstatus() == Niederlassungsstatus.FLUECHTLING) {
            return pia.getVermoegenVorjahr() != null;
        }

        return pia.getVermoegenVorjahr() == null;
    }
}
