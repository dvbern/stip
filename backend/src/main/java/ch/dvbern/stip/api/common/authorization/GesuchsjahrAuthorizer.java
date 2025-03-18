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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchsjahrAuthorizer extends BaseAuthorizer {
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final GesuchsjahrService gesuchsjahrService;

    public void canGet() {
        permitAll();
    }

    public void canCreate() {
        permitAll();
    }

    @Transactional
    public void canUpdate(final UUID gesuchsjahrId) {
        final var gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
        if (!gesuchsjahrService.isReadonly(gesuchsjahr)) {
            return;
        }

        forbidden();
    }

    public void canPublish() {
        permitAll();
    }

    @Transactional
    public void canDelete(final UUID gesuchsjahrId) {
        final var gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
        if (!gesuchsjahrService.isReadonly(gesuchsjahr)) {
            return;
        }

        forbidden();
    }
}
