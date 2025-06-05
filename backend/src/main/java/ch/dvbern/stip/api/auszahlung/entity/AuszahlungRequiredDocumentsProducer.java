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

import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class AuszahlungRequiredDocumentsProducer implements RequiredDocumentsProducer {
    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        // todo: move out of gesuchformular!!! Documents still required in future?
        // delete / alter?
        return ImmutablePair.of("", Set.of());

        // todo: use helper method
        // final var auszahlung = formular.getTranche().getGesuch().getAusbildung().getFall().getAuszahlung();
        // if (auszahlung == null) {
        // return ImmutablePair.of("", Set.of());
        // }
        //
        // final var requiredDocs = new HashSet<DokumentTyp>();
        // if (
        // auszahlung.isAuszahlungAnSozialdienst()
        // // todo: specify
        // // auszahlung.getKontoinhaber() == Kontoinhaber.SOZIALDIENST_INSTITUTION ||
        // // auszahlung.getKontoinhaber() == Kontoinhaber.ANDERE
        // ) {
        // requiredDocs.add(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
        // }
        //
        // return ImmutablePair.of("auszahlung", requiredDocs);
    }
}
