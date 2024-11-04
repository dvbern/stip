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

package ch.dvbern.stip.api.plz.service;

import java.util.List;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.plz.repo.PlzRepository;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class PlzService {
    private final PlzRepository plzRepository;
    private final PlzMapper plzMapper;

    @Transactional
    public List<PlzDto> getAllPlz() {
        return plzRepository.findAll().stream().map(plzMapper::toDto).toList();
    }

    public boolean isInBern(final String postleitzahl) {
        if (postleitzahl == null) {
            return false;
        }
        return plzRepository.isPlzInKanton(postleitzahl, "be");
    }

    public boolean isInBern(final Adresse adresse) {
        if (adresse == null) {
            return false;
        }

        final var plz = adresse.getPlz();
        return isInBern(plz);
    }
}
