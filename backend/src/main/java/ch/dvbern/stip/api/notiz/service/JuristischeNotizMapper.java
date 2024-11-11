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

package ch.dvbern.stip.api.notiz.service;

import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.notiz.entity.JuristischeAbklaerungNotiz;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class JuristischeNotizMapper extends EntityUpdateMapper<GesuchNotizDto, GesuchNotiz> {
    abstract JuristischeAbklaerungNotiz toEntity(GesuchNotizCreateDto dto);

    abstract JuristischeAbklaerungNotiz partialUpdate(
        JuristischeAbklaerungNotizAntwortDto dto,
        @MappingTarget JuristischeAbklaerungNotiz entity
    );

    abstract GesuchNotizDto toDto(JuristischeAbklaerungNotiz entity);

    abstract GesuchNotizDto toDto(GesuchNotiz entity);

    @AfterMapping
    public void mapAntwortIfJuristischeNotiz(GesuchNotiz entity, @MappingTarget GesuchNotizDto dto) {
        if (entity instanceof JuristischeAbklaerungNotiz juristischeAbklaerungNotiz) {
            dto.setAntwort(juristischeAbklaerungNotiz.getAntwort());
        }
    }
}
