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

package ch.dvbern.stip.api.admindokumente.service;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.massendruck.service.MassendruckJobService;
import ch.dvbern.stip.api.verfuegung.service.AdminDokumenteMapper;
import ch.dvbern.stip.generated.dto.AdminDokumenteDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AdminDokumenteService {
    private final GesuchRepository gesuchRepository;
    private final MassendruckJobService massendruckJobService;
    private final AdminDokumenteMapper adminDokumenteMapper;

    public AdminDokumenteDto getAdminDokumente(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var verfuegungen = gesuch.getVerfuegungs()
            .stream()
            .filter(verfuegung -> verfuegung.getObjectId() != null)
            .toList();

        final var datenschutzbriefMassendruck = massendruckJobService.getDatenschutzMassendruckJobForGesuchId(gesuchId);
        return adminDokumenteMapper.toDto(verfuegungen, datenschutzbriefMassendruck.orElse(null));
    }
}
