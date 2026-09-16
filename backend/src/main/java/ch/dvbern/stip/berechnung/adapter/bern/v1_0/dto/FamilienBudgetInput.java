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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0.dto;

import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;

public record FamilienBudgetInput(
SteuerdatenTyp steuerdatenTyp,
List<Eltern> elterns,
Steuerdaten steuerdaten,
Steuererklaerung steuererklaerung,
Gesuchsperiode gesuchsperiode,
List<AbstractFamilieEntity> kinderImHaushalt,
int anzahlKinderDerElternInAusbildung
) {
    public FamilienBudgetInput {
        if (
            steuerdatenTyp != steuerdaten.getSteuerdatenTyp()
            || steuerdatenTyp != steuererklaerung.getSteuerdatenTyp()
        ) {
            throw new IllegalStateException("SteuerdatenTyps need to match");
        }
    }
}
