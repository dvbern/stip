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

package ch.dvbern.stip.api.gesuchtranche.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.entity.QGesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheRepository implements BaseRepository<GesuchTranche> {
    private final EntityManager em;

    private static final QGesuchTranche gesuchTranche = QGesuchTranche.gesuchTranche;

    public Stream<GesuchTranche> findForGesuch(final UUID gesuchId) {
        return new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.gesuch.id.eq(gesuchId))
            .orderBy(gesuchTranche.gueltigkeit.gueltigAb.asc())
            .stream();
    }

    public GesuchTranche requireAenderungById(final UUID aenderungId) {
        final var found = new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.id.eq(aenderungId))
            .fetchFirst();

        if (found != null && found.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            return found;
        }

        throw new NotFoundException();
    }

    public Optional<GesuchTranche> findMostRecentCreatedTranche(final Gesuch gesuch) {
        return new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.gesuch.id.eq(gesuch.getId()))
            .orderBy(gesuchTranche.timestampErstellt.desc())
            .stream()
            .findFirst();
    }

    public List<GesuchTranche> getAllFehlendeDokumente() {
        return new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.status.eq(GesuchTrancheStatus.FEHLENDE_DOKUMENTE))
            .stream()
            .toList();
    }
}
