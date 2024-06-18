package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VermoegenVorjahrRequiredConstraintValidator
    implements ConstraintValidator<VermoegenVorjahrRequiredConstraint, PersonInAusbildung> {

    @Inject
    PlzService plzService;

    @Override
    public boolean isValid(PersonInAusbildung pia, ConstraintValidatorContext context) {
        final var addr = pia.getAdresse();

        if (pia.getNiederlassungsstatus() == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B ||
            pia.getNiederlassungsstatus() == Niederlassungsstatus.FLUECHTLING) {
            return pia.getVermoegenVorjahr() != null;
        }

        if (addr != null && !plzService.isInBern(addr)) {
            return pia.getVermoegenVorjahr() != null;
        }

        return pia.getVermoegenVorjahr() == null;
    }
}
