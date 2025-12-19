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

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchDokumentKommentarCopyUtil {
    public GesuchDokumentKommentar createCopy(
        final GesuchDokumentKommentar source,
        final GesuchDokument destinationGesuchDokument
    ) {
        final var copy = new GesuchDokumentKommentar();

        copy.setKommentar(source.getKommentar());
        copy.setGesuchDokumentStatus(source.getGesuchDokumentStatus());
        copy.setAutor(source.getAutor());
        copy.setTimestampErstellt(source.getTimestampErstellt());
        copy.setTimestampMutiert(source.getTimestampMutiert());
        destinationGesuchDokument.addGesuchKommentar(copy);

        return copy;
    }
}
