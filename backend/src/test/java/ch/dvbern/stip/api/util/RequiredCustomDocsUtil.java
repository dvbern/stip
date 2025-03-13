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

package ch.dvbern.stip.api.util;

import java.util.Set;

import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@UtilityClass
public class RequiredCustomDocsUtil {
    public void requiresOneAndType(
        final Pair<String, Set<CustomDokumentTyp>> requiredDocs,
        final CustomDokumentTyp dokTyp
    ) {
        requiresOneAndType(requiredDocs.getRight(), dokTyp);
    }

    public void requiresOneAndType(final Set<CustomDokumentTyp> requiredDocs, final CustomDokumentTyp dokTyp) {
        assertCount(requiredDocs, 1);
        assertType(requiredDocs, dokTyp);
    }

    public void assertCount(final Set<CustomDokumentTyp> requiredDocs, final int count) {
        assertThat(requiredDocs.toString(), requiredDocs.size(), is(count));
    }

    public void assertType(final Set<CustomDokumentTyp> requiredDocs, final CustomDokumentTyp dokTyp) {
        assertThat(requiredDocs.toString(), requiredDocs.contains(dokTyp), is(true));
    }
}
