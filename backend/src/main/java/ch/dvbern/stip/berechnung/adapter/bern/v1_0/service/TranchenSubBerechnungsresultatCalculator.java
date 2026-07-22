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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.dto.BudgetsResult;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.dto.FamilienBudgetInput;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.domain.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.PersonenHaushaltGruppeDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDtoBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TranchenSubBerechnungsresultatCalculator {
    public TranchenBerechnungsresultatDto getTranchenSubBerechnungsresultat(
        final GesuchTranche gesuchTranche,
        final SteuerdatenTyp steuerdatenTypToPrioritize,
        final Boolean teilzeitKinderBeiPiaAnrechnen,
        final DateRange gesuchsDateRange,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr,
        final BerechnungsStammdatenMapper berechnungsStammdatenMapper
    ) {
        final GesuchFormular gesuchFormular = gesuchTranche.getGesuchFormular();

        final List<Kind> kindsImPiaHaushalt =
            getKindsImPiaHaushalt(gesuchFormular.getKinds(), teilzeitKinderBeiPiaAnrechnen);

        final List<FamilienBudgetInput> familienBudgetInputs =
            getFamilienBudgetInputs(gesuchFormular, gesuchsperiode, steuerdatenTypToPrioritize);

        final int anzahlMonateGueltigkeit = DateUtil.getMonthsBetween(
            gesuchTranche.getGueltigkeit().getGueltigAb(),
            gesuchTranche.getGueltigkeit().getGueltigBis()
        );

        final BudgetsResult budgetResults = BudgetsCalculator.calculateStipendien(
            gesuchFormular,
            kindsImPiaHaushalt,
            familienBudgetInputs,
            anzahlMonateGueltigkeit,
            gesuchsDateRange,
            gesuchsperiode,
            gesuchsjahr
        );

        final String yearRange = "%s/%s".formatted(
            gesuchsperiode.getGesuchsperiodeStart().getYear(),
            gesuchsperiode.getGesuchsperiodeStopp().getYear()
        );

        final BerechnungsStammdatenDto berechnungsStammdaten =
            berechnungsStammdatenMapper.toDto(gesuchsperiode, anzahlMonateGueltigkeit);
        final List<PersonenHaushaltGruppeDto> personenHaushaltGroups = BerechnungUtil.getPersonenHaushaltGroups(
            budgetResults.persoenlichesBudgetresultat(),
            budgetResults.familienBudgetresultate()
        );

        Integer total = budgetResults.stipendien();

        final BigDecimal berechnungsanteilKindsDerEltern =
            BernCalculatorUtil.getBerechnugsAnteilKindsDerEltern(gesuchFormular, steuerdatenTypToPrioritize);

        total = BernCalculatorUtil.calculateTotalBerechnungsAnteilKinds(total, berechnungsanteilKindsDerEltern);

        // if (
        // Objects.nonNull(steuerdatenTypToPrioritize)
        // && List.of(SteuerdatenTyp.VATER, SteuerdatenTyp.MUTTER).contains(steuerdatenTypToPrioritize)
        // ) {
        // final List<AbstractFamilieEntity> teilzeitKindsDerElternInHaushalten =
        // BernCalculatorUtil.getTeilzeitKindsDerElternInHaushalten(gesuchFormular);
        // final int anzahlTeilzeitKindsDerElternInHaushalten = teilzeitKindsDerElternInHaushalten.size();
        // assert anzahlTeilzeitKindsDerElternInHaushalten > 0;
        //
        // final BigDecimal kinderDerElternProzente =
        // BigDecimal.valueOf(teilzeitKindsDerElternInHaushalten.stream().mapToInt(
        // abstractFamilieEntity -> abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTypToPrioritize).intValue()
        // ).sum());
        //
        // berechnungsanteilKinderDerEltern = kinderDerElternProzente.divide(
        // BigDecimal.valueOf(anzahlTeilzeitKindsDerElternInHaushalten),
        // 2,
        // RoundingMode.HALF_UP
        // );
        //
        // // Calculate the total stipendien amount based on the respective amounts and their relative kid
        // // percentages.
        // total = berechnungsanteilKinderDerEltern.multiply(
        // BigDecimal.valueOf(total)
        // .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        // ).intValue();
        // total = Math.min(0, total);
        // }

        final BigDecimal berechnungsanteilKindsPia = BernCalculatorUtil.getBerechnugsAnteilKindsPia(
            gesuchFormular,
            kindsImPiaHaushalt,
            teilzeitKinderBeiPiaAnrechnen
        );

        total = BernCalculatorUtil.calculateTotalBerechnungsAnteilKinds(total, berechnungsanteilKindsPia);
        //
        //
        // if (Objects.nonNull(teilzeitKinderBeiPiaAnrechnen)) {
        // final List<Kind> teilzeitKinderDerPia = gesuchFormular.getKinds()
        // .stream()
        // .filter(kind -> kind.getWohnsitzAnteilPia() < 100)
        // .toList();
        //
        // BigDecimal teilzeitKinderDerPiaProzenteThisBerechnung =
        // BigDecimal.valueOf(
        // kindsImPiaHaushalt.stream()
        // .filter(kind -> kind.getWohnsitzAnteilPia() < 100)
        // .mapToInt(Kind::getWohnsitzAnteilPia)
        // .sum()
        // );
        //
        // berechnungsanteilKinderPia = teilzeitKinderDerPiaProzenteThisBerechnung.divide(
        // BigDecimal.valueOf(teilzeitKinderDerPia.size()),
        // 2,
        // RoundingMode.HALF_UP
        // );
        //
        // if (!teilzeitKinderBeiPiaAnrechnen) {
        // berechnungsanteilKinderPia = BigDecimal.valueOf(100).subtract(berechnungsanteilKinderPia);
        // }
        // // Calculate the total stipendien amount based on the respective amounts and their relative kid
        // // percentages.
        // total = berechnungsanteilKinderPia.multiply(
        // BigDecimal.valueOf(total)
        // .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        // ).intValue();
        // }

        return TranchenBerechnungsresultatDtoBuilder.tranchenBerechnungsresultatDto()
            .total(total)
            .ungekuerztTotal(budgetResults.stipendien())
            .gueltigAb(gesuchTranche.getGueltigkeit().getGueltigAb())
            .gueltigBis(gesuchTranche.getGueltigkeit().getGueltigBis())
            .ausbildungAb(DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungBegin()))
            .ausbildungBis(DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungEnd()))
            .yearRange(yearRange)
            .gesuchTrancheId(gesuchTranche.getId())
            .berechnungsStammdaten(berechnungsStammdaten)
            .persoenlichesBudgetresultat(budgetResults.persoenlichesBudgetresultat())
            .familienBudgetresultate(budgetResults.familienBudgetresultate())
            .personenHaushaltGroups(personenHaushaltGroups)
            .berechnungsanteilKinderDerEltern(berechnungsanteilKindsDerEltern)
            .teilzeitKinderBeiPiaAnrechnen(teilzeitKinderBeiPiaAnrechnen)
            .berechnungsanteilKinderPia(berechnungsanteilKindsPia)
            .build();
    }

    private List<FamilienBudgetInput> getFamilienBudgetInputs(
        final GesuchFormular gesuchFormular,
        final Gesuchsperiode gesuchsperiode,
        final SteuerdatenTyp steuerdatenTypToPrioritize
    ) {
        return gesuchFormular.getSteuerdaten()
            .stream()
            .map(
                steuerdaten -> {
                    final List<Eltern> elterns = switch (steuerdaten.getSteuerdatenTyp()) {
                        case FAMILIE -> gesuchFormular.getElterns().stream().toList();
                        case VATER -> gesuchFormular.getElterns()
                            .stream()
                            .filter(eltern -> eltern.getElternTyp() == ElternTyp.VATER)
                            .toList();
                        case MUTTER -> gesuchFormular.getElterns()
                            .stream()
                            .filter(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER)
                            .toList();
                    };

                    final Steuererklaerung steuererklaerungOfSteuerdaten = gesuchFormular.getSteuererklaerung()
                        .stream()
                        .filter(
                            steuererklaerung -> steuererklaerung.getSteuerdatenTyp() == steuerdaten.getSteuerdatenTyp()
                        )
                        .findFirst()
                        .get();
                    final List<AbstractFamilieEntity> allKinderDerElternInHaushalten =
                        BernCalculatorUtil.getKindsDerElternInHaushalten(gesuchFormular);

                    final List<AbstractFamilieEntity> kinderImHaushalt = allKinderDerElternInHaushalten.stream()
                        .filter(
                            abstractFamilieEntity -> abstractFamilieEntity
                                .getWohnsitzAnteil(steuerdaten.getSteuerdatenTyp())
                                .intValue() == 100
                            || (abstractFamilieEntity.getWohnsitzAnteil(steuerdaten.getSteuerdatenTyp()).intValue() > 0
                            && steuerdaten.getSteuerdatenTyp() == steuerdatenTypToPrioritize)
                        )
                        .toList();

                    return new FamilienBudgetInput(
                        steuerdaten.getSteuerdatenTyp(),
                        elterns,
                        steuerdaten,
                        steuererklaerungOfSteuerdaten,
                        gesuchsperiode,
                        kinderImHaushalt
                    );
                }
            )
            .toList();
    }

    private static List<Kind> getKindsImPiaHaushalt(
        final Set<Kind> kinds,
        final Boolean teilzeitKinderBeiPiaAnrechnen
    ) {
        return kinds
            .stream()
            .filter(
                kind -> kind.getWohnsitzAnteilPia() > 0
                && (kind.getWohnsitzAnteilPia() == 100
                || Objects.requireNonNullElse(teilzeitKinderBeiPiaAnrechnen, false))
            )
            .toList();
    }
}
