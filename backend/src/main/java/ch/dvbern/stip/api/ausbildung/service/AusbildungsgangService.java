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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangQueryBuilder;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsgangSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangSlimDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNGSGANG_AUSBILDUNGSSTAETTE_ABSCHLUSS_NOT_UNIQUE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangService {
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final AusbildungsgangQueryBuilder ausbildungsgangQueryBuilder;
    private final AusbildungsgangMapper ausbildungsgangMapper;
    private final ConfigService configService;

    @Transactional
    public Ausbildungsgang requireById(final UUID id) {
        return ausbildungsgangRepository.requireById(id);
    }

    @Transactional
    public AusbildungsgangDto createAusbildungsgang(final AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        final var ausbildungsgang = ausbildungsgangMapper.toEntity(ausbildungsgangCreateDto);
        validateAusbildungsgangUniqueness(ausbildungsgang);

        ausbildungsgangRepository.persist(ausbildungsgang);
        return ausbildungsgangMapper.toDto(ausbildungsgang);
    }

    @Transactional
    public List<AusbildungsgangSlimDto> getAllAusbildungsgangForAuswahl() {
        return ausbildungsgangRepository.findAllAktiv()
            .map(ausbildungsgangMapper::toSlimDto)
            .toList();
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

        final var baseQuery = ausbildungsgangQueryBuilder.baseQuery();

        if (Objects.nonNull(abschlussBezeichnungDe)) {
            ausbildungsgangQueryBuilder.abschlussBezeichnungDeFilter(baseQuery, abschlussBezeichnungDe);
        }
        if (Objects.nonNull(abschlussBezeichnungFr)) {
            ausbildungsgangQueryBuilder.abschlussBezeichnungFrFilter(baseQuery, abschlussBezeichnungFr);
        }
        if (Objects.nonNull(ausbildungskategorie)) {
            ausbildungsgangQueryBuilder.ausbildungskategorieFilter(baseQuery, ausbildungskategorie);
        }
        if (Objects.nonNull(ausbildungsstaetteNameDe)) {
            ausbildungsgangQueryBuilder.ausbildungsstaetteNameDeFilter(baseQuery, ausbildungsstaetteNameDe);
        }
        if (Objects.nonNull(ausbildungsstaetteNameFr)) {
            ausbildungsgangQueryBuilder.ausbildungsstaetteNameFrFilter(baseQuery, ausbildungsstaetteNameFr);
        }
        if (Objects.nonNull(aktiv)) {
            ausbildungsgangQueryBuilder.aktivFilter(baseQuery, aktiv);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = ausbildungsgangQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            ausbildungsgangQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            ausbildungsgangQueryBuilder.defaultOrder(baseQuery);
        }

        ausbildungsgangQueryBuilder.paginate(baseQuery, page, pageSize);

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

    private void validateAusbildungsgangUniqueness(final Ausbildungsgang ausbildungsgang) {
        final var duplicate = ausbildungsgangRepository.findByAusbildungsstaetteAndAbschluss(
            ausbildungsgang.getAusbildungsstaette().getId(),
            ausbildungsgang.getAbschluss().getId()
        );

        if (duplicate.isPresent()) {
            throw new CustomValidationsException(
                "The combination of Ausbildungsstaette and Abschluss must be unique",
                new CustomConstraintViolation(
                    VALIDATION_AUSBILDUNGSGANG_AUSBILDUNGSSTAETTE_ABSCHLUSS_NOT_UNIQUE,
                    ""
                )
            );
        }
    }
}
