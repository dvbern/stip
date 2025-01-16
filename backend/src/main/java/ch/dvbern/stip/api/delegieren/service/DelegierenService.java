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
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class DelegierenService {
    private final DelegierungRepository delegierungRepository;
    private final GesuchRepository gesuchRepository;
    private final SozialdienstRepository sozialdienstRepository;

    @Transactional
    public void delegateGesuch(final UUID gesuchId, final UUID sozialdienstId) {
        final var fall = gesuchRepository.requireById(gesuchId).getAusbildung().getFall();
        if (fall.getDelegierung() != null) {
            throw new BadRequestException();
        }

        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var newDelegierung = new Delegierung()
            .setDelegierterFall(fall)
            .setSozialdienst(sozialdienst);

        delegierungRepository.persist(newDelegierung);
    }
}
