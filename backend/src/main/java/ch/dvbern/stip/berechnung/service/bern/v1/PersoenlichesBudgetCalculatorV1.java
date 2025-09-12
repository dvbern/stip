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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputPersoenlichesbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;
import static java.lang.Math.max;

@UtilityClass
public class PersoenlichesBudgetCalculatorV1 {
    public PersoenlichesBudgetresultatDto calculatePersoenlichesBudget(
        final InputPersoenlichesbudgetV1 input,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var result = new PersoenlichesBudgetresultatDto();
        final var antragssteller = input.getAntragssteller();

        calculateAndSetEinnahmen(
            result,
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );

        calculateAndSetAusgaben(
            result,
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );

        final var rawBerechnet =
            BigDecimal.valueOf(result.getEinnahmenPersoenlichesBudget() - result.getAusgabenPersoenlichesBudget());
        if (antragssteller.isVerheiratetKonkubinat() && antragssteller.isEigenerHaushalt()) {
            result.setPersoenlichesbudgetBerechnet(
                rawBerechnet
                    .divide(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()), RoundingMode.HALF_UP)
                    .intValue()
            );
        } else {
            result.setPersoenlichesbudgetBerechnet(rawBerechnet.intValue());
        }

        result.setAnteilLebenshaltungskosten(
            calculateAnteilLebenshaltungskosten(List.of(familienbudget1, familienbudget2), antragssteller)
        );
        result.setAnzahlPersonenImHaushalt(antragssteller.getAnzahlPersonenImHaushalt());

