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

package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1.getAlterForMedizinischeGrundversorgung;

@Data
@Builder
@Jacksonized
@Value
@JsonIgnoreProperties
public class BerechnungRequestV1 implements CalculatorRequest {
    @JsonProperty("Stammdaten_V1")
    StammdatenV1 stammdaten;

    @JsonProperty("InputFamilienbudget_1_V1")
    InputFamilienbudgetV1 inputFamilienbudget1;

    @JsonProperty("InputFamilienbudget_2_V1")
    InputFamilienbudgetV1 inputFamilienbudget2;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    @Getter
    public static class InputFamilienbudgetV1 {
        @JsonProperty("elternteil")
        ElternteilV1 elternteil;
    }

    @JsonProperty("InputPersoenlichesbudget_V1")
    InputPersoenlichesbudgetV1 inputPersoenlichesBudget;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    @Getter
    public static class InputPersoenlichesbudgetV1 {
        @JsonProperty("antragssteller")
        AntragsstellerV1 antragssteller;
    }

    @Override
    public int majorVersion() {
        return 1;
    }

    @Override
    public int minorVersion() {
        return 0;
    }

    public static BerechnungRequestV1 createRequest(
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp,
        final PersonenImHaushaltService personenImHaushaltService
    ) {
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var personenImHaushaltRequest = personenImHaushaltService.getPersonenImHaushaltRequest(
            ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1Builder.class.getAnnotation(CalculatorVersion.class)
                .major(),
            ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1Builder.class.getAnnotation(CalculatorVersion.class)
                .minor(),
            gesuchFormular,
            elternTyp
        );
        final var personenImHaushalt = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequest);
        final List<Integer> personenImHaushaltList = List.of(
            personenImHaushalt.getPersonenImHaushalt1(),
            personenImHaushalt.getPersonenImHaushalt2()
        );

        final List<ElternteilV1> elternteilerequests = new ArrayList<>(
            List.of(
                ElternteilV1.builderWithDefaults().build(),
                ElternteilV1.builderWithDefaults().build()
            )
        );

        final List<Eltern> elternteile = gesuchFormular.getElterns().stream().toList();
        ListIterator<Steuerdaten> steuerdatenListIterator = gesuchFormular.getSteuerdaten()
            .stream()
            .sorted(
                Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
            )
            .toList()
            .listIterator();

        List<AbstractFamilieEntity> kinderDerElternInHaushalten = new ArrayList<>(
            gesuchFormular.getGeschwisters()
                .stream()
                .filter(
                    geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT
                )
                .map(AbstractFamilieEntity.class::cast)
                .toList()
        );
        final var personInAusbildung = gesuchFormular.getPersonInAusbildung();
        int piaWohntInElternHaushalt = 0;
        if (personInAusbildung.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
            kinderDerElternInHaushalten.add(personInAusbildung);
            if (personInAusbildung.getWohnsitz() == Wohnsitz.FAMILIE) {
                piaWohntInElternHaushalt = 1;
            } else if (personInAusbildung.getWohnsitz() == Wohnsitz.MUTTER_VATER) {
                if (elternteile.size() < 2) {
                    piaWohntInElternHaushalt = 1;
                } else if (elternTyp == ElternTyp.VATER) {
                    piaWohntInElternHaushalt = personInAusbildung.getWohnsitzAnteilVater().intValue() > 0 ? 1 : 2;
                } else if (elternTyp == ElternTyp.MUTTER) {
                    piaWohntInElternHaushalt = personInAusbildung.getWohnsitzAnteilMutter().intValue() > 0 ? 2 : 1;
                }
            }
        }

        while (steuerdatenListIterator.hasNext()) {
            final int currentIdx = steuerdatenListIterator.nextIndex();

            final var steuerdatenToUse = steuerdatenListIterator.next();
            final var steuerdatenTypToUse = steuerdatenToUse.getSteuerdatenTyp();
            final var steuererklaerungToUse = gesuchFormular.getSteuererklaerung()
                .stream()
                .filter(steuererklaerung1 -> steuererklaerung1.getSteuerdatenTyp().equals(steuerdatenTypToUse))
                .findFirst()
                .orElseThrow();
            elternteilerequests.set(
                currentIdx,
                ElternteilV1.buildFromDependants(
                    currentIdx + 1,
                    gesuch.getGesuchsperiode(),
                    elternteile,
                    steuererklaerungToUse,
                    steuerdatenToUse,
                    personenImHaushaltList.get(currentIdx),
                    kinderDerElternInHaushalten,
                    (int) gesuchFormular.getGeschwisters()
                        .stream()
                        .filter(
                            geschwister -> geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE
                        )
                        .count(),
                    (int) gesuchFormular.getGeschwisters()
                        .stream()
                        .filter(
                            geschwister -> geschwister.getAusbildungssituation() == Ausbildungssituation.IN_AUSBILDUNG
                        )
                        .count(),
                    elternTyp,
                    gesuchFormular.getFamiliensituation(),
                    gesuch.getAusbildung().getAusbildungBegin()
                )
            );
        }

