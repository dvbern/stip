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

package ch.dvbern.stip.api.delegieren.service;

import java.util.UUID;

import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class DelegierenService {
    private final DelegierungRepository delegierungRepository;
    private final FallRepository fallRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final PersoenlicheAngabenMapper persoenlicheAngabenMapper;

    @Transactional
    public void delegateFall(final UUID fallId, final UUID sozialdienstId, final DelegierungCreateDto dto) {
        final var fall = fallRepository.requireById(fallId);
        if (fall.getDelegierung() != null) {
            throw new BadRequestException();
        }

        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var newDelegierung = new Delegierung()
            .setDelegierterFall(fall)
            .setSozialdienst(sozialdienst)
            .setPersoenlicheAngaben(persoenlicheAngabenMapper.toEntity(dto));

        delegierungRepository.persist(newDelegierung);
    }

    @Transactional
    public void delegierterMitarbeiterAendern(final UUID delegierungId, final DelegierterMitarbeiterAendernDto dto) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        final var mitarbeiter = sozialdienstBenutzerRepository.requireById(dto.getMitarbeiterId());

        delegierung.setDelegierterMitarbeiter(mitarbeiter);
    }
}
