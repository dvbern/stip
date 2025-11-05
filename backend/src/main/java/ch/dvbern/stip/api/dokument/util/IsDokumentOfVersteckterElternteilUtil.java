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

import java.util.EnumSet;
import java.util.Set;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IsDokumentOfVersteckterElternteilUtil {
    private static final EnumSet<DokumentTyp> POTENTIALLY_HIDDEN_VATER_DOKUMENTE = EnumSet.of(
        DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER,
        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER,
        DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER,
        DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_VATER,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER,
        DokumentTyp.STEUERERKLAERUNG_RENTEN_VATER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER
    );

    private static final EnumSet<DokumentTyp> POTENTIALLY_HIDDEN_MUTTER_DOKUMENTE = EnumSet.of(
        DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER,
        DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER,
        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_RENTEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER
    );

    private static EnumSet<DokumentTyp> getDokumenteFor(final ElternTyp elternTyp) {
        return switch (elternTyp) {
            case MUTTER -> POTENTIALLY_HIDDEN_MUTTER_DOKUMENTE;
            case VATER -> POTENTIALLY_HIDDEN_VATER_DOKUMENTE;
        };
    }

    public boolean isVerstecktesDokument(final Set<ElternTyp> versteckteEltern, final GesuchDokument gesuchDokument) {
        for (final var verstecktesElternteil : versteckteEltern) {
            final var dokumente = getDokumenteFor(verstecktesElternteil);
            if (dokumente.contains(gesuchDokument.getDokumentTyp())) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
