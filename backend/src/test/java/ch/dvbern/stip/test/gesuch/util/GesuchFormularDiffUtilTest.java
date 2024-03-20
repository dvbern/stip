package ch.dvbern.stip.test.gesuch.util;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularDiffUtil;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GesuchFormularDiffUtilTest {

    /**
     * Testcase: PersonInAusbildung Geburtsdatum has changed
     */
    @Test
    void hasGeburtsdatumOfPersonInAusbildungChangedTest() {
        // Given
        GesuchFormular toUpdate = new GesuchFormular();
        toUpdate.setPersonInAusbildung(new PersonInAusbildung());
        toUpdate.getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2000, 1, 1));

        GesuchFormularUpdateDto update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(new PersonInAusbildungUpdateDto());
        update.getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2001, 1, 1)); //changed

        // When
        boolean actual = GesuchFormularDiffUtil.hasGeburtsdatumOfPersonInAusbildungChanged(toUpdate, update);

        // Then
        assertTrue(actual);
    }

    /**
     * Testcase: PersonInAusbildung Geburtsdatum has not changed
     */
    @Test
    void hasGeburtsdatumOfPersonInAusbildungNotChangedTest() {
        // Given
        GesuchFormular toUpdate = new GesuchFormular();
        toUpdate.setPersonInAusbildung(new PersonInAusbildung());
        toUpdate.getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2000, 1, 1));

        GesuchFormularUpdateDto update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(new PersonInAusbildungUpdateDto());
        update.getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2000, 1, 1)); // NOT changed

        // When
        boolean actual = GesuchFormularDiffUtil.hasGeburtsdatumOfPersonInAusbildungChanged(toUpdate, update);

        // Then
        assertFalse(actual);
    }


    // Test case for when the "Eigener Haushalt" has been updated in the GesuchFormularUpdateDto object.
    @Test
    void whenUpdateToEigenerHaushaltIsMade_shouldReturnTrue() {
        // Initialize the GesuchFormularUpdateDto object.
        GesuchFormularUpdateDto gesuchFormularUpdateDto = new GesuchFormularUpdateDto();
        gesuchFormularUpdateDto.setPersonInAusbildung(new PersonInAusbildungUpdateDto());
        gesuchFormularUpdateDto.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

        // Test the 'isUpdateToEigenerHaushalt' method.
        boolean isUpdated = GesuchFormularDiffUtil.isUpdateToEigenerHaushalt(gesuchFormularUpdateDto);
        assertTrue(isUpdated, "Expected true when 'Wohnsitz' is set to 'EIGENER_HAUSHALT' but it returned false.");
    }

    // Test case for when the "Eigener Haushalt" has not been updated in the GesuchFormularUpdateDto object.
    @Test
    void whenUpdateToEigenerHaushaltIsNotMade_shouldReturnFalse() {
        // Initialize the GesuchFormularUpdateDto object.
        GesuchFormularUpdateDto gesuchFormularUpdateDto = new GesuchFormularUpdateDto();
        gesuchFormularUpdateDto.setPersonInAusbildung(new PersonInAusbildungUpdateDto());
        gesuchFormularUpdateDto.getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);

        // Test the 'isUpdateToEigenerHaushalt' method.
        boolean isUpdated = GesuchFormularDiffUtil.isUpdateToEigenerHaushalt(gesuchFormularUpdateDto);
        assertFalse(isUpdated, "Expected false when 'Wohnsitz' is not set to 'EIGENER_HAUSHALT' but it returned true.");
    }

    @Test
    void hasGerichtlicheAlimenteregelungChangedTest() {
        // Create original GesuchFormular
        GesuchFormular original = new GesuchFormular();
        original.setFamiliensituation(new Familiensituation());
        original.getFamiliensituation().setGerichtlicheAlimentenregelung(true);

        // Create updated GesuchFormular
        GesuchFormularUpdateDto update = new GesuchFormularUpdateDto();
        update.setFamiliensituation(new FamiliensituationUpdateDto());
        update.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        // Test hasGerichtlicheAlimenteregelungChanged method
        assertTrue(GesuchFormularDiffUtil.hasGerichtlicheAlimenteregelungChanged(original, update));

        // Test when no changes in GerichtlicheAlimentenregelung
        update.getFamiliensituation().setGerichtlicheAlimentenregelung(true);
        assertFalse(GesuchFormularDiffUtil.hasGerichtlicheAlimenteregelungChanged(original, update));

        // Test when GerichtlicheAlimentenregelung not set in update
        update.getFamiliensituation().setGerichtlicheAlimentenregelung(null);
        assertTrue(GesuchFormularDiffUtil.hasGerichtlicheAlimenteregelungChanged(original, update));
    }
}
