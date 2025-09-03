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

package ch.dvbern.stip.berechnung.service.v1;

import java.util.function.BiFunction;
import java.util.function.Function;

import ch.dvbern.stip.api.common.util.StringUtil;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import ch.dvbern.stip.berechnung.dto.v1.FamiliensituationV1;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@CalculatorVersion(major = 1, minor = 0)
public class PersonenImHaushaltCalculatorV1 implements PersonenImHaushaltCalculator {
    @Override
    public PersonenImHaushaltResult calculatePersonenImHaushalt(DmnRequest request) {
        if (request instanceof PersonenImHaushaltRequestV1 input) {
            return calculatePersonenImHaushalt(input);
        }

        throw new IllegalArgumentException("Called PersonenImHaushaltCalculatorV1 with a wrong request type");
    }

    public PersonenImHaushaltResult calculatePersonenImHaushalt(final PersonenImHaushaltRequestV1 input) {
        final var personenImHaushalt = input.getPersonenImHaushaltInput();
        final var elternImHaushalt = calculateElternImHaushalt(personenImHaushalt.getFamiliensituation());

        final var result = PersonenImHaushaltResult.builder();

        calculateAndSetPersonenImHaushalt(
            result,
            personenImHaushalt,
            elternImHaushalt
        );

        return result.build();
    }

    private void calculateAndSetPersonenImHaushalt(
        final PersonenImHaushaltResult.PersonenImHaushaltResultBuilder result,
        final PersonenImHaushaltRequestV1.PersonenImHaushaltInputV1 personenImHaushalt,
        final ElternImHaushalt elternImHaushalt
    ) {
        final var familiensituation = personenImHaushalt.getFamiliensituation();

        final var personInAusbildungModifier =
            calculatePersonInAusbildungModifier(personenImHaushalt.getPersonInAusbildung().getWohnsitz());
        final int geschwisterVollzeitAndTeilzeit = personenImHaushalt.getGeschwisterMutterVollzeit()
        + personenImHaushalt.getGeschwisterVaterVollzeit() + personenImHaushalt.getGeschwisterTeilzeit();
        final int kinderInEinzelnerHaushalt = personInAusbildungModifier + geschwisterVollzeitAndTeilzeit;
        final int elternImHaushalt1EinzelnerHaushalt =
            elternImHaushalt.getElternImHaushalt1() + geschwisterVollzeitAndTeilzeit;
        final int elternImHaushalt2EinzelnerHaushalt =
            elternImHaushalt.getElternImHaushalt2() + geschwisterVollzeitAndTeilzeit;

        if (StringUtil.isNullOrEmpty(personenImHaushalt.getElternToPrioritize())) {
            calculateAndSetIfNoElternToPrioritize(
                result,
                familiensituation,
                kinderInEinzelnerHaushalt,
                elternImHaushalt1EinzelnerHaushalt,
                elternImHaushalt2EinzelnerHaushalt
            );
        } else {
            calculateAndSetIfElternToPrioritize(
                personenImHaushalt,
                result,
                familiensituation,
                personInAusbildungModifier,
                elternImHaushalt
            );
        }
    }