        return result;
    }

    private void calculateAndSetAusgaben(
        final PersoenlichesBudgetresultatDto result,
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        List<Function<PersoenlichesBudgetresultatDto, Integer>> haushaltToApply;
        if (antragssteller.isEigenerHaushalt()) {
            haushaltToApply = List.of(
                mapAndReturn(PersoenlichesBudgetresultatDto::setGrundbedarf, antragssteller.getGrundbedarf()),
                mapAndReturn(PersoenlichesBudgetresultatDto::setWohnkosten, antragssteller.getWohnkosten()),
                mapAndReturn(
                    PersoenlichesBudgetresultatDto::setMedizinischeGrundversorgung,
                    antragssteller.getMedizinischeGrundversorgung()
                )
            );
        } else {
            haushaltToApply = List.of(
                mapAndReturn(PersoenlichesBudgetresultatDto::setGrundbedarf, 0),
                mapAndReturn(PersoenlichesBudgetresultatDto::setWohnkosten, 0),
                mapAndReturn(PersoenlichesBudgetresultatDto::setMedizinischeGrundversorgung, 0)
            );
        }
        result.eigenerHaushalt(antragssteller.isEigenerHaushalt());

        int anzahlWochen = 0;
        if (!antragssteller.isEigenerHaushalt()) {
            if (antragssteller.isLehre()) {
                anzahlWochen = stammdaten.getAnzahlWochenLehre();
            } else {
                anzahlWochen = stammdaten.getAnzahlWochenSchule();
            }
        }

        final Function<PersoenlichesBudgetresultatDto, Integer> verpflegungApplier = mapAndReturn(
            PersoenlichesBudgetresultatDto::setVerpflegung,
            antragssteller.getVerpflegung() * anzahlWochen * stammdaten.getPreisProMahlzeit()
        );

        List<Function<PersoenlichesBudgetresultatDto, Integer>> verheiratetKonkubinatToApply;
        if (antragssteller.isVerheiratetKonkubinat()) {
            verheiratetKonkubinatToApply = List.of(
                mapAndReturn(
                    PersoenlichesBudgetresultatDto::setAusbildungskosten,
                    antragssteller.getAusbildungskosten() * antragssteller.getAnzahlPersonenImHaushalt()
                ),
                mapAndReturn(
                    PersoenlichesBudgetresultatDto::setFahrkosten,
                    antragssteller.getFahrkosten() * antragssteller.getAnzahlPersonenImHaushalt()
                )
            );
        } else {
            verheiratetKonkubinatToApply = List.of(
                mapAndReturn(
                    PersoenlichesBudgetresultatDto::setAusbildungskosten,
                    antragssteller.getAusbildungskosten()
                ),
                mapAndReturn(PersoenlichesBudgetresultatDto::setFahrkosten, antragssteller.getFahrkosten())
            );
        }

        final var basicToApply = List.of(
            mapAndReturn(PersoenlichesBudgetresultatDto::setSteuernKantonGemeinde, antragssteller.getSteuern()),
            mapAndReturn(PersoenlichesBudgetresultatDto::setVerpflegungPartner, antragssteller.getVerpflegungPartner()),
            mapAndReturn(PersoenlichesBudgetresultatDto::setFremdbetreuung, antragssteller.getFremdbetreuung()),
            mapAndReturn(PersoenlichesBudgetresultatDto::setFahrkostenPartner, antragssteller.getFahrkostenPartner())
        );

        final var anteilFamilienbudget1 = Math.abs(
            calculateAnteilFamilienbudget(
                antragssteller,
                familienbudget1,
                2,
                1
            )
        );

        final var anteilFamilienbudget2 = Math.abs(
            calculateAnteilFamilienbudget(
                antragssteller,
                familienbudget2,
                1,
                2
            )
        );

        result.setAnteilFamilienbudget(anteilFamilienbudget1 + anteilFamilienbudget2);

        final var wohnkostenAbhaengig = applyAndSum(haushaltToApply, result);
        final var verpflegung = applyAndSum(List.of(verpflegungApplier), result);
        final var verheiratetKonkubinatAbhaengig = applyAndSum(verheiratetKonkubinatToApply, result);
        final var basic = applyAndSum(basicToApply, result);

        result.setAusgabenPersoenlichesBudget(
            wohnkostenAbhaengig + verpflegung + verheiratetKonkubinatAbhaengig + basic + anteilFamilienbudget1
            + anteilFamilienbudget2
        );
    }

    private Integer calculateAnteilFamilienbudget(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget,
        final int wohntImHaushaltZero,
        final int wohntImHaushaltAnrechnen
    ) {
        if (familienbudget == null) {
            return null;
        }

        if (familienbudget.getFamilienbudgetBerechnet() < 0) {
            if (!antragssteller.isEigenerHaushalt()) {
                if (antragssteller.getPiaWohntInElternHaushalt() == wohntImHaushaltZero) {
                    return 0;
                } else if (antragssteller.getPiaWohntInElternHaushalt() == wohntImHaushaltAnrechnen) {
                    return roundHalfUp(
                        BigDecimal.valueOf(familienbudget.getFamilienbudgetBerechnet())
                            .divide(
                                BigDecimal.valueOf(familienbudget.getAnzahlPersonenImHaushalt()),
                                RoundingMode.HALF_UP
                            )
                    );
                }
            }
        }

        return 0;
    }

    private void calculateAndSetEinnahmen(
        final PersoenlichesBudgetresultatDto result,
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var elternbeitrag1 = calculateElternbeitrag(antragssteller, familienbudget1);
        final var elternbeitrag2 = calculateElternbeitrag(antragssteller, familienbudget2);

        final int einkommen;
        if (antragssteller.isTertiaerstufe()) {
            einkommen = max(antragssteller.getEinkommen() - stammdaten.getEinkommensfreibetrag(), 0);
        } else {
            einkommen = antragssteller.getEinkommen();
        }

        final var toApply = List.of(
            mapAndReturn(PersoenlichesBudgetresultatDto::setEinkommen, einkommen),
            mapAndReturn(PersoenlichesBudgetresultatDto::setEinkommenPartner, antragssteller.getEinkommenPartner()),
            mapAndReturn(
                PersoenlichesBudgetresultatDto::setAnrechenbaresVermoegen,
                roundHalfUp(
                    BigDecimal.valueOf(antragssteller.getVermoegen())
                        .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                )
            ),
            mapAndReturn(PersoenlichesBudgetresultatDto::setAlimente, antragssteller.getAlimente()),
            mapAndReturn(PersoenlichesBudgetresultatDto::setRente, antragssteller.getRente()),
            mapAndReturn(
                PersoenlichesBudgetresultatDto::setKinderAusbildungszulagen,
                antragssteller.getKinderAusbildungszulagen()
            ),
            mapAndReturn(
                PersoenlichesBudgetresultatDto::setErgaenzungsleistungen,
                antragssteller.getErgaenzungsleistungen()
            ),
            mapAndReturn(PersoenlichesBudgetresultatDto::setLeistungenEO, antragssteller.getLeistungenEO()),
            mapAndReturn(
                PersoenlichesBudgetresultatDto::setGemeindeInstitutionen,
                antragssteller.getGemeindeInstitutionen()
            ),
            mapAndReturn(PersoenlichesBudgetresultatDto::setElternbeitrag1, elternbeitrag1),
            mapAndReturn(PersoenlichesBudgetresultatDto::setElternbeitrag2, elternbeitrag2)
        );

        final var einnahmen = applyAndSum(toApply, result);
        result.setSteuerbaresVermoegen(antragssteller.getVermoegen());
        result.setEinnahmenPersoenlichesBudget(einnahmen);
    }

    private Integer calculateElternbeitrag(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget
    ) {
        if (familienbudget == null) {
            return null;
        }

        if (familienbudget.getFamilienbudgetBerechnet() > 0) {
            final var anzahlGeschwisterInAusbildung =
                BigDecimal.valueOf(familienbudget.getAnzahlGeschwisterInAusbildung() + 1);
            final var fractionalValue = BigDecimal.valueOf(familienbudget.getFamilienbudgetBerechnet())
                .divide(anzahlGeschwisterInAusbildung, RoundingMode.HALF_UP);
            if (antragssteller.isHalbierungElternbeitrag()) {
                return fractionalValue.divide(BigDecimal.TWO, RoundingMode.HALF_UP).intValue();
            } else {
                return fractionalValue.intValue();
            }
        }

        return 0;
    }

    private static int calculateAnteilLebenshaltungskosten(
        List<FamilienBudgetresultatDto> familienBudgetresultatList,
        AntragsstellerV1 antragssteller
    ) {
        int anteilLebenshaltungskosten = 0;
        int piaWohntInElternHaushalt = antragssteller.getPiaWohntInElternHaushalt();
        ListIterator<FamilienBudgetresultatDto> iterator = familienBudgetresultatList.listIterator();
        while (iterator.hasNext()) {
            final FamilienBudgetresultatDto familienBudgetresultat = iterator.next();
            final var budgetIdx = iterator.nextIndex();
            final var familienBudget = familienBudgetresultat.getFamilienbudgetBerechnet();
            if (familienBudget >= 0 || antragssteller.isEigenerHaushalt() || budgetIdx != piaWohntInElternHaushalt) {
                continue;
            }
            anteilLebenshaltungskosten += familienBudget / familienBudgetresultat.getAnzahlPersonenImHaushalt();
        }
        return anteilLebenshaltungskosten;
    }

    private Function<PersoenlichesBudgetresultatDto, Integer> mapAndReturn(
        final BiConsumer<PersoenlichesBudgetresultatDto, Integer> getter,
        final Integer value
    ) {
        return CalculatorUtilV1.mapAndReturn(getter, value);
    }

    private int applyAndSum(
        final List<Function<PersoenlichesBudgetresultatDto, Integer>> toApply,
        final PersoenlichesBudgetresultatDto result
    ) {
        return CalculatorUtilV1.applyAndSum(
            toApply.stream(),
            result
        );
    }
}
