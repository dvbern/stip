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
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecider implements BaseStipDecider {
    private final LandService landService;
    private final PlzService plzService;

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
        return switch (decision) {
            case GESUCH_VALID -> "";
            case EINGABEFRIST_ABGELAUFEN -> getEingabeFristAbgelaufenText(korrespondenzSprache).render();
            case AUSBILDUNG_NICHT_ANERKANNT -> getAusbildungNichtAnerkanntText(korrespondenzSprache).render();
            case AUSBILDUNG_IM_LEBENSLAUF -> getAusbildungImLebenslaufText(korrespondenzSprache).render();
            case AUSBILDUNGEN_LAENGER_12_JAHRE -> getAusbildungLaenger12JahreText(korrespondenzSprache).render();
            case PIA_AELTER_35_JAHRE -> getPiaAelter35JahreText(korrespondenzSprache).render();
            case ANSPRUCH_MANUELL_PRUEFEN -> "ANSPRUCH_MANUELL_PRUEFEN";
            case NICHT_BERECHTIGTE_PERSON -> "NICHT_BERECHTIGTE_PERSON";
            case ANSPRUCH_UNKLAR -> throw new IllegalStateException("Unkown StipDecision: " + decision);
        };
    }

    @Override
    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(StipDecision decision) {
        return switch (decision) {
            case GESUCH_VALID -> GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
            case EINGABEFRIST_ABGELAUFEN, NICHT_BERECHTIGTE_PERSON -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_NICHT_ANERKANNT -> GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG;
            case AUSBILDUNG_IM_LEBENSLAUF, AUSBILDUNGEN_LAENGER_12_JAHRE, ANSPRUCH_MANUELL_PRUEFEN -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case PIA_AELTER_35_JAHRE -> GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG;
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

    private static TemplateInstance getEingabeFristAbgelaufenText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.eingabefristAbgelaufenFr();
            case DEUTSCH -> Templates.eingabefristAbgelaufenDe();
        };
    }

    private static TemplateInstance getAusbildungNichtAnerkanntText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungNichtAnerkanntFr();
            case DEUTSCH -> Templates.ausbildungNichtAnerkanntDe();
        };
    }

    private static TemplateInstance getAusbildungLaenger12JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungLaenger12JahreFr();
            case DEUTSCH -> Templates.ausbildungLaenger12JahreDe();
        };
    }

    private static TemplateInstance getAusbildungImLebenslaufText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungImLebenslaufFr();
            case DEUTSCH -> Templates.ausbildungImLebenslaufDe();
        };
    }

    private static TemplateInstance getPiaAelter35JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.piaAelter35JahreFr();
            case DEUTSCH -> Templates.piaAelter35JahreDe();
        };
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
                    return StipDecision.NICHT_BERECHTIGTE_PERSON;
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

    @CheckedTemplate
    static final class Templates {
        private Templates() {}

        public static native TemplateInstance eingabefristAbgelaufenDe();

        public static native TemplateInstance eingabefristAbgelaufenFr();

        public static native TemplateInstance ausbildungNichtAnerkanntDe();

        public static native TemplateInstance ausbildungNichtAnerkanntFr();

        public static native TemplateInstance ausbildungLaenger12JahreDe();

        public static native TemplateInstance ausbildungLaenger12JahreFr();

        public static native TemplateInstance ausbildungImLebenslaufDe();

        public static native TemplateInstance ausbildungImLebenslaufFr();

        public static native TemplateInstance piaAelter35JahreDe();

        public static native TemplateInstance piaAelter35JahreFr();
    }
}
