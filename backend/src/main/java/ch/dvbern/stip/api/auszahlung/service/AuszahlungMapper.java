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

import ch.dvbern.stip.api.adresse.repo.AdresseRepository;
import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungDiffUtil;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = AdresseMapper.class)
public abstract class AuszahlungMapper extends EntityUpdateMapper<AuszahlungUpdateDto, Auszahlung> {
    @Inject
    AdresseRepository adresseRepository;

    public abstract Auszahlung toEntity(AuszahlungDto auszahlungDto);

    public abstract AuszahlungDto toDto(Auszahlung auszahlung);

    public abstract Auszahlung partialUpdate(
        AuszahlungUpdateDto auszahlungUpdateDto,
        @MappingTarget Auszahlung auszahlung
    );

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final AuszahlungUpdateDto newAuszahlung,
        @MappingTarget final Auszahlung targetAuszahlung
    ) {
        resetFieldIf(
            () -> AuszahlungDiffUtil.hasAdresseChanged(newAuszahlung, targetAuszahlung),
            "Reset Adresse because ID has changed",
            () -> {
                final var newAdresseId = newAuszahlung.getAdresse().getId();
                if (newAdresseId != null) {
                    targetAuszahlung.setAdresse(adresseRepository.requireById(newAdresseId));
                } else {
                    targetAuszahlung.setAdresse(null);
                }
            }
        );
    }

    public abstract AuszahlungUpdateDto toUpdateDto(Auszahlung auszahlung);
}
