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

package ch.dvbern.stip.api.dokument.util;

import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchDokumentCopyUtil {
    /**
     * Copies the given {@link GesuchDokument}e and sets the {@link GesuchDokument#dokumente} to the same reference
     * as the dokumente in the respective entry in the source
     */
    public List<GesuchDokument> copyGesuchDokumenteWithDokumentReferences(
        final GesuchTranche targetTranche,
        final List<GesuchDokument> toCopy
    ) {
        return toCopy.stream().map(original -> {
            final var copy = new GesuchDokument();

            copy.setGesuchTranche(targetTranche);
            copy.setDokumentTyp(original.getDokumentTyp());
            copy.setCustomDokumentTyp(original.getCustomDokumentTyp());
            copy.setStatus(original.getStatus());
            original.getDokumente().forEach(copy::addDokument);

            return copy;
        }).toList();
    }
}
