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

package ch.dvbern.stip.api.demo.repo;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsgang;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.generated.dto.DemoDataDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DemoDataAusbildungsgangRepository implements BaseRepository<Ausbildungsgang> {
    private final EntityManager em;

    private static final QAusbildungsgang Q_AUSBILDUNGSGANG = QAusbildungsgang.ausbildungsgang;

    public final Ausbildungsgang requireAusbildungsgangByDemoData(DemoDataDto demoDataDto) {
        final var ausbildung = demoDataDto.getAusbildung();
        return new JPAQueryFactory(em)
            .selectFrom(Q_AUSBILDUNGSGANG)
            .where(
                Q_AUSBILDUNGSGANG.ausbildungsstaette.nameDe.eq(ausbildung.getAusbildungsstaette())
                    .and(Q_AUSBILDUNGSGANG.abschluss.bezeichnungDe.eq(ausbildung.getAusbildungsgang()))
                    .and(Q_AUSBILDUNGSGANG.aktiv)
            )
            .stream()
            .findFirst()
            .orElseThrow();
    }
}
