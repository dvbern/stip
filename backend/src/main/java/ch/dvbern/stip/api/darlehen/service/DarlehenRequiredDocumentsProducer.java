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

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class DarlehenRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        if (
            Objects.isNull(formular.getDarlehen()) || Boolean.FALSE.equals(formular.getDarlehen().getWillDarlehen())
        ) {
            return ImmutablePair.of("", Set.of());
        }
        final var darlehen = formular.getDarlehen();
        final var requiredDocs = new HashSet<DokumentTyp>();

        requiredDocs.add(DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG);

        if (Boolean.TRUE.equals(darlehen.getGrundNichtBerechtigt())) {
            requiredDocs.add(DokumentTyp.DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN);
        }
        if (Boolean.TRUE.equals(darlehen.getGrundHoheGebuehren())) {
            requiredDocs.add(DokumentTyp.DARLEHEN_KOPIE_SCHULGELDRECHNUNG);
        }
        if (
            Boolean.TRUE.equals(darlehen.getGrundAnschaffungenFuerAusbildung())
        ) {
            requiredDocs.add(DokumentTyp.DARLEHEN_BELEGE_ANSCHAFFUNGEN);
        }

        return ImmutablePair.of("darlehen", requiredDocs);
    }
}
