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

package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamiliensituationRequiredDocumentsProducerTest {
    private FamiliensituationRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new FamiliensituationRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfElternteilUnbekannterAufenthaltsort() {
        formular.setFamiliensituation(
            new Familiensituation()
                .setVaterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER
        );

        formular.setFamiliensituation(
            new Familiensituation()
                .setMutterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER
        );
    }

    @Test
    void requiresIfElternteilUngekannt() {

        formular.setFamiliensituation(
            new Familiensituation()
                .setVaterUnbekanntGrund(ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_GEBURTSSCHEIN
        );

        formular.setFamiliensituation(
            new Familiensituation()
                .setMutterUnbekanntGrund(ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_GEBURTSSCHEIN
        );
    }

    @Test
    void requiresIfAlimentenregelung() {
        formular.setFamiliensituation(
            new Familiensituation()
                .setGerichtlicheAlimentenregelung(true)
        );

        final var requiredDocuments = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocuments, DokumentTyp.FAMILIENSITUATION_TRENNUNGSKONVENTION);
    }
}
