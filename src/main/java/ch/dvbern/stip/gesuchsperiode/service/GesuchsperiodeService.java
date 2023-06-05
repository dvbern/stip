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

package ch.dvbern.stip.gesuchsperiode.service;

import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import ch.dvbern.stip.gesuchsperiode.model.Gesuchsperiode;
import ch.dvbern.stip.gesuchsperiode.model.QGesuchsperiode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GesuchsperiodeService {


    @Inject
    private EntityManager entityManager;

    public Optional<Gesuchsperiode> findGesuchsperiode(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Gesuchsperiode gp = entityManager.find(Gesuchsperiode.class, id);
        return Optional.ofNullable(gp);
    }

    public Gesuchsperiode saveGesuchsperiode(GesuchsperiodeDTO gesuchsperiodeDTO) {
        Gesuchsperiode gesuchsperiode = findGesuchsperiode(gesuchsperiodeDTO.getId()).orElse(new Gesuchsperiode());
        gesuchsperiodeDTO.apply(gesuchsperiode);
        return entityManager.merge(gesuchsperiode);
    }

    public Optional<List<GesuchsperiodeDTO>> findAlleAktiveGesuchsperiodeDTO() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QGesuchsperiode gesuchsperiode = new QGesuchsperiode("gesuchsperiode");

        var query = queryFactory.select(Projections.constructor(GesuchsperiodeDTO.class,
                gesuchsperiode.id,
                gesuchsperiode.gueltigkeit.gueltigAb,
                gesuchsperiode.gueltigkeit.gueltigBis,
                gesuchsperiode.einreichfrist,
                gesuchsperiode.aufschaltdatum
            )).from(gesuchsperiode);

        return Optional.ofNullable(query.fetch());
    }
}
