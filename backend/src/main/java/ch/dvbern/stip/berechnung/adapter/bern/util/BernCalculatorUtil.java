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

package ch.dvbern.stip.berechnung.adapter.bern.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.berechnung.domain.util.MathUtil.roundHalfUp;
import static java.lang.Math.max;

@UtilityClass
public class BernCalculatorUtil {
    public static final int PIA_COUNT = 1;

    public String getElternPartnerName(final ElternTyp elternTyp) {
        return String.format("Partner %s", StringUtils.capitalize(elternTyp.name().toLowerCase()));
    }

    public int getSaeule3a(
        final Steuerdaten steuerdaten,
        final Gesuchsperiode gesuchsperiode
    ) {
        var saeule3a = 0;
        if (steuerdaten.getIsArbeitsverhaeltnisSelbstaendig()) {
            saeule3a =
                max(Objects.requireNonNullElse(steuerdaten.getSaeule3a(), 0) - gesuchsperiode.getMaxSaeule3a(), 0);
        }
        return saeule3a;
    }

    public int getSaeule2(
        final Steuerdaten steuerdaten
    ) {
        var saeule2 = 0;
        if (steuerdaten.getIsArbeitsverhaeltnisSelbstaendig()) {
            saeule2 = Objects.requireNonNullElse(steuerdaten.getSaeule2(), 0);
        }
        return saeule2;
    }

