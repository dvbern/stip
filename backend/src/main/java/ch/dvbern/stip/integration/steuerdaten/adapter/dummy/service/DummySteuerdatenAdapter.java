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

package ch.dvbern.stip.integration.steuerdaten.adapter.dummy.service;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenPortData;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPort;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import ch.dvbern.stip.integration.steuerdaten.domain.service.SteuerdatenAccessService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequestScoped
@SteuerdatenAdapterQualifier(SteuerdatenAdapterType.DUMMY)
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class DummySteuerdatenAdapter implements SteuerdatenPort {

    private final SteuerdatenAccessService steuerdatenAccessService;

    @Override
    public SteuerdatenPortData getSteuerdaten(
        String svn,
        Integer jahr,
        SteuerdatenTyp steuerdatenTyp,
        String fallNr,
        String gesuchNr
    ) {
        steuerdatenAccessService.logAccess(SteuerdatenAdapterType.DUMMY, gesuchNr, fallNr, svn);

        return SteuerdatenPortData.builder()
            .totalEinkuenfte(54347)
            .eigenmietwert(0)
            .isArbeitsverhaeltnisSelbstaendig(false)
            .saeule3a(0)
            .saeule2(0)
            .vermoegen(14152)
            .steuernKantonGemeinde(5019)
            .steuernBund(244)
            .steuerjahr(jahr)
            .veranlagungsStatus("Rechtskraeftig")
            .fahrkosten(700)
            .fahrkostenPartner(0)
            .verpflegung(2025)
            .verpflegungPartner(0)
            .build();
    }
}
