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
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsgangSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangService {
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final AbschlussRepository abschlussRepository;
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangMapper ausbildungsgangMapper;
    private final ConfigService configService;

    @Transactional
    public AusbildungsgangDto createAusbildungsgang(final AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        final var ausbildungsgang = ausbildungsgangMapper.toEntity(ausbildungsgangCreateDto);
        final var abschluss = abschlussRepository.requireById(ausbildungsgangCreateDto.getAbschlussId());
        final var ausbildungsstaette =
            ausbildungsstaetteRepository.requireById(ausbildungsgangCreateDto.getAusbildungsstaetteId());
        ausbildungsgang.setAbschluss(abschluss);
        ausbildungsgang.setAusbildungsstaette(ausbildungsstaette);
        ausbildungsgangRepository.persist(ausbildungsgang);
        return ausbildungsgangMapper.toDto(ausbildungsgang);
    }

    public PaginatedAusbildungsgangDto getAllAusbildungsgangForUebersicht(
        final Integer page,
        final Integer pageSize,
        final AusbildungsgangSortColumn sortColumn,
        final SortOrder sortOrder,
        final String abschlussBezeichnungDe,
        final String abschlussBezeichnungFr,
        final Ausbildungskategorie ausbildungskategorie,
        final String ausbildungsstaetteNameDe,
        final String ausbildungsstaetteNameFr,
        final Boolean aktiv
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = ausbildungsgangRepository.baseQuery();

        if (Objects.nonNull(abschlussBezeichnungDe)) {
            ausbildungsgangRepository.abschlussBezeichnungDeFilter(baseQuery, abschlussBezeichnungDe);
        }
        if (Objects.nonNull(abschlussBezeichnungFr)) {
            ausbildungsgangRepository.abschlussBezeichnungFrFilter(baseQuery, abschlussBezeichnungFr);
        }
        if (Objects.nonNull(ausbildungskategorie)) {
            ausbildungsgangRepository.ausbildungskategorieFilter(baseQuery, ausbildungskategorie);
        }
        if (Objects.nonNull(ausbildungsstaetteNameDe)) {
            ausbildungsgangRepository.ausbildungsstaetteNameDeFilter(baseQuery, ausbildungsstaetteNameDe);
        }
        if (Objects.nonNull(ausbildungsstaetteNameFr)) {
            ausbildungsgangRepository.ausbildungsstaetteNameFrFilter(baseQuery, ausbildungsstaetteNameFr);
        }
        if (Objects.nonNull(aktiv)) {
            ausbildungsgangRepository.aktivFilter(baseQuery, aktiv);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = ausbildungsgangRepository.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            ausbildungsgangRepository.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            ausbildungsgangRepository.defaultOrder(baseQuery);
        }

        ausbildungsgangRepository.paginate(baseQuery, page, pageSize);

        final var results = baseQuery.stream()
            .map(ausbildungsgangMapper::toDto)
            .toList();

        return new PaginatedAusbildungsgangDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }

    @Transactional
    public AusbildungsgangDto setAusbildungsgangInaktiv(final UUID ausbildungsgangId) {
        final var ausbildungsgang = ausbildungsgangRepository.requireById(ausbildungsgangId);
        ausbildungsgang.setAktiv(false);
        return ausbildungsgangMapper.toDto(ausbildungsgang);
    }

}
