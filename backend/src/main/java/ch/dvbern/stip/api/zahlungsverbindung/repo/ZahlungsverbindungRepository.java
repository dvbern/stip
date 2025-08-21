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

package ch.dvbern.stip.api.zahlungsverbindung.repo;

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.zahlungsverbindung.entity.QZahlungsverbindung;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ZahlungsverbindungRepository implements BaseRepository<Zahlungsverbindung> {
    private final EntityManager entityManager;
    static final QZahlungsverbindung ZAHLUNGSVERBINDUNG = QZahlungsverbindung.zahlungsverbindung;

    public Stream<Zahlungsverbindung> findZahlungsverbindungsWithPendingSapDelivery() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(ZAHLUNGSVERBINDUNG)
            .where(ZAHLUNGSVERBINDUNG.sapDelivery.sapStatus.eq(SapStatus.IN_PROGRESS))
            .stream();
    }

    public Fall getFallOfZahlungsverbindung(final UUID zahlungsverbindungId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(QFall.fall)
            .where(QFall.fall.auszahlung.zahlungsverbindung.id.eq(zahlungsverbindungId))
            .fetchFirst();
    }
}
