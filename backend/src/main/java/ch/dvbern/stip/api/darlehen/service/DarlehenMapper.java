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

package ch.dvbern.stip.api.darlehen.service;

import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class DarlehenMapper extends EntityUpdateMapper<DarlehenDto, Darlehen> {
    public abstract DarlehenDto toDto(Darlehen darlehen);

    public abstract Darlehen toEntity(DarlehenDto darlehenDto);

    public abstract Darlehen partialUpdate(DarlehenDto darlehenDto, @MappingTarget Darlehen entity);

    @Override
    @AfterMapping
    protected void resetDependentDataBeforeUpdate(
        final DarlehenDto newDarlehen,
        @MappingTarget final Darlehen targetDarlehen
    ) {
        resetFieldIf(
            () -> Boolean.FALSE.equals(newDarlehen.getWillDarlehen()),
            "Clear Darlehen values because willDarlehen has changed",
            () -> {
                targetDarlehen.setWillDarlehen(false);
                targetDarlehen.setBetragDarlehen(null);
                targetDarlehen.setAnzahlBetreibungen(null);
                targetDarlehen.setSchulden(null);
                targetDarlehen.setBetragBezogenKanton(null);
                targetDarlehen.setGrundAusbildungZwoelfJahre(null);
                targetDarlehen.setGrundHoheGebuehren(null);
                targetDarlehen.setGrundZweitausbildung(null);
                targetDarlehen.setGrundNichtBerechtigt(null);
                targetDarlehen.setGrundAnschaffungenFuerAusbildung(null);
            }
        );
    }
}
