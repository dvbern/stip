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

package ch.dvbern.stip.api.gesuchformular.util;

import java.util.List;

import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteChangedDocumentsUtilTest {
    @Test
    void getDocumentsForPartnerDoesNotFailWithNull() {
        final var newPartner = new PartnerUpdateDto();
        final var oldParnter = new Partner();

        // Both null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForPartner(null, null));

        // Old null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForPartner(newPartner, null));

        // New null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForPartner(null, oldParnter));

        // Neither is null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForPartner(newPartner, oldParnter));
    }

    @Test
    void getDocumentsForDarlehenDoesNotFailWithNull() {
        final var newDarlehen = new DarlehenDto();
        final var oldDarlehen = new Darlehen();

        // Both null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForDarlehen(null, null));

        // Old null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForDarlehen(newDarlehen, null));

        // New null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForDarlehen(null, oldDarlehen));

        // Neither is null
        assertDoesNotThrow(() -> DeleteChangedDocumentsUtil.getDocumentsToDeleteForDarlehen(newDarlehen, oldDarlehen));
    }

    @Test
    void deleteChangedDocumentsDoesNothingIfTrancheIsUpdated() {
        // Arrange
        final var gesuchDokumentService = Mockito.mock(GesuchDokumentService.class);
        Mockito.doNothing().when(gesuchDokumentService).deleteDokumenteForTranche(Mockito.any(), Mockito.any());

        final var oldFormular = new GesuchFormular().setTranche(new GesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE));

        // Act
        DeleteChangedDocumentsUtil.deleteChangedDocuments(gesuchDokumentService, null, oldFormular);

        // Assert
        Mockito.verify(gesuchDokumentService, Mockito.never()).deleteDokumenteForTranche(Mockito.any(), Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(GetDocumentsForPersonInAusbildungArgumentsProvider.class)
    void getDocumentsForPersonInAusbildungTest(
        final PersonInAusbildungUpdateDto newPia,
        final PersonInAusbildung oldPia,
        final DokumentTyp expected
    ) {
        // Act
        final var actual = DeleteChangedDocumentsUtil.getDocumentsToDeleteForPersonInAusbildung(newPia, oldPia);

        // Assert
        assertExpectedResult(actual, expected);
    }

    @ParameterizedTest
    @ArgumentsSource(GetDocumentsForPartnerArgumentsProvider.class)
    void getDocumentsForPartnerTest(
        final PartnerUpdateDto newPartner,
        final Partner oldPartner,
        final DokumentTyp expected
    ) {
        // Act
        final var actual = DeleteChangedDocumentsUtil.getDocumentsToDeleteForPartner(newPartner, oldPartner);

        // Assert
        assertExpectedResult(actual, expected);
    }

    @ParameterizedTest
    @ArgumentsSource(GetDocumentsForEinnahmenKostenArgumentsProvider.class)
    void getDocumentsForEinnahmenKostenTest(
        final EinnahmenKostenUpdateDto newEk,
        final EinnahmenKosten oldEk,
        final DokumentTyp expected
    ) {
        // Act
        final var actual = DeleteChangedDocumentsUtil.getDocumentsToDeleteForEinnahmenKosten(newEk, oldEk);

        // Assert
        assertExpectedResult(actual, expected);
    }

    @ParameterizedTest
    @ArgumentsSource(GetDocumentsForDarlehenArgumentsProvider.class)
    void getDocumentsForDarlehenTest(
        final DarlehenDto newDarlehen,
        final Darlehen oldDarlehen,
        final DokumentTyp expected
    ) {
        // Act
        final var actual = DeleteChangedDocumentsUtil.getDocumentsToDeleteForDarlehen(newDarlehen, oldDarlehen);

        // Assert
        assertExpectedResult(actual, expected);
    }

    @ParameterizedTest
    @ArgumentsSource(GetDocumentsForElternArgumentsProvider.class)
    void getDocumentsForElternTest(
        final ElternUpdateDto newEltern,
        final Eltern oldEltern,
        final DokumentTyp expected
    ) {
        // Act
        final var actual = DeleteChangedDocumentsUtil.getDocumentsToDeleteForEltern(newEltern, oldEltern);

        // Assert
        if (expected == null) {
            assertEquals(0, actual.size());
        } else {
            assertThat(actual.size(), Matchers.greaterThanOrEqualTo(1));
            assertThat(actual.contains(expected), Matchers.is(true));
        }
    }

    void assertExpectedResult(final List<DokumentTyp> actual, final DokumentTyp expected) {
        if (expected == null) {
            assertEquals(0, actual.size());
        } else {
            assertEquals(1, actual.size());
            final var found = actual.getFirst();
            assertEquals(expected, found);
        }
    }
}
