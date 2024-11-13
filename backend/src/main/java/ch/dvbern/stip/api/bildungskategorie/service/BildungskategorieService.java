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

package ch.dvbern.stip.api.bildungskategorie.service;

import java.util.List;

import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BildungskategorieService {
    private final BildungskategorieRepository bildungskategorieRepository;
    private final BildungskategorieMapper bildungskategorieMapper;

    @Transactional
    public List<BildungskategorieDto> findAll() {
        return bildungskategorieRepository.findAll().stream().map(bildungskategorieMapper::toDto).toList();
    }
}
