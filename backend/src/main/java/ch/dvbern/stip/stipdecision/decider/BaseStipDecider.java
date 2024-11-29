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

import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.stipdecision.entity.StipDecisionTextRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseStipDecider {
    private final StipDecisionTextRepository stipDecisionTextRepository;

    public abstract StipDecision decide(final GesuchTranche gesuchTranche);

    public String getTextForDecision(final StipDecision decision, final Sprache korrespondenzSprache) {
        if (decision.equals(StipDecision.GESUCH_VALID)) {
            return "";
        } else {
            final var decisionText = stipDecisionTextRepository.getTextByStipDecision(decision);
            return switch (korrespondenzSprache) {
                case FRANZOESISCH -> decisionText.getTextFr();
                case DEUTSCH -> decisionText.getTextDe();
            };
        }
    }

    public abstract GesuchStatusChangeEvent getGesuchStatusChangeEvent(final StipDecision decision);
}
