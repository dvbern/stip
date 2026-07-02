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

package ch.dvbern.stip.api.dokument.entity;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class DokumentsRequiredDokumentProducerTest {

    @Test
    void requiresIfPiaLedigWithChildren() {
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var formular = gesuch.getLatestGesuchTranche().getGesuchFormular();
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setZivilstand(Zivilstand.LEDIG)
        )
            .setKinds(
                Set.of(
                    new Kind()
                )
            );

        final var requiredDocs = new DokumentsRequiredDokumentProducer().getRequiredDokuments(formular, true);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION);
    }
}
