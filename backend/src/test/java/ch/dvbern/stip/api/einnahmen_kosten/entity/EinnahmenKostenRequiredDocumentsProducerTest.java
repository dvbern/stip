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

package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EinnahmenKostenRequiredDocumentsProducerTest {
    private EinnahmenKostenRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new EinnahmenKostenRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfVerdienstRealisiert() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setVerdienstRealisiert(true)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_VERDIENST);
    }

    @Test
    void requiresIfNettoerwerbseinkommen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_LOHNABRECHNUNG);
    }

    @Test
    void requiresIfWohnkosten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setWohnkosten(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_MIETVERTRAG);
    }

    @Test
    void requiresIfFahrkosten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setFahrkosten(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_OV_ABONNEMENT
        );
    }

    @Test
    void requiresIfEoLeistungen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setEoLeistungen(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
        );
    }

    @Test
    void requiresIfRenten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setRenten(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN
        );
    }

    @Test
    void requiresIfBeitraege() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setBeitraege(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION
        );
    }

    @Test
    void requiresIfZulagen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setZulagen(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_KINDERZULAGEN
        );
    }

    @Test
    void requiresIfAlimente() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setAlimente(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_BELEG_ALIMENTE);
    }

    @Test
    void requiresIfVermoegen() {
        formular.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(1000));
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_VERMOEGEN);

    }
}
