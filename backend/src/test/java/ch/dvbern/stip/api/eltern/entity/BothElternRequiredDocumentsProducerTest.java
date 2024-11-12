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

package ch.dvbern.stip.api.eltern.entity;

import java.util.ArrayList;
import java.util.Set;

import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

@QuarkusTest
class BothElternRequiredDocumentsProducerTest {

    @Inject
    RequiredDokumentService requiredDokumentService;

    @Test
    @Description(
        "DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER should only appear once" +
        "in required documents"
    )
    void vaterWohnkostenRequired() {
        GesuchTranche tranche = new GesuchTranche();
        GesuchFormular gesuchFormular = new GesuchFormular();
        gesuchFormular.setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(true));
        gesuchFormular.setElterns(
            Set.of(
                new Eltern().setElternTyp(ElternTyp.VATER)
                    .setWohnkosten(1)
                    .setSozialhilfebeitraege(false),
                new Eltern().setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(1)
                    .setSozialhilfebeitraege(false)
            )
        );
        tranche.setGesuchFormular(gesuchFormular);
        tranche.setGesuchDokuments(new ArrayList<>());
        gesuchFormular.setTranche(tranche);
        final var requiredDocuments = requiredDokumentService
            .getRequiredDokumentsForGesuchFormular(gesuchFormular);
        assertEquals(1, requiredDocuments.size());
    }
}
