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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.service.EntityCopyMapper;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository.DelegierungEntry;
import ch.dvbern.stip.api.delegieren.type.DelegierungStatus;
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
import ch.dvbern.stip.generated.dto.DelegierungDto;
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
    private final BenutzerService benutzerService;
    private final DelegierungRepository delegierungRepository;
    private final FallRepository fallRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final PersoenlicheAngabenMapper persoenlicheAngabenMapper;
    private final StipConfig config;
    private final DelegierungMapper delegierungMapper;
    private final NotificationService notificationService;
    private final EntityCopyMapper entityCopyMapper;

    @Transactional
    public void delegateFall(final UUID fallId, final UUID sozialdienstId, final DelegierungCreateDto dto) {
        if (!Boolean.TRUE.equals(dto.getNutzungsbedingungenAkzeptiert())) {
            throw new BadRequestException();
        }

        final var fall = fallRepository.requireById(fallId);
        if (Objects.nonNull(fall.getCurrentDelegierung())) {
            throw new BadRequestException();
        }

        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        if (!sozialdienst.isAktiv()) {
            throw new BadRequestException();
        }

        final var newDelegierung = new Delegierung()
            .setFall(fall)
            .setSozialdienst(sozialdienst)
            .setPersoenlicheAngaben(persoenlicheAngabenMapper.toEntity(dto));
        fall.setCurrentDelegierung(newDelegierung);
        fall.getHistoricalDelegierungs().add(newDelegierung);

        delegierungRepository.persist(newDelegierung);
    }

    @Transactional
    public void delegierterMitarbeiterAendern(final UUID delegierungId, final DelegierterMitarbeiterAendernDto dto) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        final var mitarbeiter = sozialdienstBenutzerRepository.requireById(dto.getMitarbeiterId());

        final var mitarbeiterCurrent = delegierung.getDelegierterMitarbeiter();
        delegierung.setDelegierterMitarbeiter(mitarbeiter);
        if (mitarbeiterCurrent == null) {
            notificationService.createDelegierungAngenommenNotificationAndSendStdMail(delegierung);
            delegierung.akzeptieren();
        }
    }

    @Transactional
    public void delegierungAblehnen(final UUID delegierungId) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        notificationService.createDelegierungAbgelehntNotificationAndSendStdMail(delegierung);

        delegierung.ablehnen();
    }

    @Transactional
    public void delegierungAufloesen(final UUID delegierungId) {
        final var delegierung = delegierungRepository.requireById(delegierungId);

        final var auszahlung = delegierung.getFall().getAuszahlung();
        notificationService.createDelegierungAufgeloestNotificationAndSendStdMail(delegierung);

        if (auszahlung != null && auszahlung.isAuszahlungAnSozialdienst()) {
            var zahlungsverbindung = entityCopyMapper.createCopy(
                delegierung.getSozialdienst().getZahlungsverbindung()
            );
            auszahlung.setZahlungsverbindung(zahlungsverbindung);
            auszahlung.setAuszahlungAnSozialdienst(false);
        }
        delegierung.aufloesen();
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
        SozDashboardColumnDto sortColumn,
        SortOrder sortOrder
    ) {
        if (pageSize > config.pagination().maxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var sozialdienstId = sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer().getId();
        final var me = benutzerService.getCurrentBenutzer();
        final var sozialdienstBenutzerId =
            getDelegierungSozQueryType == GetDelegierungSozQueryTypeAdmin.ALLE_BEARBEITBAR_MEINE
                ? sozialdienstBenutzerRepository.requireById(me.getId()).getId()
                : null;
        final var delegierungStatus = switch (getDelegierungSozQueryType) {
            case GetDelegierungSozQueryTypeAdmin.ALLE, GetDelegierungSozQueryTypeAdmin.ALLE_BEARBEITBAR_MEINE -> null;
            case GetDelegierungSozQueryTypeAdmin.OFFEN -> DelegierungStatus.EINGEREICHT;
        };

        final var result = delegierungRepository.getFilteredAndOrderedDelegierungEntrys(
            sozialdienstId,
            sozialdienstBenutzerId,
            fallNummer,
            nachname,
            vorname,
            geburtsdatum,
            wohnort,
            delegierungStatus,
            sortColumn,
            sortOrder,
            page,
            pageSize
        );
        final var totalEntries =
            BigDecimal.valueOf(result.stream().findFirst().map(DelegierungEntry::totalCount).orElse(0d)).intValue();

        return new PaginatedSozDashboardDto(
            page,
            result.size(),
            totalEntries,
            result.stream().map(delegierungMapper::toEntryDto).toList()
        );
    }

    public DelegierungDto getDelegierung(UUID delegierungId) {
        final var delegierung = delegierungRepository.requireById(delegierungId);

        return delegierungMapper.toDto(delegierung);
    }

    public List<DelegierungDto> getAllDelegierungsForGesuch(UUID gesuchId) {
        final var fall = fallRepository.findFallForGesuch(gesuchId);

        return fall.getHistoricalDelegierungs()
            .stream()
            .sorted(Comparator.comparing(Delegierung::getTimestampErstellt).reversed())
            .map(delegierungMapper::toDto)
            .toList();
    }
}
