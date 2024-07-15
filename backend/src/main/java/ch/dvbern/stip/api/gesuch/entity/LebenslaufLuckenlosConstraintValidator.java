package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LebenslaufLuckenlosConstraintValidator
    implements ConstraintValidator<LebenslaufLuckenlosConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(LebenslaufLuckenlosConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getAusbildung() == null) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        // If PIA is younger than 16 no items need to be present
        if (
            gesuchFormular.getPersonInAusbildung().getGeburtsdatum().plusYears(16)
                .isAfter(gesuchFormular.getAusbildung().getAusbildungBegin())
        ) {
            return true;
        }

        LocalDate start = gesuchFormular.getPersonInAusbildung().getGeburtsdatum()
            .plusYears(16);
        LocalDate stop = gesuchFormular.getAusbildung().getAusbildungBegin();
        List<DateRange> dateRanges = new ArrayList<>();
        gesuchFormular.getLebenslaufItems().stream().forEach(
            lebenslaufItem -> dateRanges.add(new DateRange(lebenslaufItem.getVon(), lebenslaufItem.getBis()))
        );
        // Sort the Lebenslaufsitem Daterange
        Collections.sort(dateRanges, Comparator.comparing(DateRange::getGueltigAb));

        // Check if first Lebenslaufitem is After birth
        LocalDate currentDate = start;
        if (dateRanges.isEmpty()) {
            LOG.warn("No Lebenslauf Items present");
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        if (gesuchFormular.getPersonInAusbildung().getGeburtsdatum().isAfter(dateRanges.get(0).getGueltigAb())) {
            LOG.warn("Lebenslauf Item start bevor " + currentDate);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        for (DateRange range : dateRanges) {
            if (range.getGueltigAb().isAfter(currentDate)) {
                LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + range.getGueltigAb());
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
            if (range.getGueltigBis().isAfter(stop)) {
                LOG.warn("Lebenslauf Item end after " + currentDate);
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
            currentDate = range.getGueltigBis().plusDays(1);
            if (currentDate.isAfter(stop)) {
                break;
            }
        }

        if (currentDate.isBefore(stop)) {
            LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + stop);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        return true;
    }
}
