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

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehenDokument;
import ch.dvbern.stip.api.darlehen.entity.QFreiwilligDarlehenDokument;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenDokumentRepository implements BaseRepository<FreiwilligDarlehenDokument> {
    private final EntityManager entityManager;

    public Optional<FreiwilligDarlehenDokument> findByDarlehenIdAndType(
        UUID darlehenId,
        DarlehenDokumentType dokumentType
    ) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var freiwilligDarlehenDokument = QFreiwilligDarlehenDokument.freiwilligDarlehenDokument;
        var query = queryFactory
            .selectFrom(freiwilligDarlehenDokument)
            .where(
                freiwilligDarlehenDokument.freiwilligDarlehen.id.eq(darlehenId)
                    .and(freiwilligDarlehenDokument.dokumentType.eq(dokumentType))
            );
        return query.stream().findFirst();
    }
}