    private void calculateAndSetIfNoElternToPrioritize(
        final PersonenImHaushaltResult.PersonenImHaushaltResultBuilder result,
        final FamiliensituationV1 familiensituation,
        final int kinderInEinzelnerHaushalt,
        final int elternImHaushalt1EinzelnerHaushalt,
        final int elternImHaushalt2EinzelnerHaushalt
    ) {
        if (familiensituation.getElternVerheiratetZusammen()) {
            result.noBudgetsRequired(1)
                .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                .kinderImHaushalt2(0)
                .personenImHaushalt1(elternImHaushalt1EinzelnerHaushalt)
                .personenImHaushalt2(0);
        } else {
            if (familiensituation.getGerichtlicheAlimentenregelung()) {
                switch (familiensituation.getWerZahltAlimente()) {
                    case "MUTTER" -> result.noBudgetsRequired(1)
                        .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                        .kinderImHaushalt2(0)
                        .personenImHaushalt1(elternImHaushalt1EinzelnerHaushalt)
                        .personenImHaushalt2(0);
                    case "VATER" -> result.noBudgetsRequired(1)
                        .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                        .kinderImHaushalt2(0)
                        .personenImHaushalt1(elternImHaushalt2EinzelnerHaushalt)
                        .personenImHaushalt2(0);
                    case "GEMEINSAM" -> result.noBudgetsRequired(0)
                        .kinderImHaushalt1(0)
                        .kinderImHaushalt2(0)
                        .personenImHaushalt1(0)
                        .personenImHaushalt2(0);
                }
            } else {
                if (familiensituation.getElternteilUnbekanntVerstorben()) {
                    if (
                        familiensituation.getVaterUnbekanntVerstorben()
                        && !familiensituation.getMutterUnbekanntVerstorben()
                    ) {
                        result.noBudgetsRequired(1)
                            .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                            .kinderImHaushalt2(0)
                            .personenImHaushalt1(elternImHaushalt2EinzelnerHaushalt)
                            .personenImHaushalt2(0);
                    } else if (
                        !familiensituation.getVaterUnbekanntVerstorben()
                        && familiensituation.getMutterUnbekanntVerstorben()
                    ) {
                        result.noBudgetsRequired(1)
                            .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                            .kinderImHaushalt2(0)
                            .personenImHaushalt1(elternImHaushalt1EinzelnerHaushalt)
                            .personenImHaushalt2(0);
                    } else if (
                        familiensituation.getVaterUnbekanntVerstorben()
                        && familiensituation.getMutterUnbekanntVerstorben()
                    ) {
                        result.noBudgetsRequired(0)
                            .kinderImHaushalt1(0)
                            .kinderImHaushalt2(0)
                            .personenImHaushalt1(0)
                            .personenImHaushalt2(0);
                    } else {
                        LOG.error(
                            "Reached an impossible state: One elternetil is unbekannt or verstorben but neither was"
                        );
                        result.noBudgetsRequired(0)
                            .kinderImHaushalt1(0)
                            .kinderImHaushalt2(0)
                            .personenImHaushalt1(0)
                            .personenImHaushalt2(0);
                    }
                }
            }
        }
    }

