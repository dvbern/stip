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

package ch.dvbern.stip.api.geschwister.entity;

import java.util.HashSet;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeschwisterRequiredDocumentsProducerTest {
    private GeschwisterRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new GeschwisterRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfGeschwisterInAusbildung() {
        formular.setGeschwisters(new HashSet<>() {
            {
                add(new Geschwister().setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG));
            }
        });

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
    }
}
