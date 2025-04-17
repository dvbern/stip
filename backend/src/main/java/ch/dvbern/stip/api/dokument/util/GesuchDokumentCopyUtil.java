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

import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
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
            if (original.getCustomDokumentTyp() != null) {
                copy.setCustomDokumentTyp(copyCustomDokumentTyp(original.getCustomDokumentTyp(), copy));
            }
            copy.setStatus(original.getStatus());
            original.getDokumente().forEach(copy::addDokument);

            return copy;
        }).toList();
    }

    public CustomDokumentTyp copyCustomDokumentTyp(
        final CustomDokumentTyp source,
        final GesuchDokument targetGesuchDokument
    ) {
        var copy = new CustomDokumentTyp();
        copy.setType(source.getType());
        copy.setDescription(source.getDescription());
        copy.setGesuchDokument(targetGesuchDokument);
        return copy;
    }

    public GesuchDokument createCopy(final GesuchDokument source, final GesuchTranche targetTranche) {
        final var copy = new GesuchDokument();
        copyValues(source, copy, targetTranche);
        return copy;
    }

    public void copyValues(final Dokument source, final Dokument target) {
        target.setFilepath(source.getFilepath());
        target.setFilename(source.getFilename());
        target.setFilesize(source.getFilesize());
        target.setObjectId(source.getObjectId());
    }

    public void copyValues(
        final GesuchDokument source,
        final GesuchDokument target,
        final GesuchTranche targetTranche
    ) {
        target.setDokumentTyp(source.getDokumentTyp());
        target.setGesuchTranche(targetTranche);
        target.setDokumentTyp(source.getDokumentTyp());
        target.setStatus(source.getStatus());
        if (source.getCustomDokumentTyp() != null) {
            target.setCustomDokumentTyp(copyCustomDokumentTyp(source.getCustomDokumentTyp(), target));
        }
    }
}
