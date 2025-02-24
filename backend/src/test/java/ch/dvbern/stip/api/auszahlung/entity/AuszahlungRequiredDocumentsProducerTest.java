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

package ch.dvbern.stip.api.auszahlung.entity;

import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AuszahlungRequiredDocumentsProducerTest {
    private AuszahlungRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new AuszahlungRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfKontoinhaberSozialdienst() {
        formular.setAuszahlung(
            new Auszahlung()
                .setKontoinhaber(Kontoinhaber.SOZIALDIENST_INSTITUTION)
        );

        final var requiredDocsList = producer.getRequiredDocuments(formular);
        assertThat(requiredDocsList.size(), is(1));
        final var requiredDocs = requiredDocsList.get(0);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }

    @Test
    void requiresIfKontoinhaberAndere() {
        formular.setAuszahlung(
            new Auszahlung()
                .setKontoinhaber(Kontoinhaber.ANDERE)
        );

        final var requiredDocsList = producer.getRequiredDocuments(formular);
        assertThat(requiredDocsList.size(), is(1));
        final var requiredDocs = requiredDocsList.get(0);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }
}
