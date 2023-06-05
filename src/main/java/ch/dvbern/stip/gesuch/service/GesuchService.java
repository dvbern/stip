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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.gesuch.service;

import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.dto.GesuchDTO;
import ch.dvbern.stip.gesuch.model.QGesuch;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import jakarta.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GesuchService {
    @Inject
    private EntityManager entityManager;

    public Optional<Gesuch> findGesuch(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Gesuch g = entityManager.find(Gesuch.class, id);
        return Optional.ofNullable(g);
    }

    public Gesuch saveGesuch(GesuchDTO gesuchDTO) {
        Gesuch gesuch = findGesuch(gesuchDTO.getId()).orElse(new Gesuch());
        gesuchDTO.apply(gesuch, gesuch.getGesuchsperiode(), gesuch.getPersonInAusbildungContainer());
        return entityManager.merge(gesuch);
    }

    // it doesn't make any sense anymore too much object to map for such a query, this is good for small dtos...
    public Optional<GesuchDTO> findGesuchDTO(UUID id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QGesuch gesuch = new QGesuch("gesuch");

        var query = queryFactory.select(Projections.constructor(GesuchDTO.class,
                        gesuch.id,
                        Projections.constructor(GesuchsperiodeDTO.class,
                                gesuch.gesuchsperiode.id,
                                gesuch.gesuchsperiode.gueltigkeit),
                        null,
                        gesuch.gesuchStatus,
                        gesuch.gesuchNummer
                )).from(gesuch)
                .where(gesuch.id.eq(id));

        return Optional.ofNullable(query.fetchOne());
    }
}
