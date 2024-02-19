package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.LebenslaufAusbildungUeberschneidenConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LebenslaufAusbildungUeberschneidenConstraintValidatorTest {

    @Test
    void isValidTest() {
        LebenslaufAusbildungUeberschneidenConstraintValidator lebenslaufAusbildungUeberschneidenConstraintValidator =
            new LebenslaufAusbildungUeberschneidenConstraintValidator();
        GesuchFormular gesuchFormular = new GesuchFormular();
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(createAusibldungLebenslaufItemWithDate(
            LocalDate.of(2000, 1, 1),
            LocalDate.of(2001, 7, 30)
        ));
        lebenslaufItemSet.add(createAusibldungLebenslaufItemWithDate(
            LocalDate.of(2001, 7, 31),
            LocalDate.of(2002, 7, 31)
        ));
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(lebenslaufAusbildungUeberschneidenConstraintValidator.isValid(gesuchFormular, null)).isTrue();
        lebenslaufItemSet.add(createAusibldungLebenslaufItemWithDate(
            LocalDate.of(2002, 7, 31),
            LocalDate.of(2003, 7, 31)
        ));
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(lebenslaufAusbildungUeberschneidenConstraintValidator.isValid(
            gesuchFormular,
            null
        )).isFalse();
    }

    private LebenslaufItem createAusibldungLebenslaufItemWithDate(LocalDate von, LocalDate bis) {
        LebenslaufItem lebenslaufItem = new LebenslaufItem();
        lebenslaufItem.setVon(von);
        lebenslaufItem.setBis(bis);
        lebenslaufItem.setBildungsart(LebenslaufAusbildungsArt.BERUFSVORBEREITENDES_SCHULJAHR);
        return lebenslaufItem;
    }
}
