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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.type;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum Gesuchstatus {
    IN_BEARBEITUNG_GS,
    KOMPLETT_EINGEREICHT,
    BEREIT_FUER_BEARBEITUNG,
    FEHLERHAFT,
    IN_BEARBEITUNG_SB,
    IN_REVIEW,
    FEHLENDE_DOKUMENTE,
    FEHLENDE_DOKUMENTE_NACHFRIST,
    ZURUECKGEZOGEN,
    ABKLAERUNG_MIT_GS,
    IN_FREIGABE,
    ZURUECKGEWIESEN,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    NEGATIVER_ENTSCHEID,
    VERFUEGT,
    STIPENDIUM_AKZEPTIERT,
    STIPENDIUM_AUSBEZAHLT;

    public static final Set<Gesuchstatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(EnumSet.of(
        IN_BEARBEITUNG_GS,
        KOMPLETT_EINGEREICHT,
        ABKLAERUNG_MIT_GS,
        FEHLENDE_DOKUMENTE,
        FEHLENDE_DOKUMENTE_NACHFRIST
    ));

    public static final Set<Gesuchstatus> SACHBEARBEITER_CAN_EDIT = Collections.unmodifiableSet(EnumSet.of(
        BEREIT_FUER_BEARBEITUNG,
        FEHLERHAFT,
        IN_BEARBEITUNG_SB,
        ZURUECKGEWIESEN,
        IN_REVIEW,
        IN_FREIGABE,
        WARTEN_AUF_UNTERSCHRIFTENBLATT,
        VERFUEGT,
        STIPENDIUM_AKZEPTIERT,
        STIPENDIUM_AUSBEZAHLT
    ));

    public static final Set<Gesuchstatus> ADMIN_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.copyOf(SACHBEARBEITER_CAN_EDIT)
    );

    public boolean isEingereicht() {
        return this != IN_BEARBEITUNG_GS;
    }
}
