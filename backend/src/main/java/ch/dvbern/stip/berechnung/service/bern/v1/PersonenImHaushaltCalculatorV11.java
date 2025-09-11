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

package ch.dvbern.stip.berechnung.service.bern.v1;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import ch.dvbern.stip.berechnung.dto.v1.FamiliensituationV1;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltCalculator;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@CalculatorVersion(major = 1, minor = 0)
public class PersonenImHaushaltCalculatorV11 implements PersonenImHaushaltCalculator {
    @Override
    public PersonenImHaushaltResult calculatePersonenImHaushalt(CalculatorRequest request) {
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
        final PersonenImHaushaltRequestV1.PersonenImHaushaltInputV1 personenImHaushalt,
        final Familiensituation familiensituation,
        final int personInAusbildungModifier,
        final ElternImHaushalt elternImHaushalt
    ) {
        var noBudgetsRequired = 0;
        var kinderImHaushalt1 = 0;
        var kinderImHaushalt2 = 0;
        var personenImHaushalt1 = 0;
        var personenImHaushalt2 = 0;

        // All the most simplet cases
        // Only one budget needed
        if (
            // parents are together
            familiensituation.getElternVerheiratetZusammen()
            // One Parents pays Alimony
            || (Objects.requireNonNullElse(familiensituation.getGerichtlicheAlimentenregelung(), false)
            && (Objects.requireNonNullElse(
                familiensituation.getWerZahltAlimente(),
                Elternschaftsteilung.GEMEINSAM
            ) != Elternschaftsteilung.GEMEINSAM))
            // One parent is unknown/dead
            || (Objects.requireNonNullElse(familiensituation.getElternteilUnbekanntVerstorben(), false)
            && (Objects.requireNonNullElse(familiensituation.getVaterUnbekanntVerstorben(), false) != Objects
                .requireNonNullElse(familiensituation.getMutterUnbekanntVerstorben(), false)))
        ) {
            noBudgetsRequired = 1;
            kinderImHaushalt1 =
                personInAusbildungModifier
                + personenImHaushalt.getGeschwisterMutterVollzeit()
                + personenImHaushalt.getGeschwisterVaterVollzeit()
                + personenImHaushalt.getGeschwisterTeilzeit();
            personenImHaushalt1 =
                kinderImHaushalt1
                + elternImHaushalt.getElternImHaushalt1();
        }

        if (
            // Alimenteregelung exists and is payed by both parents
            (Objects.requireNonNullElse(familiensituation.getGerichtlicheAlimentenregelung(), false)
            && (Objects.requireNonNullElse(
                familiensituation.getWerZahltAlimente(),
                Elternschaftsteilung.VATER
            ) == Elternschaftsteilung.GEMEINSAM))
            // Both parents are unknown/dead
            || (Objects.requireNonNullElse(familiensituation.getElternteilUnbekanntVerstorben(), false)
            && (Objects.requireNonNullElse(
                familiensituation.getVaterUnbekanntVerstorben(),
                ElternAbwesenheitsGrund.WEDER_NOCH
            ) != ElternAbwesenheitsGrund.WEDER_NOCH)
            && (Objects.requireNonNullElse(
                familiensituation.getMutterUnbekanntVerstorben(),
                ElternAbwesenheitsGrund.WEDER_NOCH
            ) != ElternAbwesenheitsGrund.WEDER_NOCH))
        ) {

        } else {
            kinderImHaushalt1 += 1;
        }

    }

    private void calculateAndSetPersonenImHaushalt(
        final PersonenImHaushaltResult.PersonenImHaushaltResultBuilder result,
        final PersonenImHaushaltRequestV1.PersonenImHaushaltInputV1 personenImHaushalt,
        final ElternImHaushalt elternImHaushalt,
        final int bla
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
                        .personenImHaushalt1(elternImHaushalt1EinzelnerHaushalt + personInAusbildungModifier)
                        .personenImHaushalt2(0);
                    case "VATER" -> result.noBudgetsRequired(1)
                        .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                        .kinderImHaushalt2(0)
                        .personenImHaushalt1(elternImHaushalt2EinzelnerHaushalt + personInAusbildungModifier)
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
                            .personenImHaushalt1(elternImHaushalt2EinzelnerHaushalt + personInAusbildungModifier)
                            .personenImHaushalt2(0);
                    } else if (
                        !familiensituation.getVaterUnbekanntVerstorben()
                        && familiensituation.getMutterUnbekanntVerstorben()
                    ) {
                        result.noBudgetsRequired(1)
                            .kinderImHaushalt1(kinderInEinzelnerHaushalt)
                            .kinderImHaushalt2(0)
                            .personenImHaushalt1(elternImHaushalt1EinzelnerHaushalt + personInAusbildungModifier)
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

            final BiFunction<Integer, Integer, Integer> getPersonenInHaushalt =
                (haushalt, geschwisterVollzeit) -> haushalt
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
                        .kinderImHaushalt2(
                            personInAusbildungModifier + personenImHaushalt.getGeschwisterMutterVollzeit()
                        )
                        .personenImHaushalt1(
                            getPersonenInHaushalt.apply(
                                elternImHaushalt.getElternImHaushalt1(),
                                personenImHaushalt.getGeschwisterVaterVollzeit()
                            )
                        )
                        .personenImHaushalt2(
                            elternImHaushalt.getElternImHaushalt2() + personInAusbildungModifier
                            + personenImHaushalt.getGeschwisterMutterVollzeit()
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
                        .kinderImHaushalt2(getFirstCase.apply(personenImHaushalt.getGeschwisterMutterVollzeit()))
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
                    result
                        .kinderImHaushalt1(
                            personInAusbildungModifier + personenImHaushalt.getGeschwisterVaterVollzeit()
                        )
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
    }

    private ElternImHaushalt calculateElternImHaushalt(
        final FamiliensituationV1 familiensituation
    ) {
        var elternImHaushalt1 = 0;
        var elternImHaushalt2 = 0;

        if (familiensituation.getElternVerheiratetZusammen()) {
            elternImHaushalt1 = 2;
        } else {
            elternImHaushalt1 = 1;
            elternImHaushalt2 = 1;
            if (familiensituation.getMutterWiederverheiratet()) {
                elternImHaushalt1 += 1;
            }
            if (familiensituation.getVaterWiederverheiratet()) {
                elternImHaushalt2 += 1;
            }
        }
        return new ElternImHaushalt(elternImHaushalt1, elternImHaushalt2);
    }

    private int calculatePersonInAusbildungModifier(final String wohnsitz) {
        return switch (wohnsitz) {
            case "EIGENER_HAUSHALT" -> 0;
            case "FAMILIE", "MUTTER_VATER" -> 1;
            default -> throw new IllegalArgumentException("Invalid Wohnsitz passed, was: " + wohnsitz);
        };
    }
}
