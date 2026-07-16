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

import java.util.List;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.dto.BudgetsResult;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.dto.FamilienBudgetInput;
import ch.dvbern.stip.berechnung.domain.util.InputUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BudgetsCalculator {
    BudgetsResult calculateStipendien(
        final GesuchFormular gesuchFormular,
        final List<Kind> kindsImPiaHaushalt,
        final List<FamilienBudgetInput> familienBudgetInputs,
        final int anzahlMonateGueltigkeit,
        final DateRange gesuchsDateRange,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr
    ) {
        final var anzahlKinderDerElternInAusbildung = Math.toIntExact(
            gesuchFormular.getGeschwisters()
                .stream()
                .filter(geschwister -> geschwister.getAusbildungssituation() == Ausbildungssituation.IN_AUSBILDUNG)
                .count()
        ) + InputUtils.PIA_COUNT;
        final boolean halbierungElternbeitrag = BernCalculatorUtil.getHalbierungElternbeitrag(
            gesuchFormular.getPersonInAusbildung().getGeburtsdatum(),
            gesuchsDateRange,
            gesuchFormular.getLebenslaufItems(),
            gesuchsperiode
        );

        final var familienBudgetresultats = familienBudgetInputs.stream()
            .map(
                familienBudgetInput -> FamilienBudgetCalculator.calculateFamilienBudget(
                    familienBudgetInput.elterns(),
                    familienBudgetInput.steuerdaten(),
                    familienBudgetInput.steuererklaerung(),
                    gesuchsperiode,
                    familienBudgetInput.kinderImHaushalt(),
                    anzahlKinderDerElternInAusbildung,
                    halbierungElternbeitrag,
                    gesuchsjahr
                )
            )
            .toList();

        final var persoenlichesBudgetresultat = PersoenlichesBudgetCalculator.calculatePersoenlichesBudget(
            gesuchFormular,
            familienBudgetresultats,
            kindsImPiaHaushalt,
            anzahlMonateGueltigkeit,
            gesuchsDateRange,
            gesuchsperiode,
            gesuchsjahr
        );

        return new BudgetsResult(
            persoenlichesBudgetresultat.getTotal(),
            familienBudgetresultats,
            persoenlichesBudgetresultat
        );
    }

}
