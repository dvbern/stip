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

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigerKanton;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.stammdaten.service.LandService;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import jakarta.inject.Singleton;

@Singleton
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecider extends BaseStipDecider {
    private final LandService landService;
    private final PlzService plzService;

    public BernStipDecider(
    StipDecisionTextRepository stipDecisionTextRepository, LandService landService, PlzService plzService
    ) {
        super(stipDecisionTextRepository);
        this.landService = landService;
        this.plzService = plzService;
    }

    @Override
    public StipDecision decide(final GesuchTranche gesuchTranche) {
        if (eingabefristAbgelaufen(gesuchTranche)) {
            return StipDecision.EINGABEFRIST_ABGELAUFEN;
        }
        final var stipendienrechtlicherWohnsitzKantonBernResult =
            StipendienrechtlicherWohnsitzKantonBernChecker.evaluate(gesuchTranche, landService, plzService);
        if (stipendienrechtlicherWohnsitzKantonBernResult != StipDecision.GESUCH_VALID) {
            return stipendienrechtlicherWohnsitzKantonBernResult;
        }
        if (ausbildungNichtAnerkannt(gesuchTranche)) {
            return StipDecision.AUSBILDUNG_NICHT_ANERKANNT;
        }
        if (ausbildungImLebenslauf(gesuchTranche)) {
            return StipDecision.AUSBILDUNG_IM_LEBENSLAUF;
        }
        if (ausbildungLaenger12Jahre(gesuchTranche)) {
            return StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE;
        }
        if (piaAelter35Jahre(gesuchTranche)) {
            return StipDecision.PIA_AELTER_35_JAHRE;
        }
        return StipDecision.GESUCH_VALID;
    }

    @Override
    public String getTextForDecision(final StipDecision decision, final Sprache korrespondenzSprache) {
        if (decision.equals(StipDecision.GESUCH_VALID)) {
            return "";
        }
        if (decision.equals(StipDecision.ANSPRUCH_MANUELL_PRUEFEN)) {
            return "ANSPRUCH_MANUELL_PRUEFEN";
        }
        if (decision.equals(StipDecision.ANSPRUCH_UNKLAR)) {
            throw new IllegalStateException("Unkown StipDecision: " + decision);
        }
        return super.getTextForDecision(decision, korrespondenzSprache);
    }

    @Override
    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(StipDecision decision) {
        return switch (decision) {
            case GESUCH_VALID -> GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
            case EINGABEFRIST_ABGELAUFEN -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_NICHT_ANERKANNT -> GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG;
            case AUSBILDUNG_IM_LEBENSLAUF, AUSBILDUNGEN_LAENGER_12_JAHRE, ANSPRUCH_MANUELL_PRUEFEN -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case PIA_AELTER_35_JAHRE -> GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG;
            case NICHT_BERECHTIGTE_PERSON -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case KEIN_WOHNSITZ_KANTON_BE -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            // note: states here are still not reachable
            case SCHULJAHR_9_SEKSTUFE_1 -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case AUSBILDUNG_BPI1 -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ART_32_BBV -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ZWEITAUSBILDUNG -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case EBA_LEHRE_2 -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case HOCHSCHULSTUDIUM_2 -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case MEHRERE_AUSBILDUNGSWECHSEL -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case ANSPRUCH_UNKLAR -> throw new IllegalStateException("Unkown StipDecision: " + decision);
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
                lebenslaufItem -> lebenslaufItem.getBildungsart() != null
            );
    }

    private static boolean ausbildungLaenger12Jahre(final GesuchTranche gesuchTranche) {
        int monthsInAusbildung = gesuchTranche
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .mapToInt(
                lebenslaufItem -> {
                    if (lebenslaufItem.getBildungsart() != null) {
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
        private StipendienrechtlicherWohnsitzKantonBernChecker() {}

        public static StipDecision evaluate(
            final GesuchTranche gesuchTranche,
            final LandService landService,
            final PlzService plzService
        ) {
            final var step1result = evaluateStep1(gesuchTranche, landService);
            if (step1result != StipDecision.ANSPRUCH_UNKLAR) {
                return step1result;
            }
            final var step2result = evaluateStep2(gesuchTranche, landService, plzService);
            if (step2result != StipDecision.ANSPRUCH_UNKLAR) {
                return step2result;
            }
            return evaluateStep3(gesuchTranche, plzService);
        }

        private static StipDecision evaluateStep1(final GesuchTranche gesuchTranche, final LandService landService) {
            if (piaHasSchweizerBuergerrecht(gesuchTranche)) {
                return StipDecision.ANSPRUCH_UNKLAR;
            }
            if (piaIsFluechtling(gesuchTranche)) {
                return StipDecision.ANSPRUCH_UNKLAR;
            }
            if (piaNationalitaetEuEfta(gesuchTranche, landService)) {
                if (piaWohntSchweiz(gesuchTranche)) {
                    return StipDecision.ANSPRUCH_UNKLAR;
                }
                return StipDecision.NICHT_BERECHTIGTE_PERSON;
            }
            if (piaHasNiederlassungsbewilligungC(gesuchTranche)) {
                return StipDecision.ANSPRUCH_UNKLAR;
            }
            if (piaHasNiederlassungsbewilligungB(gesuchTranche) && pia5JahreInChWohnhaft(gesuchTranche)) {
                return StipDecision.ANSPRUCH_UNKLAR;
            }
            return StipDecision.NICHT_BERECHTIGTE_PERSON;
        }

        private static StipDecision evaluateStep2(
            final GesuchTranche gesuchTranche,
            final LandService landService,
            final PlzService plzService
        ) {
            if (piaVolljaehrig(gesuchTranche) && piaBerufsbefaehigendeAusbildungAbeschlossen(gesuchTranche)) {
                return StipDecision.ANSPRUCH_MANUELL_PRUEFEN;
            }
            if (piaFluechtlingOderStaatenlos(gesuchTranche)) {
                if (elternlosOderElternImAusland(gesuchTranche)) {
                    if (piaBernZugewiesen(gesuchTranche)) {
                        return StipDecision.GESUCH_VALID;
                    }
                    return StipDecision.NICHT_BERECHTIGTE_PERSON;
                }
                return StipDecision.ANSPRUCH_UNKLAR;
            }

            if (!piaNationalitaetEuEfta(gesuchTranche, landService)) {
                if (elternlosOderElternImAusland(gesuchTranche)) {
                    if (piaBernWohnhaft(gesuchTranche, plzService)) {
                        return StipDecision.GESUCH_VALID;
                    }
                    return StipDecision.KEIN_WOHNSITZ_KANTON_BE;
                }
                return StipDecision.ANSPRUCH_UNKLAR;
            }
            if (piaHasSchweizerBuergerrecht(gesuchTranche) && elternlosOderElternImAusland(gesuchTranche)) {
                return StipDecision.ANSPRUCH_MANUELL_PRUEFEN;
            }
            return StipDecision.ANSPRUCH_UNKLAR;
        }

        private static StipDecision evaluateStep3(final GesuchTranche gesuchTranche, final PlzService plzService) {
            if (piaBevormundet(gesuchTranche)) {
                return StipDecision.ANSPRUCH_MANUELL_PRUEFEN;
            }
            return evaluateElternWohnsitz(gesuchTranche, plzService);
        }

        private static StipDecision evaluateElternWohnsitz(
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
                return StipDecision.GESUCH_VALID;
            }
            if (noElternInBern == 0) {
                return StipDecision.NICHT_BERECHTIGTE_PERSON;
            }
            return StipDecision.ANSPRUCH_MANUELL_PRUEFEN;
        }

        private static boolean piaHasSchweizerBuergerrecht(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNationalitaet() == Land.CH;
        }

        private static boolean piaIsFluechtling(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNiederlassungsstatus() == Niederlassungsstatus.FLUECHTLING;
        }

        private static boolean piaWohntSchweiz(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular().getPersonInAusbildung().getAdresse().getLand() == Land.CH;
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
                    item -> item.getBildungsart() != null
                    && item.getBildungsart().isBerufsbefaehigenderAbschluss()
                    && item.isAusbildungAbgeschlossen()
                );
        }

        private static boolean piaFluechtlingOderStaatenlos(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getNiederlassungsstatus() == Niederlassungsstatus.FLUECHTLING
            || gesuchTranche.getGesuchFormular().getPersonInAusbildung().getNationalitaet() == Land.STATELESS;
        }

        private static boolean elternlosOderElternImAusland(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular().getElterns().isEmpty() || gesuchTranche.getGesuchFormular()
                .getElterns()
                .stream()
                .noneMatch(eltern -> eltern.getAdresse().getLand() == Land.CH);
        }

        private static boolean piaBernZugewiesen(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular()
                .getPersonInAusbildung()
                .getZustaendigerKanton() == ZustaendigerKanton.BERN;
        }

        private static boolean piaNationalitaetEuEfta(
            final GesuchTranche gesuchTranche,
            final LandService landService
        ) {
            return landService
                .landInEuEfta(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getNationalitaet());
        }

        private static boolean piaBernWohnhaft(final GesuchTranche gesuchTranche, final PlzService plzService) {
            return plzService.isInBern(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getAdresse());
        }

        private static boolean piaBevormundet(final GesuchTranche gesuchTranche) {
            return gesuchTranche.getGesuchFormular().getPersonInAusbildung().isVormundschaft();
        }
    }
}
