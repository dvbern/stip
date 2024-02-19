package ch.dvbern.stip.api.gesuch.entity;

import java.util.List;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LebenslaufAusbildungUeberschneidenConstraintValidator
    implements ConstraintValidator<LebenslaufAusbildungUeberschneidenConstraint, GesuchFormular> {
    private static boolean isOverlapping(LebenslaufItem a, LebenslaufItem b) {
        return !a.getBis().isBefore(b.getVon()) && !b.getBis().isBefore(a.getVon());
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {

        List<LebenslaufItem> lebenslaufItemList = gesuchFormular.getLebenslaufItems()
            .stream()
            .filter(lebenslaufItem -> lebenslaufItem.getBildungsart() != null)
            .collect(
                Collectors.toList());

        int n = lebenslaufItemList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isOverlapping(lebenslaufItemList.get(i), lebenslaufItemList.get(j))) {
                    return false; // Overlapping ranges found
                }
            }
        }
        return true;
    }
}
