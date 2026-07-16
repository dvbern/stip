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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0.service;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TranchenBerechnungsresultatCalculator {
    public List<TranchenBerechnungsresultatDto> getTranchenBerechnungsresultat(
        final GesuchTranche gesuchTranche,
        final DateRange gesuchsDateRange,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr,
        final BerechnungsStammdatenMapper berechnungsStammdatenMapper
    ) {
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var hasTeilzeitKinderDerEltern =
            !BernCalculatorUtil.getTeilzeitKindsDerElternInHaushalten(gesuchFormular).isEmpty();

        List<SteuerdatenTyp> steuerdatenTypsToPrioritize = new ArrayList<>();

        if (gesuchFormular.getFamiliensituation().getElternVerheiratetZusammen()) {
            steuerdatenTypsToPrioritize.add(SteuerdatenTyp.FAMILIE);
        } else {
            if (hasTeilzeitKinderDerEltern) {
                steuerdatenTypsToPrioritize.addAll(
                    gesuchFormular.getSteuerdaten().stream().map(Steuerdaten::getSteuerdatenTyp).toList()
                );
            } else {
                steuerdatenTypsToPrioritize.add(null);
            }
        }

        final var hasTeilzeitKinderDerPia =
            !gesuchFormular.getKinds()
                .stream()
                .filter(
                    kind -> kind.getWohnsitzAnteilPia() > 0
                    && kind.getWohnsitzAnteilPia() < 100
                )
                .toList()
                .isEmpty();

        final List<Boolean> teilzeitKinderBeiPiaAnrechnenVals = new ArrayList<>();
        if (hasTeilzeitKinderDerPia) {
            teilzeitKinderBeiPiaAnrechnenVals.addAll(List.of(Boolean.TRUE, Boolean.FALSE));
        } else {
            teilzeitKinderBeiPiaAnrechnenVals.add(null);
        }

        List<TranchenBerechnungsresultatDto> berechnungsresultatDtoList = new ArrayList<>();

        for (final var teilzeitKinderBeiPiaAnrechnen : teilzeitKinderBeiPiaAnrechnenVals) {
            for (final var steuerdatenTypToPrioritize : steuerdatenTypsToPrioritize) {
                berechnungsresultatDtoList.add(
                    TranchenSubBerechnungsresultatCalculator.getTranchenSubBerechnungsresultat(
                        gesuchTranche,
                        steuerdatenTypToPrioritize,
                        teilzeitKinderBeiPiaAnrechnen,
                        gesuchsDateRange,
                        gesuchsperiode,
                        gesuchsjahr,
                        berechnungsStammdatenMapper
                    )
                );
            }
        }
        return berechnungsresultatDtoList;
    }

}
