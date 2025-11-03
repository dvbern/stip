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

package ch.dvbern.stip.api.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class RequiredDocumentsTestUtil {
    public List<Pair<String, Set<DokumentTyp>>> getRequiredDocuments(
        final GesuchFormular formular,
        final List<RequiredDocumentsProducer> producers
    ) {
        final var requiredTypes = new ArrayList<Pair<String, Set<DokumentTyp>>>();
        for (final var producer : producers) {
            requiredTypes.add(producer.getRequiredDocuments(formular));
        }

        return requiredTypes.stream().filter(pair -> !pair.getRight().isEmpty()).toList();
    }
}
