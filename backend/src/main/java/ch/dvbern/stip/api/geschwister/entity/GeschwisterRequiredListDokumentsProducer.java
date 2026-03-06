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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class GeschwisterRequiredListDocumentsProducer implements RequiredRefDokumentsProducer {
    @Override
    public Pair<String, Set<Pair<DokumentTyp, UUID>>> getRequiredDokuments(GesuchFormular formular) {
        final var geschwisters = formular.getGeschwisters();
        if (Objects.isNull(geschwisters)) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<Pair<DokumentTyp, UUID>>();

        for (var geschwister : geschwisters) {
            if (geschwister.getAusbildungssituation() == Ausbildungssituation.IN_AUSBILDUNG) {
                requiredDocs.add(
                    Pair.of(
                        DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
                        geschwister.getEntryId()
                    )
                );
            }
        }

        return ImmutablePair.of("geschwisters", requiredDocs);
    }
}
