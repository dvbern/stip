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

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.auszahlung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class ZahlungsverbindungService {
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;
    private final ZahlungsverbindungMapper zahlungsverbindungMapper;

    @Transactional
    public Zahlungsverbindung createZahlungsverbindung(final ZahlungsverbindungDto dto) {
        final var zahlungsverbindung = zahlungsverbindungMapper.toEntity(dto);
        zahlungsverbindungRepository.persist(zahlungsverbindung);
        return zahlungsverbindung;
    }

}