    private void calculateAndSetIfElternToPrioritize(
        final PersonenImHaushaltRequestV1.PersonenImHaushaltInputV1 personenImHaushalt,
        final PersonenImHaushaltResult.PersonenImHaushaltResultBuilder result,
        final FamiliensituationV1 familiensituation,
        final int personInAusbildungModifier,
        final ElternImHaushalt elternImHaushalt
    ) {
        if (familiensituation.getElternVerheiratetZusammen()) {
            LOG.error(
                "Called calculateAndSetIfElternToPrioritize with a Familiensituation where the parents are verheiratet or zusammen, doing nothing"
            );
            return;
        }

        if (familiensituation.getGerichtlicheAlimentenregelung()) {
            LOG.error(
                "Called calculateAndSetIfElternToPrioritize with a Familiensituation with a gerichtliche alimentenregelung, doing nothing"
            );
            return;
        }

        if (familiensituation.getElternteilUnbekanntVerstorben()) {
            LOG.error(
                "Called calculateAndSetIfElternToPrioritize with a Familiensituation with a elternetil unbekannt or verstorben, doing nothing"
            );
            return;
        }

        // All the cases where an elternteil is prioritized needs 2 budgets
        result.noBudgetsRequired(2);

        // There are fundamentally 3 cases, twice, once "normal" once "flipped", e.g.
        // When to use which case is handled by the ifs below, the param is either
        // geschwisterVaterVollzeit or geschwisterMutterVollzeit
        // wohnsitzAnteilVater > 0 && wohnsitzAnteilMutter >= 0
        final Function<Integer, Integer> getFirstCase =
            (anteil) -> personInAusbildungModifier + anteil + personenImHaushalt.getGeschwisterTeilzeit();

        // wohnsitzAnteilVater = 0 && wohnsitzAnteilMutter >= 0
        final Function<Integer, Integer> getSecondCase =
            (anteil) -> anteil + personenImHaushalt.getGeschwisterTeilzeit();

        // wohnsitzAnteilVater = null && wohnsitzAnteilMutter = null

        final var personInAusbildung = personenImHaushalt.getPersonInAusbildung();
        final var wohnsitzAnteilVater = personInAusbildung.getWohnsitzAnteilVater();
        final var wohnsitzAnteilMutter = personInAusbildung.getWohnsitzAnteilMutter();

        final BiFunction<Integer, Integer, Integer> getPersonenInHaushalt = (haushalt, geschwisterVollzeit) -> haushalt
        + geschwisterVollzeit + personenImHaushalt.getGeschwisterTeilzeit();
        final BiFunction<Integer, Integer, Integer> getPersonenInHaushaltWithContribution =
            (haushalt, geschwisterVollzeit) -> personInAusbildungModifier
            + getPersonenInHaushalt.apply(haushalt, geschwisterVollzeit);

        final var elternteilToPrioritize = personenImHaushalt.getElternToPrioritize();
        if (elternteilToPrioritize.equals("VATER")) {
            if (
                wohnsitzAnteilVater != null && wohnsitzAnteilVater > 0 && wohnsitzAnteilMutter != null
                && wohnsitzAnteilMutter >= 0
            ) {
                result.kinderImHaushalt1(getFirstCase.apply(personenImHaushalt.getGeschwisterVaterVollzeit()))
                    .kinderImHaushalt2(personenImHaushalt.getGeschwisterMutterVollzeit())
                    .personenImHaushalt1(
                        getPersonenInHaushaltWithContribution.apply(
                            elternImHaushalt.getElternImHaushalt1(),
                            personenImHaushalt.getGeschwisterVaterVollzeit()
                        )
                    )
                    .personenImHaushalt2(
                        elternImHaushalt.getElternImHaushalt2() + personenImHaushalt.getGeschwisterMutterVollzeit()
                    );
            } else if (
                wohnsitzAnteilVater != null && wohnsitzAnteilVater == 0 && wohnsitzAnteilMutter != null
                && wohnsitzAnteilMutter >= 0
            ) {
                result.kinderImHaushalt1(getSecondCase.apply(personenImHaushalt.getGeschwisterVaterVollzeit()))
                    .kinderImHaushalt2(personInAusbildungModifier + personenImHaushalt.getGeschwisterMutterVollzeit())
                    .personenImHaushalt1(
                        getPersonenInHaushalt.apply(
                            elternImHaushalt.getElternImHaushalt1(),
                            personenImHaushalt.getGeschwisterVaterVollzeit()
                        )
                    )
                    .personenImHaushalt2(
                        getPersonenInHaushaltWithContribution.apply(
                            elternImHaushalt.getElternImHaushalt2(),
                            personenImHaushalt.getGeschwisterMutterVollzeit()
                        )
                    );
            } else if (wohnsitzAnteilVater == null && wohnsitzAnteilMutter == null) {
                result.kinderImHaushalt1(getSecondCase.apply(personenImHaushalt.getGeschwisterVaterVollzeit()))
                    .kinderImHaushalt2(personenImHaushalt.getGeschwisterMutterVollzeit())
                    .personenImHaushalt1(
                        getPersonenInHaushalt.apply(
                            elternImHaushalt.getElternImHaushalt1(),
                            personenImHaushalt.getGeschwisterVaterVollzeit()
                        )
                    )
                    .personenImHaushalt2(
                        elternImHaushalt.getElternImHaushalt2() + personenImHaushalt.getGeschwisterMutterVollzeit()
                    );
            }
        } else if (elternteilToPrioritize.equals("MUTTER")) {
            if (
                wohnsitzAnteilVater != null && wohnsitzAnteilVater >= 0 && wohnsitzAnteilMutter != null
                && wohnsitzAnteilMutter > 0
            ) {
                result.kinderImHaushalt1(personenImHaushalt.getGeschwisterVaterVollzeit())
                    .kinderImHaushalt2(getFirstCase.apply(wohnsitzAnteilMutter))
                    .personenImHaushalt1(
                        elternImHaushalt.getElternImHaushalt1() + personenImHaushalt.getGeschwisterVaterVollzeit()
                    )
                    .personenImHaushalt2(
                        getPersonenInHaushaltWithContribution.apply(
                            elternImHaushalt.getElternImHaushalt2(),
                            personenImHaushalt.getGeschwisterMutterVollzeit()
                        )
                    );
            } else if (
                wohnsitzAnteilVater != null && wohnsitzAnteilVater >= 0 && wohnsitzAnteilMutter != null
                && wohnsitzAnteilMutter == 0
            ) {
                result.kinderImHaushalt1(personInAusbildungModifier + personenImHaushalt.getGeschwisterVaterVollzeit())
                    .kinderImHaushalt2(getSecondCase.apply(personenImHaushalt.getGeschwisterMutterVollzeit()))
                    .personenImHaushalt1(
                        getPersonenInHaushaltWithContribution.apply(
                            elternImHaushalt.getElternImHaushalt1(),
                            personenImHaushalt.getGeschwisterVaterVollzeit()
                        )
                    )
                    .personenImHaushalt2(
                        getPersonenInHaushalt.apply(
                            elternImHaushalt.getElternImHaushalt2(),
                            personenImHaushalt.getGeschwisterMutterVollzeit()
                        )
                    );
            } else if (wohnsitzAnteilVater == null && wohnsitzAnteilMutter == null) {
                result.kinderImHaushalt1(personenImHaushalt.getGeschwisterVaterVollzeit())
                    .kinderImHaushalt2(getSecondCase.apply(personenImHaushalt.getGeschwisterMutterVollzeit()))
                    .personenImHaushalt1(
                        elternImHaushalt.getElternImHaushalt1() + personenImHaushalt.getGeschwisterVaterVollzeit()
                    )
                    .personenImHaushalt2(
                        getPersonenInHaushalt.apply(
                            elternImHaushalt.getElternImHaushalt2(),
                            personenImHaushalt.getGeschwisterMutterVollzeit()
                        )
                    );
            }
        }
    }

    private ElternImHaushalt calculateElternImHaushalt(
        final FamiliensituationV1 familiensituation
    ) {
        if (familiensituation.getElternVerheiratetZusammen()) {
            return new ElternImHaushalt(2, 0);
        }

        if (familiensituation.getVaterWiederverheiratet()) {
            if (familiensituation.getMutterWiederverheiratet()) {
                return new ElternImHaushalt(2, 2);
            }

            return new ElternImHaushalt(2, 1);
        }

        if (familiensituation.getMutterWiederverheiratet()) {
            return new ElternImHaushalt(1, 2);
        }

        return new ElternImHaushalt(1, 1);
    }

    private int calculatePersonInAusbildungModifier(final String wohnsitz) {
        return switch (wohnsitz) {
            case "EIGENER_HAUSHALT" -> 0;
            case "FAMILIE", "MUTTER_VATER" -> 1;
            default -> throw new IllegalArgumentException("Invalid Wohnsitz passed, was: " + wohnsitz);
        };
    }

    @Getter
    @RequiredArgsConstructor
    private class ElternImHaushalt {
        private final int elternImHaushalt1;
        private final int elternImHaushalt2;
    }
}
