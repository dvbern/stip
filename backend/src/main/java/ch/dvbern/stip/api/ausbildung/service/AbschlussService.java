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

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.repo.AbschlussQueryBuilder;
import ch.dvbern.stip.api.ausbildung.repo.AbschlussRepository;
import ch.dvbern.stip.api.ausbildung.type.AbschlussSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AbschlussSlimDto;
import ch.dvbern.stip.generated.dto.BrueckenangebotCreateDto;
import ch.dvbern.stip.generated.dto.PaginatedAbschlussDto;
import ch.dvbern.stip.generated.dto.RenameAbschlussDto;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AbschlussService {
    private final AbschlussRepository abschlussRepository;
    private final AbschlussQueryBuilder abschlussQueryBuilder;
    private final AbschlussMapper abschlussMapper;
    private final ConfigService configService;

    @Transactional
    public Abschluss requireById(final UUID abschlussId) {
        return abschlussRepository.requireById(abschlussId);
    }

    @Transactional
    public AbschlussDto createAbschlussBrueckenangebot(final BrueckenangebotCreateDto brueckenangebotCreateDto) {
        final var brueckenangebot = abschlussMapper.createBrueckenangebot(brueckenangebotCreateDto);
        abschlussRepository.persist(brueckenangebot);
        return abschlussMapper.toDto(brueckenangebot);
    }

    @Transactional
    public List<AbschlussSlimDto> getAllAbschlussForAuswahl() {
        return abschlussRepository.findAll()
            .stream()
            .map(abschlussMapper::toSlimDto)
            .toList();
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

        final var baseQuery = abschlussQueryBuilder.baseQuery();

        if (Objects.nonNull(ausbildungskategorie)) {
            abschlussQueryBuilder.ausbildungskategorieFilter(baseQuery, ausbildungskategorie);
        }
        if (Objects.nonNull(bildungsrichtung)) {
            abschlussQueryBuilder.bildungsrichtungFilter(baseQuery, bildungsrichtung);
        }
        if (Objects.nonNull(bezeichnungDe)) {
            abschlussQueryBuilder.bezeichnungDeFilter(baseQuery, bezeichnungDe);
        }
        if (Objects.nonNull(bezeichnungFr)) {
            abschlussQueryBuilder.bezeichnungFrFilter(baseQuery, bezeichnungFr);
        }
        if (Objects.nonNull(aktiv)) {
            abschlussQueryBuilder.aktivFilter(baseQuery, aktiv);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = abschlussQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            abschlussQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            abschlussQueryBuilder.defaultOrder(baseQuery);
        }

        abschlussQueryBuilder.paginate(baseQuery, page, pageSize);

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
        if (abschluss.getAusbildungskategorie() != Ausbildungskategorie.BRUECKENANGEBOT) {
            throw new ForbiddenException("Can't set abschluss inaktiv that was not user created");
        }
        abschluss.setAktiv(false);
        return abschlussMapper.toDto(abschluss);
    }

    @Transactional
    public AbschlussDto renameAbschluss(final UUID abschlussId, final RenameAbschlussDto renameAbschlussDto) {
        final var abschluss = abschlussRepository.requireById(abschlussId);
        abschlussMapper.partialUpdate(renameAbschlussDto, abschluss);
        return abschlussMapper.toDto(abschluss);
    }
}
