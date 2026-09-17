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

package ch.dvbern.stip.api.unterschriftenblatt.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.unterschriftenblatt.entity.Unterschriftenblatt;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UnterschriftenblattUtil {
    public Set<SteuerdatenTyp> getGsVisibleSteuerdatenTyps(final Gesuch gesuch) {
        final Set<SteuerdatenTyp> unterschriftenblattSteuerdatenTyps = gesuch.getUnterschriftenblaetter()
            .stream()
            .map(Unterschriftenblatt::getDokumentTyp)
            .map(UnterschriftenblattUtil::mapToVisibleSteuerdatenTyps)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        final Set<SteuerdatenTyp> hiddenElternTyps = gesuch.getGesuchTranchen()
            .stream()
            .map(GesuchTranche::getGesuchFormular)
            .flatMap(
                (gesuchFormular -> gesuchFormular.getVersteckteEltern().stream())
            )
            .map(ElternTyp::getSteuerdatenTyp)
            .collect(Collectors.toSet());

        return unterschriftenblattSteuerdatenTyps.stream()
            .filter(steuerdatenTyp -> !hiddenElternTyps.contains(steuerdatenTyp))
            .collect(Collectors.toSet());
    }

    private List<SteuerdatenTyp> mapToVisibleSteuerdatenTyps(
        final UnterschriftenblattDokumentTyp unterschriftenblattDokumentTyp
    ) {
        return switch (unterschriftenblattDokumentTyp) {
            case MUTTER -> List.of(SteuerdatenTyp.MUTTER);
            case VATER -> List.of(SteuerdatenTyp.VATER);
            case GEMEINSAM -> List.of(SteuerdatenTyp.FAMILIE, SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER);
        };
    }
}
