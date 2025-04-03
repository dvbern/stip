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

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.api.notiz.type.GesuchNotizTyp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchNotizAuthorizer extends BaseAuthorizer {
    private final GesuchNotizRepository gesuchNotizRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchStatusService gesuchStatusService;

    public void canGet() {
        permitAll();
    }

    @Transactional
    public void canUpdate(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (notiz.getNotizTyp().equals(GesuchNotizTyp.JURISTISCHE_NOTIZ)) {
            forbidden();
        }
    }

    @Transactional
    public void canDelete(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (notiz.getNotizTyp().equals(GesuchNotizTyp.JURISTISCHE_NOTIZ)) {
            forbidden();
        }
        canUpdate(notizId);
    }

    @Transactional
    public void canSetAnswer(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (Objects.nonNull(notiz.getAntwort())) {
            forbidden();
        }
    }

    @Transactional
    public void canCreate(final UUID gesuchId, final GesuchNotizTyp typ) {
        if (typ == GesuchNotizTyp.JURISTISCHE_NOTIZ) {
            final var gesuch = gesuchRepository.requireById(gesuchId);
            if (!gesuchStatusService.canFire(gesuch, GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG)) {
                forbidden();
            }
        }
    }
}
