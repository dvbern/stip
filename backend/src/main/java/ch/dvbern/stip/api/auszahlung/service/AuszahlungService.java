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

import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.auszahlung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class AuszahlungService {
    private final GesuchRepository gesuchRepository;
    private final AuszahlungRepository auszahlungRepository;
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;
    private final AuszahlungMapper auszahlungMapper;

    @Transactional
    public UUID createAuszahlungForGesuch(UUID gesuchId, AuszahlungDto auszahlungDto) {
        var auszahlung = auszahlungMapper.toEntity(auszahlungDto);
        var fall = getFallOfGesuch(gesuchId);
        fall.setAuszahlung(auszahlung);
        // todo: add if else
        zahlungsverbindungRepository.persistAndFlush(auszahlung.getZahlungsverbindung());
        auszahlungRepository.persistAndFlush(auszahlung);
        return auszahlung.getId();
    }

    @Transactional
    public AuszahlungDto getAuszahlungForGesuch(UUID gesuchId) {
        // todo: add if else
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fallId = gesuch.getAusbildung().getFall().getId();
        return auszahlungMapper.toDto(auszahlungRepository.findAuszahlungByFallId(fallId));
    }

    @Transactional
    public AuszahlungDto updateAuszahlungForGesuch(UUID gesuchId, AuszahlungUpdateDto auszahlungUpdateDto) {
        // todo : resetDependentDataBeforeUpdate required?
        // todo: add if else
        var fall = getFallOfGesuch(gesuchId);
        var auszahlung = fall.getAuszahlung();
        auszahlungMapper.partialUpdate(auszahlungUpdateDto, auszahlung);
        return auszahlungMapper.toDto(auszahlung);
    }

    private Fall getFallOfGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuch.getAusbildung().getFall();
    }
}
