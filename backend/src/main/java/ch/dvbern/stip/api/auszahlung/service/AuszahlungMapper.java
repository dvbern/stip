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

package ch.dvbern.stip.api.auszahlung.service;

import java.util.Objects;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { ZahlungsverbindungMapper.class })
public abstract class AuszahlungMapper extends EntityUpdateMapper<AuszahlungUpdateDto, Auszahlung> {
    @Inject
    FallRepository fallRepository;

    public abstract Auszahlung toEntity(AuszahlungUpdateDto auszahlungUpdateDto);

    public abstract AuszahlungDto toDto(Auszahlung auszahlung);

    public abstract Auszahlung partialUpdate(
        AuszahlungUpdateDto auszahlungUpdateDto,
        @MappingTarget Auszahlung auszahlung
    );

    public abstract AuszahlungUpdateDto toUpdateDto(Auszahlung auszahlung);

    @AfterMapping
    protected void setIsDelegatedFlag(final Auszahlung auszahlung, @MappingTarget AuszahlungDto auszahlungDto) {
        final var fall = fallRepository.findByAuszahlungId(auszahlung.getId());
        auszahlungDto.setIsDelegated(Objects.nonNull(fall.getDelegierung()));
    }
}
