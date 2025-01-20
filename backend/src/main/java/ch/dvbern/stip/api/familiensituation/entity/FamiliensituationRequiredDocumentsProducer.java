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

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class FamiliensituationRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var famsit = formular.getFamiliensituation();
        if (famsit == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();
        if (famsit.getMutterUnbekanntGrund() == ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER);
        }

        if (famsit.getVaterUnbekanntGrund() == ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER);
        }

        if (Boolean.TRUE.equals(famsit.getGerichtlicheAlimentenregelung())) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_TRENNUNGSKONVENTION);
        }

        if (famsit.getVaterUnbekanntGrund() == ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_GEBURTSSCHEIN);
        }

        return ImmutablePair.of("familiensituation", requiredDocs);
    }

    @Override
    public Pair<String, Set<CustomDokumentTyp>> getRequiredDocuments(GesuchTranche tranche) {
        return ImmutablePair.of("", Set.of());
    }
}
