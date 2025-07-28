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

package ch.dvbern.stip.stipdecision.decider;

import java.time.LocalDate;
import java.util.Optional;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.stipdecision.type.StipDeciderResult;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecider extends BaseStipDecider {
    private final PlzService plzService;

    @Override
    public StipDeciderResult decide(final GesuchTranche gesuchTranche) {
        if (eingabefristAbgelaufen(gesuchTranche)) {
            return StipDeciderResult.NEGATIVVERFUEGUNG_NICHTEINTRETENSVERFUEGUNG;
        }
        final var stipendienrechtlicherWohnsitzKantonBernResult =
            StipendienrechtlicherWohnsitzKantonBernChecker.evaluate(gesuchTranche, plzService);
        if (stipendienrechtlicherWohnsitzKantonBernResult != StipDeciderResult.GESUCH_VALID) {
            return stipendienrechtlicherWohnsitzKantonBernResult;
        }
        if (ausbildungNichtAnerkannt(gesuchTranche)) {
            return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNG_NICHT_ANERKANNT;
        }
        if (ausbildungImLebenslauf(gesuchTranche)) {
            return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_ZWEITAUSBILDUNG;
        }
        if (ausbildungLaenger12Jahre(gesuchTranche)) {
            return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNGSDAUER;
        }
        if (piaAelter35Jahre(gesuchTranche)) {
            return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_ALTER_PIA;
        }
        return StipDeciderResult.GESUCH_VALID;
    }

    @Override
    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(StipDeciderResult decision) {
        return switch (decision) {
            case GESUCH_VALID -> GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
            case NEGATIVVERFUEGUNG_NICHTEINTRETENSVERFUEGUNG -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case NEGATIVVERFUEGUNG_NICHT_BERECHTIGTE_PERSON -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_PIA_NICHT_BERN ->
                GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_FLUECHTLING_NICHT_BERN ->
                GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN ->
                GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            // case NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN ->
            // GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_FINANZIELL_UNABHAENGIG ->
                GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN ->
                GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_KESB_NICHT_BERN ->
                GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN ->
                GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_ZWEITAUSBILDUNG -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNGSDAUER -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNG_NICHT_ANERKANNT ->
                GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG;
            case ANSPRUCH_MANUELL_PRUEFEN_ALTER_PIA -> GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG;
            case ANSPRUCH_UNKLAR -> throw new IllegalStateException("Unkown StipDeciderResult: " + decision);
        };
    }

    private static boolean eingabefristAbgelaufen(final GesuchTranche gesuchTranche) {
        return LocalDate.now().isAfter(gesuchTranche.getGesuch().getGesuchsperiode().getEinreichefristReduziert());
    }

    private static boolean ausbildungNichtAnerkannt(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuch().getAusbildung().isAusbildungNichtGefunden();
    }

    private static boolean ausbildungImLebenslauf(final GesuchTranche gesuchTranche) {
        return gesuchTranche
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .anyMatch(
                lebenslaufItem -> lebenslaufItem.getAbschluss() != null
            );
    }

    private static boolean ausbildungLaenger12Jahre(final GesuchTranche gesuchTranche) {
        int monthsInAusbildung = gesuchTranche
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .mapToInt(
                lebenslaufItem -> {
                    if (lebenslaufItem.getAbschluss() != null) {
                        return DateUtil.getMonthsBetween(lebenslaufItem.getVon(), lebenslaufItem.getBis());
                    }
                    return 0;
                }
            )
            .sum();

        monthsInAusbildung +=
            DateUtil.getMonthsBetween(gesuchTranche.getGesuch().getAusbildung().getAusbildungBegin(), LocalDate.now());

        return monthsInAusbildung / 12.0 >= 12.0;
    }

    private static boolean piaAelter35Jahre(final GesuchTranche gesuchTranche) {
        return DateUtil
            .getAgeInYears(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getGeburtsdatum()) > 35;
    }

    static final class StipendienrechtlicherWohnsitzKantonBernChecker {
        private StipendienrechtlicherWohnsitzKantonBernChecker() {
        }

        public static StipDeciderResult evaluate(
            final GesuchTranche gesuchTranche,
            final PlzService plzService
        ) {
            final var step1result = evaluateStep1(gesuchTranche);
            if (step1result != StipDeciderResult.ANSPRUCH_UNKLAR) {
                return step1result;
            }
            final var step2result = evaluateStep2(gesuchTranche, plzService);
            if (step2result != StipDeciderResult.ANSPRUCH_UNKLAR) {
                return step2result;
            }
            return evaluateStep3(gesuchTranche, plzService);
        }

        private static StipDeciderResult evaluateStep1(
            final GesuchTranche gesuchTranche
        ) {
            if (piaHasSchweizerBuergerrecht(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }
            if (piaHasFluechtlingsstatus(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }
            if (piaNationalitaetEuEfta(gesuchTranche)) {
                if (piaWohntSchweiz(gesuchTranche)) {
                    return StipDeciderResult.ANSPRUCH_UNKLAR;
                }
                return StipDeciderResult.NEGATIVVERFUEGUNG_NICHT_BERECHTIGTE_PERSON;
            }
            if (piaHasNiederlassungsbewilligungC(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }
            if (piaHasNiederlassungsbewilligungB(gesuchTranche) && pia5JahreInChWohnhaft(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }
            return StipDeciderResult.NEGATIVVERFUEGUNG_NICHT_BERECHTIGTE_PERSON;
        }

        private static StipDeciderResult evaluateStep2(
            final GesuchTranche gesuchTranche,
            final PlzService plzService
        ) {
            if (piaVolljaehrig(gesuchTranche) && piaBerufsbefaehigendeAusbildungAbeschlossen(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_FINANZIELL_UNABHAENGIG;
            }
            if (piaFluechtlingOderStaatenlos(gesuchTranche)) {
                if (elternlosOderElternImAusland(gesuchTranche)) {
                    if (piaKantonMandantZugewiesen(gesuchTranche)) {
                        return StipDeciderResult.GESUCH_VALID;
                    }
                    return StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_FLUECHTLING_NICHT_BERN;
                }
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }

            if (!piaNationalitaetEuEfta(gesuchTranche)) {
                if (elternlosOderElternImAusland(gesuchTranche)) {
                    if (piaBernWohnhaft(gesuchTranche, plzService)) {
                        return StipDeciderResult.GESUCH_VALID;
                    }
                    return StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_PIA_NICHT_BERN;
                }
                return StipDeciderResult.ANSPRUCH_UNKLAR;
            }
            if (piaHasSchweizerBuergerrecht(gesuchTranche) && elternlosOderElternImAusland(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN;
            }
            return StipDeciderResult.ANSPRUCH_UNKLAR;
        }

        private static StipDeciderResult evaluateStep3(final GesuchTranche gesuchTranche, final PlzService plzService) {
            if (piaBevormundet(gesuchTranche)) {
                return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_KESB_NICHT_BERN;
            }
            return evaluateElternWohnsitz(gesuchTranche, plzService);
        }

        private static StipDeciderResult evaluateElternWohnsitz(
            final GesuchTranche gesuchTranche,
            final PlzService plzService
        ) {
            final int noEltern = gesuchTranche.getGesuchFormular().getElterns().size();
            final int noElternInBern = (int) gesuchTranche.getGesuchFormular()
                .getElterns()
                .stream()
                .filter(eltern -> plzService.isInBern(eltern.getAdresse()))
                .count();

            if (noElternInBern == noEltern) {
                return StipDeciderResult.GESUCH_VALID;
            }
            if (noElternInBern == 0) {
                return StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN;
            }
            return StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN;
        }

        private static boolean piaHasSchweizerBuergerrecht(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNationalitaet()
                .is(WellKnownLand.CHE);
        }

        private static boolean piaHasFluechtlingsstatus(final GesuchTranche gesuchTranche) {
            return Niederlassungsstatus.ALL_WITH_FLUECHTLINGSSTATUS
                .contains(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getNiederlassungsstatus());
        }

        private static boolean piaWohntSchweiz(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getAdresse()
                .getLand()
                .is(WellKnownLand.CHE);
        }

        private static boolean piaHasNiederlassungsbewilligungC(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNiederlassungsstatus() == Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C;
        }

        private static boolean piaHasNiederlassungsbewilligungB(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNiederlassungsstatus() == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B;
        }

        private static boolean pia5JahreInChWohnhaft(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getEinreisedatum() != null
                && gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getEinreisedatum()
                .isBefore(LocalDate.now().minusYears(5));
        }

        private static boolean piaVolljaehrig(final GesuchTranche gesuchTranche) {
            return DateUtil.getAgeInYears(
                gesuchTranche.getGesuchFormular()
                    .getPersonInAusbildung()
                    .getGeburtsdatum()
            ) >= 18;
        }

        private static boolean piaBerufsbefaehigendeAusbildungAbeschlossen(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getLebenslaufItems()
                .stream()
                .anyMatch(
                    item -> item.getAbschluss() != null

                        && item.getAbschluss().isBerufsbefaehigenderAbschluss()
                        && item.isAusbildungAbgeschlossen()
                );
        }

        private static boolean piaFluechtlingOderStaatenlos(final GesuchTranche gesuchTranche) {
            return Niederlassungsstatus.ALL_WITH_FLUECHTLINGSSTATUS.contains(
                gesuchTranche.getGesuchFormular()
                    .getPersonInAusbildung()
                    .getNiederlassungsstatus()
            )
                || gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNationalitaet()
                .is(WellKnownLand.STATELESS);
        }

        private static boolean elternlosOderElternImAusland(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular().getElterns().isEmpty() || gesuchTranche.getGesuchFormular()
                .getElterns()
                .stream()
                .noneMatch(eltern -> eltern.getAdresse().getLand().is(WellKnownLand.CHE));
        }

        private static boolean piaKantonMandantZugewiesen(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNiederlassungsstatus()
                == Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT;
        }

        private static boolean piaNationalitaetEuEfta(
            final GesuchTranche gesuchTranche
        ) {
            return gesuchTranche.getGesuchFormular().getPersonInAusbildung().getNationalitaet().getIsEuEfta();
        }

        private static boolean piaBernWohnhaft(final GesuchTranche gesuchTranche, final PlzService plzService) {
            return plzService.isInBern(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getAdresse());
        }

        private static boolean piaBevormundet(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular().getPersonInAusbildung().isVormundschaft();
        }
    }
}
