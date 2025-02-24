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

package ch.dvbern.stip.api.darlehen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DarlehenRequiredDocumentsProducerTest {
    private DarlehenRequiredDocumentsProducer producer;
    private GesuchFormular formular;
    Darlehen darlehen;

    @BeforeEach
    void setUp() {
        darlehen = new Darlehen();
        darlehen.setWillDarlehen(true);
        darlehen.setGrundAnschaffungenFuerAusbildung(false);
        darlehen.setGrundNichtBerechtigt(false);
        darlehen.setGrundHoheGebuehren(false);
        formular = new GesuchFormular();
        formular.setDarlehen(darlehen);
        producer = new DarlehenRequiredDocumentsProducer();
    }

    @Test
    void requiresAlwaysBetreibungsregisterauszug() {
        formular.setDarlehen(darlehen);
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG
        );
    }

    @Test
    void requiresIfKeineStipendienberechtigungKostenEltern() {
        darlehen.setGrundNichtBerechtigt(true);
        formular.setDarlehen(darlehen);
        RequiredDocsUtil.requiresFirstCountAndTypes(
            2,
            getRequiredDocuments(formular),
            DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG,
            DokumentTyp.DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN
        );

    }

    @Test
    void requiresIfZuHoheSchulgebühren() {
        darlehen.setGrundHoheGebuehren(true);
        formular.setDarlehen(darlehen);
        RequiredDocsUtil.requiresFirstCountAndTypes(
            2,
            getRequiredDocuments(formular),
            DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG,
            DokumentTyp.DARLEHEN_KOPIE_SCHULGELDRECHNUNG
        );
    }

    @Test
    void requiresIfAnschaffungenSchule() {
        darlehen.setGrundAnschaffungenFuerAusbildung(true);
        formular.setDarlehen(darlehen);
        RequiredDocsUtil.requiresFirstCountAndTypes(
            2,
            getRequiredDocuments(formular),
            DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG,
            DokumentTyp.DARLEHEN_BELEGE_ANSCHAFFUNGEN
        );
    }

    @Test
    void requiresNoneIfNotWillDarlehen() {
        darlehen.setWillDarlehen(false);
        formular.setDarlehen(darlehen);

        Assertions.assertEquals(0, producer.getRequiredDocuments(formular).getRight().size());
    }

    List<Pair<String, Set<DokumentTyp>>> getRequiredDocuments(final GesuchFormular formular) {
        final var requiredTypes = new ArrayList<Pair<String, Set<DokumentTyp>>>();
        requiredTypes.add(producer.getRequiredDocuments(formular));
        return requiredTypes.stream().filter(pair -> !pair.getRight().isEmpty()).toList();
    }
}
