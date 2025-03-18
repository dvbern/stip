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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.util.OverrideUtil;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
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
                copy.setCustomDokumentTyp(copyCustomDokumentTyp(original.getCustomDokumentTyp()));
            }
            copy.setStatus(original.getStatus());
            original.getDokumente().forEach(copy::addDokument);

            return copy;
        }).toList();
    }

    public CustomDokumentTyp copyCustomDokumentTyp(final CustomDokumentTyp source) {
        var copy = new CustomDokumentTyp();
        copy.setType(source.getType());
        copy.setDescription(source.getDescription());
        copy.setGesuchDokument(source.getGesuchDokument());
        return copy;
    }

    public GesuchDokument createCopy(final GesuchDokument source) {
        final var copy = new GesuchDokument();
        copyValues(source, copy);
        return copy;
    }

    public void copyValues(final GesuchDokument source, final GesuchDokument target) {
        target.setDokumentTyp(source.getDokumentTyp());
        target.setGesuchTranche(source.getGesuchTranche());
        target.setDokumentTyp(source.getDokumentTyp());
        target.setStatus(source.getStatus());
        if (source.getCustomDokumentTyp() != null) {
            target.setCustomDokumentTyp(copyCustomDokumentTyp(source.getCustomDokumentTyp()));
        }
        target.setDokumente(new ArrayList<>());
        source.getDokumente().forEach(target::addDokument);
    }

    public void doOverrideOfSet(Set<GesuchDokument> targetItems, Set<GesuchDokument> sourceItems) {
        OverrideUtil.doOverrideOfSet(
            targetItems,
            sourceItems,
            GesuchDokumentCopyUtil::copyValues,
            GesuchDokumentCopyUtil::createCopy
        );
    }

}
