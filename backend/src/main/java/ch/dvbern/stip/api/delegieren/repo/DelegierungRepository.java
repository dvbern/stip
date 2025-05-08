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

package ch.dvbern.stip.api.delegieren.repo;

import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.entity.QDelegierung;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DelegierungRepository implements BaseRepository<Delegierung> {
    private final EntityManager entityManager;
    private static final QDelegierung qDelegierung = QDelegierung.delegierung;

    public JPAQuery<Delegierung> getFindAlleMeineQuery(
        final SozialdienstBenutzer sozialdienstBenutzer,
        final UUID sozialdienstId
    ) {
        return addMeineFilter(sozialdienstBenutzer, getFindAlleOfSozialdienstQuery(sozialdienstId));
    }

    public JPAQuery<Delegierung> getFindAlleOfSozialdienstQuery(UUID sozialdienstId) {
        return addOfSozialdienstFilter(sozialdienstId, new JPAQueryFactory(entityManager).selectFrom(qDelegierung));
    }

    private JPAQuery<Delegierung> addOfSozialdienstFilter(
        final UUID sozialdienstId,
        final JPAQuery<Delegierung> query
    ) {
        query.where(qDelegierung.sozialdienst.id.eq(sozialdienstId));
        return query;
    }

    private JPAQuery<Delegierung> addMeineFilter(
        final SozialdienstBenutzer sozialdienstBenutzer,
        final JPAQuery<Delegierung> query
    ) {

        query.where(qDelegierung.delegierterMitarbeiter.id.eq(sozialdienstBenutzer.getId()));
        return query;
    }
}
