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

package ch.dvbern.stip.api.steuererklaerung.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuererklaerungRequiredDocumentsProducerUtil {
    public Set<DokumentTyp> getRequiredDocuments(
        Set<Steuererklaerung> steuererklarungen,
        SteuerdatenTyp steuerdatenTyp
    ) {
        final var requiredDocs = new HashSet<DokumentTyp>();

        if (steuererklarungen.stream().map(Steuererklaerung::getSteuerdatenTyp).toList().contains(steuerdatenTyp)) {
            final var steuererklarung = steuererklarungen.stream()
                .filter(steuererklaerung -> steuererklaerung.getSteuerdatenTyp() == steuerdatenTyp)
                .findFirst()
                .orElseThrow();

            if (Boolean.FALSE.equals(steuererklarung.getSteuererklaerungInBern())) {
                requiredDocs.add(switch (steuerdatenTyp) {
                    case FAMILIE -> DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE;
                    case VATER -> DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER;
                    case MUTTER -> DokumentTyp.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER;
                });
            }

            if (
                Objects.nonNull(steuererklarung.getErgaenzungsleistungen())
                && steuererklarung.getErgaenzungsleistungen() > 0
            ) {
                requiredDocs.add(switch (steuerdatenTyp) {
                    case FAMILIE -> DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER; // todo kstip-2780: how to handle?
                                                                                     // maybe also rename enum?
                    case VATER -> DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER;
                    case MUTTER -> DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER;
                });
            }
        }

        return requiredDocs;
    }
}
