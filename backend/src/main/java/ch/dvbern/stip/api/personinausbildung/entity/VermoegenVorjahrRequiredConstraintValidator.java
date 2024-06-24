package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzOrtService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class VermoegenVorjahrRequiredConstraintValidator
    implements ConstraintValidator<VermoegenVorjahrRequiredConstraint, PersonInAusbildung> {
    @Inject
    PlzOrtService plzOrtService;

    @Inject
    EntityManager entityManager;

    @Override
    public boolean isValid(PersonInAusbildung pia, ConstraintValidatorContext context) {
        final var addr = pia.getAdresse();
        final var flushmode = entityManager.getFlushMode();
        entityManager.setFlushMode(FlushModeType.COMMIT);

        if (addr != null && !plzOrtService.isInBern(addr)) {
            entityManager.setFlushMode(flushmode);
            return pia.getVermoegenVorjahr() != null;
        }
        entityManager.setFlushMode(flushmode);

        if (pia.getNiederlassungsstatus() == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B ||
            pia.getNiederlassungsstatus() == Niederlassungsstatus.FLUECHTLING) {
            return pia.getVermoegenVorjahr() != null;
        }

        return pia.getVermoegenVorjahr() == null;
    }
}
