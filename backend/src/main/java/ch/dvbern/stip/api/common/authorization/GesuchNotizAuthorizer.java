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

import ch.dvbern.stip.api.notiz.entity.NotizTyp;
import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchNotizAuthorizer extends BaseAuthorizer {
    private final GesuchNotizRepository gesuchNotizRepository;

    public void canUpdate(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (notiz.getNotizTyp().equals(NotizTyp.JURISTISCHE_NOTIZ)) {
            throw new ForbiddenException();
        }
    }

    public void canDelete(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (notiz.getNotizTyp().equals(NotizTyp.JURISTISCHE_NOTIZ)) {
            throw new ForbiddenException();
        }
        canUpdate(notizId);
    }

    public void canSetAnswer(UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        if (Objects.nonNull(notiz.getAntwort())) {
            throw new ForbiddenException();
        }
    }
}