    public int getAnrechenbaresVermoegen(
        final int vermoegen,
        final boolean isArbeitsverhaeltnisSelbstaendig,
        final Gesuchsperiode gesuchsperiode
    ) {
        var anrechenbaresVermoegen = BigDecimal.valueOf(vermoegen);
        if (isArbeitsverhaeltnisSelbstaendig) {
            // steuerbaresVermoegen - freibetragVermoegen
            anrechenbaresVermoegen =
                BigDecimal.valueOf(
                    max(
                        vermoegen - gesuchsperiode.getFreibetragVermoegen(),
                        0
                    )
                );
        }
        return roundHalfUp(
            anrechenbaresVermoegen.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(gesuchsperiode.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                .abs()
        );
    }

    public static boolean getHalbierungElternbeitrag(
        final LocalDate geburtsdatumPia,
        final DateRange gesuchsDateRange,
        final Set<LebenslaufItem> lebenslaufItemSet,
        final Gesuchsperiode gesuchsperiode
    ) {
        final var abgeschlosseneErstausbildungLebenslaufItem = lebenslaufItemSet.stream()
            .filter(
                lebenslaufItem -> lebenslaufItem.isAusbildung()
                && Objects.nonNull(lebenslaufItem.getAbschluss())
                && lebenslaufItem.getAbschluss().isBerufsbefaehigenderAbschluss()
                && lebenslaufItem.isAusbildungAbgeschlossen()
            )
            .findFirst();

        final boolean abgeschlosseneErstausbildung = abgeschlosseneErstausbildungLebenslaufItem.isPresent();

        boolean erstAusbildungWasCompletedBeforeAusbildungsjahr = false;
        if (abgeschlosseneErstausbildung) {
            erstAusbildungWasCompletedBeforeAusbildungsjahr =
                abgeschlosseneErstausbildungLebenslaufItem.get().getVon().isBefore(gesuchsDateRange.getGueltigAb());
        }

        var alterAtEndOfAusbildungsjahr =
            DateUtil.getAgeInYearsAtDate(geburtsdatumPia, gesuchsDateRange.getGueltigBis());

        final boolean halbierungAbgeschlosseneErstausbildung =
            erstAusbildungWasCompletedBeforeAusbildungsjahr
            && (alterAtEndOfAusbildungsjahr >= gesuchsperiode
                .getLimiteAlterAntragsstellerHalbierungElternbeitrag());
        final var beruftaetigkeiten = Set.of(
            Taetigkeitsart.ERWERBSTAETIGKEIT,
            Taetigkeitsart.BETREUUNG_FAMILIENMITGLIEDER_EIGENER_HAUSHALT
        );
        final var berufstaetigeItems = lebenslaufItemSet.stream()
            .filter(
                lebenslaufItem -> lebenslaufItem.getTaetigkeitsart() != null
            )
            .filter(
                lebenslaufItem -> beruftaetigkeiten.contains(
                    lebenslaufItem.getTaetigkeitsart()
                )
            );
        final int monthsBerufstaetig = berufstaetigeItems
            .mapToInt(lebenslaufItem -> (int) ChronoUnit.DAYS.between(lebenslaufItem.getVon(), lebenslaufItem.getBis()))
            .sum()
        / 30;
        final boolean halbierungBerufstaetig = monthsBerufstaetig >= 72;

        return halbierungAbgeschlosseneErstausbildung || halbierungBerufstaetig;
    }

    public BigDecimal calculateAnteilLebenshaltungskosten(
        final boolean antragsstellerWohntInDiesemHaushalt,
        final BigDecimal total,
        final int anzahlPersonenImHaushalt
    ) {
        if (
            (total.signum() >= 0) || !antragsstellerWohntInDiesemHaushalt
        ) {
            return BigDecimal.ZERO;
        }

        return total
            .divide(
                BigDecimal.valueOf(anzahlPersonenImHaushalt),
                RoundingMode.HALF_UP
            )
            .abs();
    }

    public int getGrundbedarf(
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

    public int getEffektiveWohnkostenFamilie(
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

    public static int getEffektiveWohnkostenPersoenlich(
        final int eingegebeneWohnkosten,
        final Gesuchsperiode gesuchsperiode,
        int anzahlPersonenImHaushalt
    ) {
        int maxWohnkosten = switch (anzahlPersonenImHaushalt) {
            case 0 -> throw new IllegalStateException("0 Personen im Haushalt");
            case 1 -> gesuchsperiode.getWohnkostenPersoenlich1pers();
            case 2 -> gesuchsperiode.getWohnkostenPersoenlich2pers();
            case 3 -> gesuchsperiode.getWohnkostenPersoenlich3pers();
            case 4 -> gesuchsperiode.getWohnkostenPersoenlich4pers();
            default -> gesuchsperiode.getWohnkostenPersoenlich5pluspers();
        };
        return Integer.min(eingegebeneWohnkosten, maxWohnkosten);
    }

    static int getAlterForMedizinischeGrundversorgung(
        final LocalDate geburtsdatum,
        final int gesuchsjahr,
        final Gesuchsperiode gesuchsperiode
    ) {
        final var stichtag =
            gesuchsperiode.getStichtagVolljaehrigkeitMedizinischeGrundversorgung().withYear(gesuchsjahr);
        return DateUtil.getAgeInYearsAtDate(
            geburtsdatum,
            stichtag
        );
    }

    public int getMedizinischeGrundversorgung(
        final LocalDate geburtsdatum,
        final int gesuchsjahr,
        final Gesuchsperiode gesuchsperiode
    ) {
        int alterForMedizinischeGrundversorgung = getAlterForMedizinischeGrundversorgung(
            geburtsdatum,
            gesuchsjahr,
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

    public int getAusbildungskosten(
        final Integer ausbildungskosten,
        final Gesuchsperiode gesuchsperiode,
        final Bildungskategorie bildungskategorie
    ) {
        if (bildungskategorie.isTertiaerstufe()) {
            return Integer.min(
                Objects.requireNonNullElse(ausbildungskosten, 0),
                gesuchsperiode.getAusbKostenTertiaer()
            );
        }
        return Integer.min(
            Objects.requireNonNullElse(ausbildungskosten, 0),
            gesuchsperiode.getAusbKostenSekII()
        );
    }

    public Integer calculateElternbeitragTotal(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final Gesuchsperiode gesuchsperiode,
        final DateRange gesuchsDateRange
    ) {
        final var elternbeitrag = new HashMap<>();

        familienBudgetresultats.forEach(
            familienBudgetresultat -> elternbeitrag.put(
                familienBudgetresultat.getVorname(),
                BernCalculatorUtil.calculateElternbeitragTotal(
                    familienBudgetresultat.getEinnahmeUeberschuss(),
                    familienBudgetresultat.getAnzahlKinderInAusbildung(),
                    BernCalculatorUtil.getHalbierungElternbeitrag(
                        gesuchFormular.getPersonInAusbildung().getGeburtsdatum(),
                        gesuchsDateRange,
                        gesuchFormular.getLebenslaufItems(),
                        gesuchsperiode
                    )
                )
            )
        );
        return elternbeitrag.values()
            .stream()
            .mapToInt(
                value -> (Integer) value
            )
            .sum();
    }

    public Integer calculateElternbeitragTotal(
        final Integer einnahmeUeberschuss,
        final int anzahlGeschwisterInAusbildung,
        final boolean isHalbierungElternbeitrag
    ) {
        if (einnahmeUeberschuss <= 0) {
            return 0;
        }

        final var fractionalValue = BigDecimal.valueOf(einnahmeUeberschuss)
            .divide(BigDecimal.valueOf(anzahlGeschwisterInAusbildung), RoundingMode.HALF_UP);
        if (isHalbierungElternbeitrag) {
            return fractionalValue.divide(BigDecimal.TWO, RoundingMode.HALF_UP).intValue();
        }
        return fractionalValue.intValue();
    }

    public List<AbstractFamilieEntity> getKindsDerElternInHaushalten(final GesuchFormular gesuchFormular) {
        List<AbstractFamilieEntity> kinderDerElternInHaushalten = new ArrayList<>(
            gesuchFormular.getGeschwisters()
                .stream()
                .filter(
                    geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT
                )
                .map(AbstractFamilieEntity.class::cast)
                .toList()
        );

        if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
            kinderDerElternInHaushalten.add(gesuchFormular.getPersonInAusbildung());
        }
        return kinderDerElternInHaushalten;
    }

    public List<AbstractFamilieEntity> getTeilzeitKindsDerElternInHaushalten(
        final GesuchFormular gesuchFormular
    ) {
        List<AbstractFamilieEntity> kindsDerElternInHaushalten = getKindsDerElternInHaushalten(gesuchFormular);

        final var teilzeitKinderDerElternInHaushalten = kindsDerElternInHaushalten.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(), BigDecimal.ZERO)
                    .intValue() > 0
                && Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0
            )
            .toList();
        return teilzeitKinderDerElternInHaushalten;
    }
}
