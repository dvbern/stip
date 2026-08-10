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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Niederlassungsstatus {
    SAISONARBEITEND_A("01"),
    AUFENTHALTSBEWILLIGUNG_B("02"),
    NIEDERLASSUNGSBEWILLIGUNG_C("03"),
    PARTNER_ERWERBSTAETIG_UND_KIND_CI("04"),
    VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS("05"),
    VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_TENANT("05"),
    VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON("05"),
    GRENZGAENGIG_G("06"),
    KURZAUFENTHALT_L("07"),
    ASYLSUCHEND_N("08"),
    SCHUTZBEDUERFTIG_S("09"),
    MELDEPFLICHTIG("10"),
    DIPLOMATISCHE_FUNKTION("11"),
    INTERNATIONALE_FUNKTION("12"),
    NICHT_ZUGETEILT("13");

    private final String bfsCode;

    public static final Set<Niederlassungsstatus> STIPENDIENANSPRUCH = EnumSet.of(
        AUFENTHALTSBEWILLIGUNG_B,
        NIEDERLASSUNGSBEWILLIGUNG_C,
        VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON,
        VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_TENANT
    );

    public static final Set<Niederlassungsstatus> ALL_WITH_FLUECHTLINGSSTATUS = EnumSet.of(
        VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_TENANT,
        VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON
    );

}
