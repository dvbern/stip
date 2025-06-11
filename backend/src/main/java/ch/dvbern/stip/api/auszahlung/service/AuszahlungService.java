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
import ch.dvbern.stip.api.common.authorization.AuszahlungAuthorizer;
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
    private final ZahlungsverbindungService zahlungsverbindungService;
    private final AuszahlungAuthorizer auszahlungAuthorizer;

    @Transactional
    public UUID createAuszahlungForGesuch(UUID fallId, AuszahlungDto auszahlungDto) {
        var fall = fallRepository.requireById(fallId);
        var auszahlung = auszahlungMapper.toEntity(auszahlungDto);

        if (auszahlungDto.getAuszahlungAnSozialdienst().booleanValue() != auszahlung.isAuszahlungAnSozialdienst()) {
            auszahlungAuthorizer.canSetFlag(fallId);
        }

        var zahlungsverbindung = zahlungsverbindungService.createOrGetZahlungsverbindungForAuszahlung(
            fall,
            auszahlungDto.getAuszahlungAnSozialdienst(),
            auszahlungDto.getZahlungsverbindung()
        );
        auszahlung.setZahlungsverbindung(zahlungsverbindung);
        fall.setAuszahlung(auszahlung);
        auszahlungRepository.persistAndFlush(auszahlung);
        return auszahlung.getId();
    }

    @Transactional
    public AuszahlungDto getAuszahlungForGesuch(UUID fallId) {
        return auszahlungMapper.toDto(auszahlungRepository.findAuszahlungByFallId(fallId));
    }

    @Transactional
    public AuszahlungDto updateAuszahlungForGesuch(UUID fallId, AuszahlungUpdateDto auszahlungUpdateDto) {
        checkIfFlagIsReadonly(fallId, auszahlungUpdateDto.getAuszahlungAnSozialdienst());
        var fall = fallRepository.requireById(fallId);
        var auszahlung = fall.getAuszahlung();

        if (
            auszahlungUpdateDto.getAuszahlungAnSozialdienst().booleanValue() != auszahlung.isAuszahlungAnSozialdienst()
        ) {
            auszahlungAuthorizer.canSetFlag(fallId);
        }

        auszahlung.setAuszahlungAnSozialdienst(auszahlungUpdateDto.getAuszahlungAnSozialdienst());
        final var zahlungsverbindung = zahlungsverbindungService.getZahlungsverbindungForAuszahlung(
            fall,
            auszahlung.isAuszahlungAnSozialdienst(),
            auszahlung.getZahlungsverbindung()
        );
        auszahlung.setZahlungsverbindung(zahlungsverbindung);
        return auszahlungMapper.toDto(auszahlung);
    }

    private void checkIfFlagIsReadonly(final UUID fallId, Boolean isAuszahlungAnSozialdienst) {
        if (isAuszahlungAnSozialdienst.booleanValue()) {
            auszahlungAuthorizer.canSetFlag(fallId);
        }
    }
}
