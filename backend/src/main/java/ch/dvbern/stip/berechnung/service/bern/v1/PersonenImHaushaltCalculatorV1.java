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

import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import ch.dvbern.stip.berechnung.dto.v1.FamiliensituationV1;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1.PersonenImHaushaltInputV1;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltCalculator;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@CalculatorVersion(major = 1, minor = 0)
public class PersonenImHaushaltCalculatorV1 implements PersonenImHaushaltCalculator {
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

        final var result = calculateAndSetPersonenImHaushalt(
            personenImHaushalt,
            elternImHaushalt
        );

        return result;
    }

    private PersonenImHaushaltResult calculateAndSetPersonenImHaushalt(
        final PersonenImHaushaltInputV1 personenImHaushalt,
        final ElternImHaushalt elternImHaushalt
    ) {
        final var familiensituation = personenImHaushalt.getFamiliensituation();
        final var personInAusbildungModifier =
            calculatePersonInAusbildungModifier(personenImHaushalt.getPersonInAusbildung().getWohnsitz());
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
            && (!Objects.requireNonNullElse(
                familiensituation.getWerZahltAlimente(),
                Elternschaftsteilung.GEMEINSAM.name()
            ).equals(Elternschaftsteilung.GEMEINSAM.name())))
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
        } else if (
            // Alimenteregelung exists and is payed by both parents
            (Objects.requireNonNullElse(familiensituation.getGerichtlicheAlimentenregelung(), false)
            && (Objects.requireNonNullElse(
                familiensituation.getWerZahltAlimente(),
                Elternschaftsteilung.VATER.name()
            ).equals(Elternschaftsteilung.GEMEINSAM.name())))
            // Both parents are unknown/dead
            || (Objects.requireNonNullElse(familiensituation.getElternteilUnbekanntVerstorben(), false)
            && (Objects.requireNonNullElse(
                familiensituation.getVaterUnbekanntVerstorben(),
                false
            ))
            && (Objects.requireNonNullElse(
                familiensituation.getMutterUnbekanntVerstorben(),
                false
            )))
        ) {
            // Do nothing
        } else {
            noBudgetsRequired = 2;
            kinderImHaushalt1 = personenImHaushalt.getGeschwisterVaterVollzeit();
            kinderImHaushalt2 = personenImHaushalt.getGeschwisterMutterVollzeit();
            final var personInAusbildung = personenImHaushalt.getPersonInAusbildung();
            final int wohnsitzanteilVater = Objects.requireNonNullElse(personInAusbildung.getWohnsitzAnteilVater(), 0);
            final int wohnsitzanteilMutter =
                Objects.requireNonNullElse(personInAusbildung.getWohnsitzAnteilMutter(), 0);

            switch (personenImHaushalt.getElternToPrioritize()) {
                case "VATER" -> {
                    kinderImHaushalt1 += personenImHaushalt.getGeschwisterTeilzeit();
                    if (wohnsitzanteilVater > 0) {
                        kinderImHaushalt1 += personInAusbildungModifier;
                    } else if (wohnsitzanteilMutter > 0) {
                        kinderImHaushalt2 += personInAusbildungModifier;
                    }
                }
                case "MUTTER" -> {
                    kinderImHaushalt2 += personenImHaushalt.getGeschwisterTeilzeit();
                    if (wohnsitzanteilMutter > 0) {
                        kinderImHaushalt2 += personInAusbildungModifier;
                    } else if (wohnsitzanteilVater > 0) {
                        kinderImHaushalt1 += personInAusbildungModifier;
                    }
                }
                default -> throw new IllegalStateException(
                    "Unexpected value: " + personenImHaushalt.getElternToPrioritize()
                );
            }
            personenImHaushalt1 = kinderImHaushalt1 + elternImHaushalt.getElternImHaushalt1();
            personenImHaushalt2 = kinderImHaushalt2 + elternImHaushalt.getElternImHaushalt2();
        }
        return new PersonenImHaushaltResult(
            noBudgetsRequired,
            kinderImHaushalt1,
            kinderImHaushalt2,
            personenImHaushalt1,
            personenImHaushalt2
        );
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
