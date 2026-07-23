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
import java.util.Set;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
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
        final GesuchFormular gesuchFormular = gesuchTranche.getGesuchFormular();

        List<SteuerdatenTyp> steuerdatenTypsToPrioritize = getSteuerdatenTypsToPrioritize(gesuchFormular);

        final List<Boolean> teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals =
            getTeilzeitStiefHalbGeschwistersBeiElternAnrechnenVals(gesuchFormular);

        final List<Boolean> teilzeitKinderBeiPiaAnrechnenVals =
            getTeilzeitKinderBeiPiaAnrechnenVals(gesuchFormular.getKinds());

        List<TranchenBerechnungsresultatDto> berechnungsresultatDtoList = new ArrayList<>();

        for (final Boolean teilzeitKinderBeiPiaAnrechnen : teilzeitKinderBeiPiaAnrechnenVals) {
            for (
                final Boolean teilzeitStiefHalbGeschwistersBeiElternAnrechnen : teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals
            ) {
                for (final SteuerdatenTyp steuerdatenTypToPrioritize : steuerdatenTypsToPrioritize) {
                    berechnungsresultatDtoList.add(
                        TranchenSubBerechnungsresultatCalculator.getTranchenSubBerechnungsresultat(
                            gesuchTranche,
                            steuerdatenTypToPrioritize,
                            teilzeitStiefHalbGeschwistersBeiElternAnrechnen,
                            teilzeitKinderBeiPiaAnrechnen,
                            gesuchsDateRange,
                            gesuchsperiode,
                            gesuchsjahr,
                            berechnungsStammdatenMapper
                        )
                    );
                }
            }
        }
        return berechnungsresultatDtoList;
    }

    private List<Boolean> getTeilzeitKinderBeiPiaAnrechnenVals(
        final Set<Kind> kinds
    ) {
        final boolean hasTeilzeitKinderDerPia = hasTeilzeitKinderDePia(kinds);

        final List<Boolean> teilzeitKinderBeiPiaAnrechnenVals = new ArrayList<>();
        if (hasTeilzeitKinderDerPia) {
            teilzeitKinderBeiPiaAnrechnenVals.addAll(List.of(Boolean.TRUE, Boolean.FALSE));
        } else {
            teilzeitKinderBeiPiaAnrechnenVals.add(null);
        }
        return teilzeitKinderBeiPiaAnrechnenVals;
    }

    private boolean hasTeilzeitKinderDePia(
        final Set<Kind> kinds
    ) {
        return !kinds
            .stream()
            .filter(
                kind -> kind.getWohnsitzAnteilPia() > 0
                && kind.getWohnsitzAnteilPia() < 100
            )
            .toList()
            .isEmpty();
    }

    private List<Boolean> getTeilzeitStiefHalbGeschwistersBeiElternAnrechnenVals(
        final GesuchFormular gesuchFormular
    ) {
        final var hasTeilzeitStiefHalbGeschwisterinHaushalten =
            !BernCalculatorUtil.getStiefHalbTeilzeitKindsDerElternInHaushalten(gesuchFormular).isEmpty();
        final List<Boolean> teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals = new ArrayList<>();
        if (hasTeilzeitStiefHalbGeschwisterinHaushalten) {
            teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals.addAll(List.of(Boolean.TRUE, Boolean.FALSE));
        } else {
            teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals.add(null);
        }
        return teilzeitStiefHalbGeschwistersBeiElternAnrechnenVals;

    }

    private List<SteuerdatenTyp> getSteuerdatenTypsToPrioritize(
        final GesuchFormular gesuchFormular
    ) {
        final boolean hasLeiblichTeilzeitKinderDerEltern =
            !BernCalculatorUtil.getLeiblichTeilzeitKindsDerElternInHaushalten(gesuchFormular).isEmpty();

        List<SteuerdatenTyp> steuerdatenTypsToPrioritize = new ArrayList<>();

        if (gesuchFormular.getFamiliensituation().getElternVerheiratetZusammen()) {
            steuerdatenTypsToPrioritize.add(SteuerdatenTyp.FAMILIE);
        } else {
            if (hasLeiblichTeilzeitKinderDerEltern) {
                steuerdatenTypsToPrioritize.addAll(
                    gesuchFormular.getSteuerdaten().stream().map(Steuerdaten::getSteuerdatenTyp).toList()
                );
                if (gesuchFormular.getFamiliensituation().getGerichtlicheAlimentenregelung()) {
                    if (gesuchFormular.getFamiliensituation().getWerZahltAlimente() != Elternschaftsteilung.GEMEINSAM) {
                        steuerdatenTypsToPrioritize.add(
                            switch (gesuchFormular.getFamiliensituation().getWerZahltAlimente()) {
                                case MUTTER -> SteuerdatenTyp.MUTTER;
                                case VATER -> SteuerdatenTyp.VATER;
                                case null, default -> throw new IllegalStateException("Unreachable");
                            }
                        );
                    }
                }
            } else {
                steuerdatenTypsToPrioritize.add(null);
            }
        }
        return steuerdatenTypsToPrioritize;
    }

}
