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
import ch.dvbern.stip.stipdecision.entity.StipDecisionTextRepository;
import jakarta.inject.Singleton;

@Singleton
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecider extends BaseStipDecider {
    public BernStipDecider(StipDecisionTextRepository stipDecisionTextRepository) {
        super(stipDecisionTextRepository);
    }

    @Override
    public StipDecision decide(final GesuchTranche gesuchTranche) {
        if (eingabefristAbgelaufen(gesuchTranche)) {
            return StipDecision.EINGABEFRIST_ABGELAUFEN;
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
    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(StipDecision decision) {
        return switch (decision) {
            case GESUCH_VALID -> GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
            case EINGABEFRIST_ABGELAUFEN -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_NICHT_ANERKANNT -> GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG;
            case AUSBILDUNG_IM_LEBENSLAUF, AUSBILDUNGEN_LAENGER_12_JAHRE -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case PIA_AELTER_35_JAHRE -> GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG;
            // note: states here are still not reachable
            case NICHT_EINTRETEN -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case NICHTBERECHTIGTER_PERSONENKREIS -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case KEIN_WOHNSITZ_KANTON_BE -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case SCHULJAHR_9_SEKSTUFE_1 -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case AUSBILDUNG_PBI1 -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ART_32_BBV -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case ZWEITAUSBILDUNG -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case EBA_LEHRE -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case HOCHSCHULSTUDIUM_2 -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case MEHRERE_AUSBILDUNGSWECHSEL -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
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
}
