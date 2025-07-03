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

package ch.dvbern.stip.api.personinausbildung.type;

import java.util.EnumSet;
import java.util.Set;

public enum Niederlassungsstatus {
    SAISONARBEITEND_A,
    AUFENTHALTSBEWILLIGUNG_B,
    NIEDERLASSUNGSBEWILLIGUNG_C,
    PARTNER_ERWERBSTAETIG_UND_KIND_CI, // PERSON_NIEDERLASSUNGSSTATUS_PARTNER_ERWERBSTAETIG_UND_KIND_CI
    VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS,
    VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT,
    VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON,
    GRENZGAENGIG_G,
    KURZAUFENTHALT_L,
    ASYLSUCHEND_N,
    SCHUTZBEDUERFTIG_S,
    MELDEPFLICHTIG,
    DIPLOMATISCHE_FUNKTION,
    INTERNATIONALE_FUNKTION,
    NICHT_ZUGETEILT;

    public static final Set<Niederlassungsstatus> STIPENDIENANSPRUCH = EnumSet.of(
        AUFENTHALTSBEWILLIGUNG_B,
        NIEDERLASSUNGSBEWILLIGUNG_C,
        VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON,
        VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT
    );

    public static final Set<Niederlassungsstatus> ALL_WITH_FLUECHTLINGSSTATUS = EnumSet.of(
        VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT,
        VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON
    );

}
