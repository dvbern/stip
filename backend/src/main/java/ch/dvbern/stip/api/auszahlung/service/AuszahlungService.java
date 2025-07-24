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

import ch.dvbern.stip.api.auszahlung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sap.service.SapService;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.FallAuszahlungDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class AuszahlungService {
    private final FallRepository fallRepository;
    private final AuszahlungMapper auszahlungMapper;
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;
    private final BuchhaltungService buchhaltungService;
    private final SapService sapService;

    @Transactional
    public FallAuszahlungDto createAuszahlungForGesuch(UUID fallId, AuszahlungUpdateDto auszahlungUpdateDto) {
        var fall = fallRepository.requireById(fallId);
        var auszahlung = auszahlungMapper.toEntity(auszahlungUpdateDto);
        fall.setAuszahlung(auszahlung);

        return auszahlungMapper.toDto(fall);
    }

    @Transactional
    public FallAuszahlungDto getAuszahlungForGesuch(UUID fallId) {
        final var fall = fallRepository.requireById(fallId);
        return auszahlungMapper.toDto(fall);
    }

    @Transactional
    public FallAuszahlungDto updateAuszahlungForGesuch(UUID fallId, AuszahlungUpdateDto auszahlungUpdateDto) {
        final var fall = fallRepository.requireById(fallId);
        final var zahlungsverbindung = fall.getAuszahlung().getZahlungsverbindung();

        if (auszahlungUpdateDto.getAuszahlungAnSozialdienst() && Objects.nonNull(zahlungsverbindung)) {
            zahlungsverbindungRepository.delete(zahlungsverbindung);
        }

        auszahlungMapper.partialUpdate(auszahlungUpdateDto, fall.getAuszahlung());
        if (fall.getAuszahlung().getZahlungsverbindung() != null) {
            fall.getAuszahlung().getZahlungsverbindung().setSapDelivery(null);
            fall.getAuszahlung().getZahlungsverbindung().setSapBusinessPartnerId(null);
        }

        if (buchhaltungService.canRetryAuszahlungBuchhaltung(fall)) {
            sapService.retryAuszahlungBuchhaltung(fall);
        }

        return auszahlungMapper.toDto(fall);
    }
}
