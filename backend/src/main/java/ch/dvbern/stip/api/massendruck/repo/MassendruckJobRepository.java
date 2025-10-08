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

package ch.dvbern.stip.api.massendruck.repo;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.massendruck.entity.MassendruckJob;
import ch.dvbern.stip.api.massendruck.entity.QDatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.QMassendruckJob;
import ch.dvbern.stip.api.massendruck.entity.QVerfuegungMassendruck;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobRepository implements BaseRepository<MassendruckJob> {
    private final EntityManager entityManager;

    public void deleteMassendruckJobById(final UUID massendruckId) {
        final var datenschutzbriefMassendruck = QDatenschutzbriefMassendruck.datenschutzbriefMassendruck;
        new JPAQueryFactory(entityManager)
            .delete(datenschutzbriefMassendruck)
            .where(datenschutzbriefMassendruck.massendruckJob.id.eq(massendruckId))
            .execute();

        final var verfuegungMassendruck = QVerfuegungMassendruck.verfuegungMassendruck;
        new JPAQueryFactory(entityManager)
            .delete(verfuegungMassendruck)
            .where(verfuegungMassendruck.massendruckJob.id.eq(massendruckId))
            .execute();

        final var massendruckJob = QMassendruckJob.massendruckJob;
        new JPAQueryFactory(entityManager)
            .delete(massendruckJob)
            .where(massendruckJob.id.eq(massendruckId))
            .execute();
    }

    public Optional<MassendruckJob> getDatenschutzMassendruckJobForGesuchId(final UUID gesuchId) {
        final var massendruckJob = QMassendruckJob.massendruckJob;

        return new JPAQueryFactory(entityManager)
            .selectFrom(massendruckJob)
            .where(massendruckJob.datenschutzbriefMassendrucks.any().datenschutzbrief.gesuch.id.eq(gesuchId))
            .stream()
            .findFirst();
    }
}
