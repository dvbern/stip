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

package ch.dvbern.stip.api.delegieren.service;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.delegieren.type.GetDelegierungSozQueryTypeAdmin;
import ch.dvbern.stip.api.delegieren.type.GetDelegierungSozQueryTypeMitarbeiter;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import ch.dvbern.stip.generated.dto.PaginatedSozDashboardDto;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class DelegierenService {
    private final DelegierungRepository delegierungRepository;
    private final FallRepository fallRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final PersoenlicheAngabenMapper persoenlicheAngabenMapper;
    private final SozialdienstDashboardQueryBuilder sozDashboardQueryBuilder;
    private final ConfigService configService;
    private final DelegierungMapper delegierungMapper;
    private final NotificationService notificationService;

    @Transactional
    public void delegateFall(final UUID fallId, final UUID sozialdienstId, final DelegierungCreateDto dto) {
        final var fall = fallRepository.requireById(fallId);
        if (fall.getDelegierung() != null) {
            throw new BadRequestException();
        }

        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        if (!sozialdienst.isAktiv()) {
            throw new BadRequestException();
        }

        final var newDelegierung = new Delegierung()
            .setDelegierterFall(fall)
            .setSozialdienst(sozialdienst)
            .setPersoenlicheAngaben(persoenlicheAngabenMapper.toEntity(dto));

        delegierungRepository.persist(newDelegierung);
    }

    @Transactional
    public void delegierterMitarbeiterAendern(final UUID delegierungId, final DelegierterMitarbeiterAendernDto dto) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        final var mitarbeiter = sozialdienstBenutzerRepository.requireById(dto.getMitarbeiterId());

        if (delegierung.getDelegierterMitarbeiter() == null) {
            notificationService.createDelegierungAngenommenNotification(delegierung);
        }

        delegierung.setDelegierterMitarbeiter(mitarbeiter);
    }

    @Transactional
    public void delegierungAblehnen(final UUID delegierungId) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        notificationService.createDelegierungAbgelehntNotification(delegierung);
        delegierung.getDelegierterFall().setDelegierung(null);
        delegierung.getSozialdienst().getDelegierungen().remove(delegierung);
        delegierungRepository.delete(delegierung);
    }

    public PaginatedSozDashboardDto getDelegierungSoz(
        GetDelegierungSozQueryTypeMitarbeiter getDelegierungSozQueryType,
        @NotNull Integer page,
        @NotNull Integer pageSize,
        String fallNummer,
        String nachname,
        String vorname,
        LocalDate geburtsdatum,
        String wohnort,
        Boolean delegierungAngenommen,
        SozDashboardColumnDto sortColumn,
        SortOrder sortOrder
    ) {
        var adminDto = switch (getDelegierungSozQueryType) {
            case ALLE -> GetDelegierungSozQueryTypeAdmin.ALLE;
            case ALLE_BEARBEITBAR_MEINE -> GetDelegierungSozQueryTypeAdmin.ALLE_BEARBEITBAR_MEINE;
        };

        return getDelegierungSoz(
            adminDto,
            page,
            pageSize,
            fallNummer,
            nachname,
            vorname,
            geburtsdatum,
            wohnort,
            delegierungAngenommen,
            sortColumn,
            sortOrder
        );
    }

    @Transactional
    public PaginatedSozDashboardDto getDelegierungSoz(
        GetDelegierungSozQueryTypeAdmin getDelegierungSozQueryType,
        @NotNull Integer page,
        @NotNull Integer pageSize,
        String fallNummer,
        String nachname,
        String vorname,
        LocalDate geburtsdatum,
        String wohnort,
        Boolean delegierungAngenommen,
        SozDashboardColumnDto sortColumn,
        SortOrder sortOrder
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var sozialdienst = sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer();

        final var baseQuery = sozDashboardQueryBuilder
            .baseQuery(getDelegierungSozQueryType, sozialdienst.getId());

        if (fallNummer != null) {
            sozDashboardQueryBuilder.fallNummer(baseQuery, fallNummer);
        }

        if (vorname != null) {
            sozDashboardQueryBuilder.vorname(baseQuery, vorname);
        }

        if (nachname != null) {
            sozDashboardQueryBuilder.nachname(baseQuery, nachname);
        }

        if (geburtsdatum != null) {
            sozDashboardQueryBuilder.geburtsdatum(baseQuery, geburtsdatum);
        }

        if (wohnort != null) {
            sozDashboardQueryBuilder.wohnort(baseQuery, wohnort);
        }

        if (delegierungAngenommen != null) {
            sozDashboardQueryBuilder.delegierungAngenommen(baseQuery, delegierungAngenommen);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = sozDashboardQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            sozDashboardQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            sozDashboardQueryBuilder.defaultOrder(baseQuery);
        }

        sozDashboardQueryBuilder.paginate(baseQuery, page, pageSize);
        final var results = baseQuery.stream()
            .map(delegierung -> delegierungMapper.toFallWithDto(delegierung.getDelegierterFall()))
            .toList();

        return new PaginatedSozDashboardDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }
}
