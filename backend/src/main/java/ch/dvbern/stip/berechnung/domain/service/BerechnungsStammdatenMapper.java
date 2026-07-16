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

package ch.dvbern.stip.berechnung.domain.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface BerechnungsStammdatenMapper {
    @Mapping(source = "gesuchsperiode.maxSaeule3a", target = "maxSaeule3a")
    @Mapping(source = "gesuchsperiode.einkommensfreibetrag", target = "einkommensfreibetrag")
    @Mapping(source = "gesuchsperiode.limiteEkFreibetragIntegrationszulage", target = "abzugslimite")
    @Mapping(source = "gesuchsperiode.freibetragErwerbseinkommen", target = "freibetragErwerbseinkommen")
    @Mapping(source = "gesuchsperiode.freibetragVermoegen", target = "freibetragVermoegen")
    @Mapping(source = "gesuchsperiode.vermoegensanteilInProzent", target = "vermoegensanteilInProzent")
    @Mapping(source = "gesuchsperiode.anzahlWochenLehre", target = "anzahlWochenLehre")
    @Mapping(source = "gesuchsperiode.preisProMahlzeit", target = "preisProMahlzeit")
    @Mapping(source = "gesuchsperiode.stipLimiteMinimalstipendium", target = "stipLimiteMinimalstipendium")
    @Mapping(
        source = "gesuchsperiode.limiteAlterAntragsstellerHalbierungElternbeitrag",
        target = "limiteAlterAntragsstellerHalbierungElternbeitrag"
    )
    @Mapping(source = "anzahlMonate", target = "anzahlMonate")
    BerechnungsStammdatenDto toDto(
        final Gesuchsperiode gesuchsperiode,
        final Integer anzahlMonate
    );
}
