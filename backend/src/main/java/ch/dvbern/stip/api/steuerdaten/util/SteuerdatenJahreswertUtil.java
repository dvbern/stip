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

package ch.dvbern.stip.api.steuerdaten.util;

import java.util.List;

import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenJahreswertUtil {
    public void synchroniseJahresfelder(
        final Steuerdaten source,
        final List<Steuerdaten> targets
    ) {
        if (source == null) {
            return;
        }

        for (final var target : targets) {
            target.setTotalEinkuenfte(source.getTotalEinkuenfte());
            target.setEigenmietwert(source.getEigenmietwert());
            target.setVermoegen(source.getVermoegen());
            target.setSteuernKantonGemeinde(source.getSteuernKantonGemeinde());
            target.setSteuernBund(source.getSteuernBund());
            target.setFahrkosten(source.getFahrkosten());
            target.setFahrkostenPartner(source.getFahrkostenPartner());
            target.setVerpflegung(source.getVerpflegung());
            target.setVerpflegungPartner(source.getVerpflegungPartner());
            target.setSteuerjahr(source.getSteuerjahr());
            target.setVeranlagungsStatus(source.getVeranlagungsStatus());
        }
    }

    public List<Steuerdaten> selectTargetSteuerdaten(
        Steuerdaten steuerdaten,
        List<GesuchTranche> gesuchTranchen
    ) {
        return gesuchTranchen.stream()
            .flatMap(targetTranche -> targetTranche.getGesuchFormular().getSteuerdaten().stream())
            .filter(
                targetSteuerdaten -> !targetSteuerdaten.getId().equals(steuerdaten.getId())
                && targetSteuerdaten.getSteuerdatenTyp().equals(steuerdaten.getSteuerdatenTyp())
            )
            .toList();
    }
}
