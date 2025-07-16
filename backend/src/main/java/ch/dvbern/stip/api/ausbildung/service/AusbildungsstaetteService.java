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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteSortColumn;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteSlimDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsstaetteDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteService {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsstaetteMapper ausbildungsstaetteMapper;
    private final ConfigService configService;

    @Transactional
    public Ausbildungsstaette requireById(final UUID ausbildungsstaetteId) {
        return ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
    }

    @Transactional
    public AusbildungsstaetteDto createAusbildungsstaette(
        final AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto
    ) {
        final var ausbildungsstaette = ausbildungsstaetteMapper.toEntity(ausbildungsstaetteCreateDto);
        ausbildungsstaetteRepository.persist(ausbildungsstaette);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public List<AusbildungsstaetteSlimDto> getAllAusbildungsstaetteForAuswahl() {
        return ausbildungsstaetteRepository.findAllAktiv()
            .map(ausbildungsstaetteMapper::toSlimDto)
            .filter(ausbildungsstaetteSlimDto -> !ausbildungsstaetteSlimDto.getAusbildungsgaenge().isEmpty())
            .toList();
    }

    public PaginatedAusbildungsstaetteDto getAllAusbildungsstaetteForUebersicht(
        final Integer page,
        final Integer pageSize,
        final AusbildungsstaetteSortColumn sortColumn,
        final SortOrder sortOrder,
        final String nameDe,
        final String nameFr,
        final String chShis,
        final String burNo,
        final String ctNo,
        final Boolean aktiv
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = ausbildungsstaetteRepository.baseQuery();

        if (Objects.nonNull(nameDe)) {
            ausbildungsstaetteRepository.nameDeFilter(baseQuery, nameDe);
        }
        if (Objects.nonNull(nameFr)) {
            ausbildungsstaetteRepository.nameFrFilter(baseQuery, nameFr);
        }
        if (Objects.nonNull(chShis)) {
            ausbildungsstaetteRepository.chShisFilter(baseQuery, chShis);
        }
        if (Objects.nonNull(burNo)) {
            ausbildungsstaetteRepository.burNoFilter(baseQuery, burNo);
        }
        if (Objects.nonNull(ctNo)) {
            ausbildungsstaetteRepository.ctNoFilter(baseQuery, ctNo);
        }
        if (Objects.nonNull(aktiv)) {
            ausbildungsstaetteRepository.aktivFilter(baseQuery, aktiv);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = ausbildungsstaetteRepository.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            ausbildungsstaetteRepository.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            ausbildungsstaetteRepository.defaultOrder(baseQuery);
        }

        ausbildungsstaetteRepository.paginate(baseQuery, page, pageSize);

        final var results = baseQuery.stream()
            .map(ausbildungsstaetteMapper::toDto)
            .toList();

        return new PaginatedAusbildungsstaetteDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }

    @Transactional
    public AusbildungsstaetteDto setAusbildungsstaetteInaktiv(final UUID ausbildungsstaetteId) {
        final var ausbildungsstaette = ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
        ausbildungsstaette.setAktiv(false);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

}
