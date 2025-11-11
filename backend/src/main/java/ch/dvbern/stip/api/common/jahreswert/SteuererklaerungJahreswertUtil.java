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

package ch.dvbern.stip.api.common.jahreswert;

import java.util.List;

import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuererklaerungJahreswertUtil {
    public void synchroniseJahresfelder(
        final Steuererklaerung source,
        final List<Steuererklaerung> targets
    ) {
        for (final var target : targets) {
            target.setErgaenzungsleistungen(source.getErgaenzungsleistungen());
            target.setEinnahmenBGSA(source.getEinnahmenBGSA());
            target.setAndereEinnahmen(source.getAndereEinnahmen());
        }
    }

    public List<Steuererklaerung> selectTargetSteuererklaerung(
        final Steuererklaerung steuererklaerung,
        final List<GesuchTranche> gesuchTranchen
    ) {
        return gesuchTranchen.stream()
            .flatMap(targetTranche -> targetTranche.getGesuchFormular().getSteuererklaerung().stream())
            .filter(
                targetSteuererklaerung -> !targetSteuererklaerung.getId().equals(steuererklaerung.getId())
                && targetSteuererklaerung.getSteuerdatenTyp().equals(steuererklaerung.getSteuerdatenTyp())
            )
            .toList();
    }
}
