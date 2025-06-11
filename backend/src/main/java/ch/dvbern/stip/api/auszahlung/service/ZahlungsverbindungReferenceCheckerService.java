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

import ch.dvbern.stip.api.fall.repo.FallRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class ZahlungsverbindungReferenceCheckerService {
    private final FallRepository fallRepository;

    public boolean isCurrentZahlungsverbindungOfDelegatedSozialdienst(UUID fallId) {
        final var fall = fallRepository.requireById(fallId);
        if (Objects.nonNull(fall.getDelegierung())) {
            final var sozialdienst = fall.getDelegierung().getSozialdienst();
            return sozialdienst.getZahlungsverbindung() == fall.getAuszahlung().getZahlungsverbindung();
        }
        return false;
    }
}
