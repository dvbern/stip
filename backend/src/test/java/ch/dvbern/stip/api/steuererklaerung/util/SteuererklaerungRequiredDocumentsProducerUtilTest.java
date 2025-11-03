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

package ch.dvbern.stip.api.steuererklaerung.util;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class SteuererklaerungRequiredDocumentsProducerUtilTest {
    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        formular = new GesuchFormular();
    }

    @Test
    void steuererklaerungNotInBernTest() {
        formular.setSteuererklaerung(
            Set.of(new Steuererklaerung().setSteuererklaerungInBern(false).setSteuerdatenTyp(SteuerdatenTyp.FAMILIE))
        );
        var requiredDocs = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.FAMILIE);
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE));
        assertEquals(requiredDocs.size(), 1);

        formular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung()
                    .setSteuererklaerungInBern(true)
                    .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
            )
        );
        requiredDocs = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.FAMILIE);

        assertFalse(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE));
        assertEquals(requiredDocs.size(), 0);

    }

    @Test
    void noDocRequiredIfValueLessThan1Test() {
        formular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung()
                    .setSteuererklaerungInBern(true)
                    .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                    .setErgaenzungsleistungen(0)
                    .setUnterhaltsbeitraege(0)
                    .setRenten(0)
                    .setEinnahmenBGSA(0)
                    .setAndereEinnahmen(0)
            )
        );
        final var requiredDocs = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.FAMILIE);

        assertEquals(requiredDocs.size(), 0);
    }

    @Test
    void docRequiredIfValueMoreThanZeroTest() {
        formular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung()
                    .setSteuererklaerungInBern(true)
                    .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                    .setErgaenzungsleistungen(1)
                    .setUnterhaltsbeitraege(1)
                    .setRenten(1)
                    .setEinnahmenBGSA(1)
                    .setAndereEinnahmen(1)
            )
        );
        final var requiredDocs = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.FAMILIE);
        assertEquals(requiredDocs.size(), 5);
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE));
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_FAMILIE));
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_RENTEN_FAMILIE));
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_FAMILIE));
        assertTrue(requiredDocs.contains(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE));
    }

    @Test
    void docRequiredForSeveralSteuererklaerungTest() {
        formular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung()
                    .setSteuererklaerungInBern(true)
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setErgaenzungsleistungen(0)
                    .setUnterhaltsbeitraege(0)
                    .setRenten(0)
                    .setEinnahmenBGSA(1)
                    .setAndereEinnahmen(1),
                new Steuererklaerung()
                    .setSteuererklaerungInBern(true)
                    .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setErgaenzungsleistungen(1)
                    .setUnterhaltsbeitraege(1)
                    .setRenten(0)
                    .setEinnahmenBGSA(0)
                    .setAndereEinnahmen(0)
            )
        );
        final var requiredDocsMutter = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.MUTTER);
        assertEquals(requiredDocsMutter.size(), 2);
        assertFalse(requiredDocsMutter.contains(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER));
        assertFalse(requiredDocsMutter.contains(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_MUTTER));
        assertFalse(requiredDocsMutter.contains(DokumentTyp.STEUERERKLAERUNG_RENTEN_MUTTER));
        assertTrue(requiredDocsMutter.contains(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER));
        assertTrue(requiredDocsMutter.contains(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER));

        final var requiredDocsVater = SteuererklaerungRequiredDocumentsProducerUtil
            .getRequiredDocuments(formular.getSteuererklaerung(), SteuerdatenTyp.VATER);
        assertEquals(requiredDocsVater.size(), 2);
        assertTrue(requiredDocsVater.contains(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER));
        assertTrue(requiredDocsVater.contains(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_VATER));
        assertFalse(requiredDocsVater.contains(DokumentTyp.STEUERERKLAERUNG_RENTEN_VATER));
        assertFalse(requiredDocsVater.contains(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER));
        assertFalse(requiredDocsVater.contains(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER));
    }

}
