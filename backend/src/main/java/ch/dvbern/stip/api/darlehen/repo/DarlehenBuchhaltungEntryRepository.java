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

package ch.dvbern.stip.api.darlehen.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.darlehen.entity.DarlehenBuchhaltungEntry;
import ch.dvbern.stip.api.darlehen.entity.QDarlehenBuchhaltungEntry;
import ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenBuchhaltungEntryRepository implements BaseRepository<DarlehenBuchhaltungEntry> {
    private static final QDarlehenBuchhaltungEntry Q_Entry =
        QDarlehenBuchhaltungEntry.darlehenBuchhaltungEntry;

    private final EntityManager entityManager;

    public List<DarlehenBuchhaltungEntry> getByGesuchId(final UUID gesuchId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_Entry)
            .where(Q_Entry.gesuch.id.eq(gesuchId))
            .stream()
            .toList()
            .reversed();
    }

    public Optional<DarlehenBuchhaltungEntry> getByVerfuegungDokumentId(final UUID verfuegungDokumentId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_Entry)
            .where(
                Q_Entry.verfuegung.id.eq(verfuegungDokumentId)
            )
            .stream()
            .findFirst();
    }

    public List<DarlehenBuchhaltungEntry> findByYear(int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_Entry)
            .from(Q_Entry)
            .where(
                Q_Entry.timestampErstellt.between(startOfYear, endOfYear)
                    .and(Q_Entry.kategorie.in(DarlehenBuchhaltungEntryKategorie.DARLEHEN))
            )
            .fetch();
    }
}
