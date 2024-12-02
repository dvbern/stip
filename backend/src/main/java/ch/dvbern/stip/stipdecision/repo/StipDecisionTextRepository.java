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

package ch.dvbern.stip.stipdecision.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.stipdecision.entity.QStipDecisionText;
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class StipDecisionTextRepository implements BaseRepository<StipDecisionText> {
    private final EntityManager entityManager;

    public StipDecisionText getTextByStipDecision(StipDecision stipDecision) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var stipDecisionText = QStipDecisionText.stipDecisionText;
        return queryFactory.selectFrom(stipDecisionText)
            .where(stipDecisionText.stipDecision.eq(stipDecision))
            .fetchOne();
    }
}
