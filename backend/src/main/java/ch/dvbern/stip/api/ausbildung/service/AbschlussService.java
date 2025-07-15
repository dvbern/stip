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

package ch.dvbern.stip.api.ausbildung.service;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.repo.AbschlussRepository;
import ch.dvbern.stip.api.ausbildung.type.AbschlussSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.BrueckenangebotCreateDto;
import ch.dvbern.stip.generated.dto.PaginatedAbschlussDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AbschlussService {
    private final AbschlussRepository abschlussRepository;
    private final AbschlussMapper abschlussMapper;
    private final ConfigService configService;

    @Transactional
    public AbschlussDto createAbschlussBrueckenangebot(final BrueckenangebotCreateDto brueckenangebotCreateDto) {
        final var brueckenangebot = abschlussMapper.createBrueckenangebot(brueckenangebotCreateDto);
        abschlussRepository.persist(brueckenangebot);
        return abschlussMapper.toDto(brueckenangebot);
    }

    public PaginatedAbschlussDto getAllAbschlussForUebersicht(
        final Integer page,
        final Integer pageSize,
        final AbschlussSortColumn sortColumn,
        final SortOrder sortOrder,
        final Ausbildungskategorie ausbildungskategorie,
        final Bildungsrichtung bildungsrichtung,
        final String bezeichnungDe,
        final String bezeichnungFr,
        final Boolean aktiv
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = abschlussRepository.baseQuery();

        if (Objects.nonNull(ausbildungskategorie)) {
            abschlussRepository.ausbildungskategorieFilter(baseQuery, ausbildungskategorie);
        }
        if (Objects.nonNull(bildungsrichtung)) {
            abschlussRepository.bildungsrichtungFilter(baseQuery, bildungsrichtung);
        }
        if (Objects.nonNull(bezeichnungDe)) {
            abschlussRepository.bezeichnungDeFilter(baseQuery, bezeichnungDe);
        }
        if (Objects.nonNull(bezeichnungFr)) {
            abschlussRepository.bezeichnungFrFilter(baseQuery, bezeichnungFr);
        }
        if (Objects.nonNull(aktiv)) {
            abschlussRepository.aktivFilter(baseQuery, aktiv);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = abschlussRepository.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            abschlussRepository.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            abschlussRepository.defaultOrder(baseQuery);
        }

        abschlussRepository.paginate(baseQuery, page, pageSize);

        final var results = baseQuery.stream()
            .map(abschlussMapper::toDto)
            .toList();

        return new PaginatedAbschlussDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }

    @Transactional
    public AbschlussDto setAbschlussInaktiv(final UUID abschlussId) {
        final var abschluss = abschlussRepository.requireById(abschlussId);
        abschluss.setAktiv(false);
        return abschlussMapper.toDto(abschluss);
    }

}
