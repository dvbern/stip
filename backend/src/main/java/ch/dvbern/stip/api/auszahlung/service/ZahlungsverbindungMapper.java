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
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.auszahlung.util.ZahlungsverbindungDiffUtil;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { AdresseMapper.class })

public abstract class ZahlungsverbindungMapper
extends EntityUpdateMapper<ZahlungsverbindungUpdateDto, Zahlungsverbindung> {
    @Inject
    AdresseRepository adresseRepository;

    public abstract Zahlungsverbindung toEntity(ZahlungsverbindungDto zahlungsverbindungDto);

    public abstract ZahlungsverbindungDto toDto(Zahlungsverbindung zahlungsverbindung);

    public abstract Zahlungsverbindung partialUpdate(
        final ZahlungsverbindungDto dto,
        @MappingTarget Zahlungsverbindung zahlungsverbindung
    );

    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final ZahlungsverbindungDto newZahlungsverbindung,
        @MappingTarget final Zahlungsverbindung targetZahlungsverbindung
    ) {
        resetFieldIf(
            () -> ZahlungsverbindungDiffUtil.hasAdresseChanged(newZahlungsverbindung, targetZahlungsverbindung),
            "Reset Adresse because ID has changed",
            () -> {
                final var newAdresseId = newZahlungsverbindung.getAdresse().getId();
                if (newAdresseId != null) {
                    targetZahlungsverbindung.setAdresse(adresseRepository.requireById(newAdresseId));
                } else {
                    targetZahlungsverbindung.setAdresse(null);
                }
            }
        );
    }

}
