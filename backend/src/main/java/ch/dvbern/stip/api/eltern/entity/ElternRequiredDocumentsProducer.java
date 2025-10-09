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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.steuerdaten.util.RequiredDocumentsProducerUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ElternRequiredDocumentsProducer {
    private static final Map<ElternTyp, DokumentTyp> SOZIALHILFEBUDGET_MAP = Map.of(
        ElternTyp.MUTTER,
        DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER,
        ElternTyp.VATER,
        DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> WOHNKOSTEN_MAP = Map.of(
        ElternTyp.MUTTER,
        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
        ElternTyp.VATER,
        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
    );

    public Set<DokumentTyp> getForElternteil(final Eltern elternteil, final Familiensituation familiensituation) {
        if (elternteil == null) {
            return Set.of();
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        if (elternteil.isSozialhilfebeitraege()) {
            requiredDocs.add(SOZIALHILFEBUDGET_MAP.get(elternteil.getElternTyp()));
        }

        if (RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getWohnkosten())) {
            if (familiensituation != null && familiensituation.getElternVerheiratetZusammen()) {
                requiredDocs.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
            } else {
                requiredDocs.add(WOHNKOSTEN_MAP.get(elternteil.getElternTyp()));
            }
        }

        return requiredDocs;
    }
}
