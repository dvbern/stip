/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.test.gesuch.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularDiffUtil;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.junit.jupiter.api.Test;

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
        update.getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2001, 1, 1)); // changed

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

        // Test the 'isUpdateToNotEigenerHaushalt' method.
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

        // Test the 'isUpdateToNotEigenerHaushalt' method.
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

    @Test
    void hasWerZahltAlimenteChangedTest() {
        final var original = new GesuchFormular()
            .setFamiliensituation(
                new Familiensituation()
                    .setWerZahltAlimente(Elternschaftsteilung.VATER)
            );

        final var updated = new GesuchFormularUpdateDto();
        final var updatedFamsit = new FamiliensituationUpdateDto();
        updatedFamsit.setWerZahltAlimente(Elternschaftsteilung.MUTTER);
        updated.setFamiliensituation(updatedFamsit);

        assertTrue(GesuchFormularDiffUtil.hasWerZahltAlimenteChanged(updated, original));

        updatedFamsit.setWerZahltAlimente(Elternschaftsteilung.VATER);
        assertFalse(GesuchFormularDiffUtil.hasWerZahltAlimenteChanged(updated, original));

        updated.setFamiliensituation(null);
        assertTrue(GesuchFormularDiffUtil.hasWerZahltAlimenteChanged(updated, original));
    }

    @Test
    void hasElternteilVerstorbenOrUnbekanntChangedTest() {
        final var original = new GesuchFormular()
            .setFamiliensituation(
                new Familiensituation()
                    .setElternteilUnbekanntVerstorben(false)
            );

        final var updated = new GesuchFormularUpdateDto();
        final var updatedFamsit = new FamiliensituationUpdateDto();
        updatedFamsit.setElternteilUnbekanntVerstorben(true);
        updated.setFamiliensituation(updatedFamsit);

        assertTrue(GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(updated, original));

        updatedFamsit.setElternteilUnbekanntVerstorben(false);
        assertFalse(GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(updated, original));

        updated.setFamiliensituation(null);
        assertTrue(GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(updated, original));
    }

    @Test
    void hasWohnsitzChangedTest() {
        final var originalPia = new PersonInAusbildung();
        originalPia.setWohnsitz(Wohnsitz.FAMILIE);
        final var original = new GesuchFormular().setPersonInAusbildung(originalPia);

        final var updated = new GesuchFormularUpdateDto();
        final var updatedPia = new PersonInAusbildungUpdateDto();
        updatedPia.setWohnsitz(Wohnsitz.MUTTER_VATER);
        updated.setPersonInAusbildung(updatedPia);

        assertTrue(GesuchFormularDiffUtil.hasWohnsitzChanged(updated, original));

        updatedPia.setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(GesuchFormularDiffUtil.hasWohnsitzChanged(updated, original));

        updated.setPersonInAusbildung(null);
        assertTrue(GesuchFormularDiffUtil.hasWohnsitzChanged(updated, original));
    }

    @Test
    void hasKinderChangedTest() {
        final var original = new GesuchFormular().setKinds(Set.of(new Kind()));

        var updateKinds = List.of(new KindUpdateDto());
        final var update = new GesuchFormularUpdateDto();
        update.setKinds(updateKinds);

        assertFalse(GesuchFormularDiffUtil.hasKinderChanged(update, original));

        updateKinds = List.of();
        update.setKinds(updateKinds);

        assertTrue(GesuchFormularDiffUtil.hasKinderChanged(update, original));

        update.setKinds(null);
        assertFalse(GesuchFormularDiffUtil.hasKinderChanged(update, original));
    }
}
