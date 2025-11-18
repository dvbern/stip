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

package ch.dvbern.stip.api.kind.entity;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class KindRequiredDocumentsProducerTest {
    private KindRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new KindRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfUnterhaltsbeitraege() {
        formular.setKinds(
            Set.of(
                new Kind()
                    .setUnterhaltsbeitraege(1)
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_ALIMENTENVERORDUNG);
    }

    @Test
    void requiresIfKinderUndAusbildungszulagen() {
        formular.setKinds(
            Set.of(
                new Kind()
                    .setKinderUndAusbildungszulagen(1)
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_UND_AUSBILDUNGSZULAGEN);
    }

    @Test
    void requiresIfRenten() {
        formular.setKinds(
            Set.of(
                new Kind()
                    .setRenten(1)
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_RENTEN);
    }

    @Test
    void requiresIfErgaenzungsleistungen() {
        formular.setKinds(
            Set.of(
                new Kind()
                    .setErgaenzungsleistungen(1)
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_ERGAENZUNGSLEISTUNGEN);
    }

    @Test
    void requiresIf() {
        formular.setKinds(
            Set.of(
                new Kind()
                    .setAndereEinnahmen(1)
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_ANDERE_EINNAHMEN);
    }
}
