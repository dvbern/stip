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
import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.auszahlung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class AuszahlungService {
    private final FallRepository fallRepository;
    private final AuszahlungRepository auszahlungRepository;
    private final AuszahlungMapper auszahlungMapper;
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;

    @Transactional
    public AuszahlungDto createAuszahlungForGesuch(UUID fallId, AuszahlungUpdateDto auszahlungUpdateDto) {
        var fall = fallRepository.requireById(fallId);
        var auszahlung = auszahlungMapper.toEntity(auszahlungUpdateDto);
        zahlungsverbindungRepository.persistAndFlush(auszahlung.getZahlungsverbindung());
        auszahlungRepository.persistAndFlush(auszahlung);
        fall.setAuszahlung(auszahlung);

        var auszahlungDto = auszahlungMapper.toDto(auszahlung);
        auszahlungDto.setIsDelegated(Objects.nonNull(fall.getDelegierung()));
        return auszahlungDto;
    }

    @Transactional
    public AuszahlungDto getAuszahlungForGesuch(UUID fallId) {
        final var fall = fallRepository.requireById(fallId);
        var auszahlungDto = auszahlungMapper.toDto(fall.getAuszahlung());
        auszahlungDto.setIsDelegated(Objects.nonNull(fall.getDelegierung()));
        return auszahlungDto;
    }

    @Transactional
    public AuszahlungDto updateAuszahlungForGesuch(UUID fallId, AuszahlungUpdateDto auszahlungUpdateDto) {
        final var fall = fallRepository.requireById(fallId);

        var auszahlung = auszahlungMapper.partialUpdate(auszahlungUpdateDto, fall.getAuszahlung());

        var auszahlungDto = auszahlungMapper.toDto(auszahlung);
        auszahlungDto.setIsDelegated(Objects.nonNull(fall.getDelegierung()));
        return auszahlungDto;
    }
}
