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

package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Value
@Jacksonized
public class StammdatenV1 {
    private final int maxSaeule3a;
    private final int einkommensfreibetrag;
    private final int abzugslimite;
    private final int freibetragErwerbseinkommen;
    private final int freibetragVermoegen;
    private final int vermoegensanteilInProzent;
    private final int anzahlWochenLehre;
    private final int anzahlWochenSchule;
    private final int preisProMahlzeit;
    private final int stipLimiteMinimalstipendium;
    private final int limiteAlterAntragsstellerHalbierungElternbeitrag;
    private final int anzahlMonate;

    public static StammdatenV1 fromGesuchsperiode(final Gesuchsperiode gesuchsperiode, final int anzahlMonate) {
        return new StammdatenV1Builder()
            .maxSaeule3a(gesuchsperiode.getMaxSaeule3a())
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .abzugslimite(gesuchsperiode.getLimiteEkFreibetragIntegrationszulage())
            .freibetragErwerbseinkommen(gesuchsperiode.getFreibetragErwerbseinkommen())
            .freibetragVermoegen(gesuchsperiode.getFreibetragVermoegen())
            .vermoegensanteilInProzent(gesuchsperiode.getVermoegensanteilInProzent())
            .anzahlWochenLehre(gesuchsperiode.getAnzahlWochenLehre())
            .anzahlWochenSchule(gesuchsperiode.getAnzahlWochenSchule())
            .preisProMahlzeit(gesuchsperiode.getPreisProMahlzeit())
            .stipLimiteMinimalstipendium(gesuchsperiode.getStipLimiteMinimalstipendium())
            .limiteAlterAntragsstellerHalbierungElternbeitrag(
                gesuchsperiode.getLimiteAlterAntragsstellerHalbierungElternbeitrag()
            )
            .anzahlMonate(anzahlMonate)
            .build();
    }
}