        final var antragssteller = AntragsstellerV1.buildFromDependants(gesuchFormular, piaWohntInElternHaushalt);
        final var anzahlMonate = DateUtil.getMonthsBetween(
            gesuchTranche.getGueltigkeit().getGueltigAb(),
            gesuchTranche.getGueltigkeit().getGueltigBis()
        );

        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode(), anzahlMonate),
            new InputFamilienbudgetV1(elternteilerequests.get(0)),
            new InputFamilienbudgetV1(elternteilerequests.get(1)),
            new InputPersoenlichesbudgetV1(antragssteller)
        );
    }

    public static int getGrundbedarf(
        final Gesuchsperiode gesuchsperiode,
        final int anzahlPersonenImHaushalt,
        final boolean wohntInWG
    ) {
        int grundbedarf = switch (anzahlPersonenImHaushalt) {
            case 1 -> gesuchsperiode.getPerson1();
            case 2 -> gesuchsperiode.getPersonen2();
            case 3 -> gesuchsperiode.getPersonen3();
            case 4 -> gesuchsperiode.getPersonen4();
            case 5 -> gesuchsperiode.getPersonen5();
            case 6 -> gesuchsperiode.getPersonen6();
            case 7 -> gesuchsperiode.getPersonen7();
            default -> gesuchsperiode.getPersonen7()
            + (anzahlPersonenImHaushalt - 7) * gesuchsperiode.getProWeiterePerson();
        };

        if (wohntInWG) {
            grundbedarf -= gesuchsperiode.getReduzierungDesGrundbedarfs();
        }

        return grundbedarf;
    }

    public static int getEffektiveWohnkosten(
        final int wohnkostenJahreswert,
        final Gesuchsperiode gesuchsperiode,
        int anzahlPersonenImHaushalt
    ) {
        int maxWohnkosten = switch (anzahlPersonenImHaushalt) {
            case 0 -> throw new IllegalStateException("0 Personen im Haushalt");
            case 1 -> gesuchsperiode.getWohnkostenFam1pers();
            case 2 -> gesuchsperiode.getWohnkostenFam2pers();
            case 3 -> gesuchsperiode.getWohnkostenFam3pers();
            case 4 -> gesuchsperiode.getWohnkostenFam4pers();
            default -> gesuchsperiode.getWohnkostenFam5pluspers();
        };

        return Integer.min(wohnkostenJahreswert, maxWohnkosten);
    }

    public static int getMedizinischeGrundversorgung(
        final LocalDate geburtsdatum,
        final LocalDate ausbildungsBegin,
        final Gesuchsperiode gesuchsperiode
    ) {
        int alterForMedizinischeGrundversorgung = getAlterForMedizinischeGrundversorgung(
            geburtsdatum,
            ausbildungsBegin,
            gesuchsperiode
        );
        // Per Stichtag 25 Jahre alt oder älter (inkl. 25. Geburtstag am Stichtag) = Erwachsene
        int medizinischeGrundversorgung = gesuchsperiode.getErwachsene2599();
        // Per Stichtag 0-17 Jahre alt (inkl. 17. Geburtstag am Stichtag) = Kindertarif
        if (alterForMedizinischeGrundversorgung <= 17) {
            medizinischeGrundversorgung = gesuchsperiode.getKinder0017();
        } else if (alterForMedizinischeGrundversorgung <= 24) {
            // Per Stichtag 18 bis und mit 24 (inkl. 18. und 24. Geburtstag am Stichtag) = Junge Erwachsene
            medizinischeGrundversorgung = gesuchsperiode.getJugendlicheErwachsene1824();
        }
        return medizinischeGrundversorgung;
    }
}
