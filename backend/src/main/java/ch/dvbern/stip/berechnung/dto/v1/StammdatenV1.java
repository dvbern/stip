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
    int maxSaeule3a;
    int einkommensfreibetrag;
    int abzugslimite;
    int freibetragErwerbseinkommen;
    int freibetragVermoegen;
    int vermoegensanteilInProzent;
    int anzahlWochenLehre;
    int anzahlWochenSchule;
    int preisProMahlzeit;
    int stipLimiteMinimalstipendium;
    int limiteAlterAntragsstellerHalbierungElternbeitrag;

    public static StammdatenV1 fromGesuchsperiode(final Gesuchsperiode gesuchsperiode) {
        return new StammdatenV1Builder()
            .maxSaeule3a(gesuchsperiode.getMaxSaeule3a())
            .abzugslimite(gesuchsperiode.getLimiteEkFreibetragIntegrationszulage())
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .freibetragErwerbseinkommen(gesuchsperiode.getFreibetragErwerbseinkommen())
            .freibetragVermoegen(gesuchsperiode.getFreibetragVermoegen())
            .vermoegensanteilInProzent(gesuchsperiode.getVermoegensanteilInProzent())
            .anzahlWochenLehre(gesuchsperiode.getAnzahlWochenLehre())
            .anzahlWochenSchule(gesuchsperiode.getAnzahlWochenSchule())
            .preisProMahlzeit(gesuchsperiode.getPreisProMahlzeit())
            .stipLimiteMinimalstipendium(0) // set to zero as we don't apply this limit in the DMN model TODO: remove
                                            // from model
            .limiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .build();
    }
}
