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

import ch.dvbern.stip.berechnung.dto.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import jakarta.inject.Singleton;

@Singleton
@CalculatorVersion(major = 1, minor = 0)
public class BerechnungsStammdatenV1Mapper implements BerechnungsStammdatenMapper {
    @Override
    public BerechnungsStammdatenDto mapFromRequest(CalculatorRequest request) {
        final BerechnungRequestV1 berechnungsRequest = (BerechnungRequestV1) request;

        return new BerechnungsStammdatenDto()
            .maxSaeule3a(berechnungsRequest.getStammdaten().getMaxSaeule3a())
            .einkommensfreibetrag(berechnungsRequest.getStammdaten().getEinkommensfreibetrag())
            .abzugslimite(berechnungsRequest.getStammdaten().getAbzugslimite())
            .freibetragErwerbseinkommen(berechnungsRequest.getStammdaten().getFreibetragErwerbseinkommen())
            .freibetragVermoegen(berechnungsRequest.getStammdaten().getFreibetragVermoegen())
            .vermoegensanteilInProzent(berechnungsRequest.getStammdaten().getVermoegensanteilInProzent())
            .anzahlWochenLehre(berechnungsRequest.getStammdaten().getAnzahlWochenLehre())
            .anzahlWochenSchule(berechnungsRequest.getStammdaten().getAnzahlWochenSchule())
            .preisProMahlzeit(berechnungsRequest.getStammdaten().getPreisProMahlzeit())
            .stipLimiteMinimalstipendium(berechnungsRequest.getStammdaten().getStipLimiteMinimalstipendium())
            .limiteAlterAntragsstellerHalbierungElternbeitrag(
                berechnungsRequest.getStammdaten().getLimiteAlterAntragsstellerHalbierungElternbeitrag()
            )
            .anzahlMonate(berechnungsRequest.getStammdaten().getAnzahlMonate());
    }
}
