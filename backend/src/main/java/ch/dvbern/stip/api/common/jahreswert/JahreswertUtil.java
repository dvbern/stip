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

import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JahreswertUtil {
    public void synchroniseJahreswerte(final GesuchTranche gesuchTranche) {
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var targetTranchen = gesuchTranche.getGesuch()
            .getTranchenTranchen()
            .filter(
                tranche -> !tranche.getId()
                    .equals(gesuchTranche.getId())
            )
            .toList();

        if (targetTranchen.isEmpty()) {
            return;
        }

        final var steuererklaerungen = gesuchFormular.getSteuererklaerung();
        for (final var steuererklaerung : steuererklaerungen) {
            final var targets = SteuererklaerungJahreswertUtil.selectTargetSteuererklaerung(
                steuererklaerung,
                targetTranchen
            );

            SteuererklaerungJahreswertUtil.synchroniseJahresfelder(
                steuererklaerung,
                targets
            );
        }

        final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final var targetEinnahmenKosten = targetTranchen.stream()
            .map(targetTranche -> targetTranche.getGesuchFormular().getEinnahmenKosten())
            .toList();
        EinnahmenKostenJahreswertUtil.synchroniseJahresfelder(
            einnahmenKosten,
            targetEinnahmenKosten
        );

        final var einnahmenKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
        final var targetEinnahmenPartner = targetTranchen.stream()
            .map(targetTranche -> targetTranche.getGesuchFormular().getEinnahmenKostenPartner())
            .toList();
        EinnahmenKostenJahreswertUtil.synchroniseJahresfelder(
            einnahmenKostenPartner,
            targetEinnahmenPartner
        );

        final var steuerdatenTabs = gesuchFormular.getSteuerdaten();
        for (final var steuerdaten : steuerdatenTabs) {
            final var targets = SteuerdatenJahreswertUtil.selectTargetSteuerdaten(steuerdaten, targetTranchen);

            SteuerdatenJahreswertUtil.synchroniseJahresfelder(
                steuerdaten,
                targets
            );
        }
    }
}
