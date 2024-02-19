package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jboss.logging.Logger;

public class LebenslaufLuckenlosConstraintValidator
    implements ConstraintValidator<LebenslaufLuckenlosConstraint, GesuchFormular> {
    private static final Logger LOG = Logger.getLogger(LebenslaufLuckenlosConstraintValidator.class);

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getAusbildung() == null) {
            return false;
        }
        LocalDate start = gesuchFormular.getPersonInAusbildung().getGeburtsdatum().withMonth(8).withDayOfMonth(1);
        start = start.plusYears(16);
        LocalDate stop = gesuchFormular.getAusbildung().getAusbildungBegin();
        List<DateRange> dateRanges = new ArrayList<>();
        gesuchFormular.getLebenslaufItems().stream().forEach(
            lebenslaufItem -> dateRanges.add(new DateRange(lebenslaufItem.getVon(), lebenslaufItem.getBis()))
        );
        // Sort the Lebenslaufsitem Daterange
        Collections.sort(dateRanges, Comparator.comparing(dateRange -> dateRange.getGueltigAb()));

        // Check if first Lebenslaufitem is After birth
        LocalDate currentDate = start;
        if (gesuchFormular.getPersonInAusbildung().getGeburtsdatum().isAfter(dateRanges.get(0).getGueltigAb())) {
            LOG.warn("Lebenslauf Item start bevor " + currentDate);
            return false;
        }

        for (DateRange range : dateRanges) {
            if (range.getGueltigAb().isAfter(currentDate)) {
                LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + range.getGueltigAb());
                return false;
            }
            if (range.getGueltigBis().isAfter(stop)) {
                LOG.warn("Lebenslauf Item end after " + currentDate);
                return false;
            }
            currentDate = range.getGueltigBis().plusDays(1);
            if (currentDate.isAfter(stop)) {
                break;
            }
        }

        if (currentDate.isBefore(stop)) {
            LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + stop);
            return false;
        }

        return true;
    }
}
