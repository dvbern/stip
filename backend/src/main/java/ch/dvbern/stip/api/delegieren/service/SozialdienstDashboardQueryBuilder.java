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

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.entity.QDelegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.generated.dto.GetDelegierungSozQueryTypeDto;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDto;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SozialdienstDashboardQueryBuilder {
    private final BenutzerService benutzerService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final DelegierungRepository delegierungRepository;
    private static final QDelegierung qDelegierung = QDelegierung.delegierung;

    public JPAQuery<Delegierung> baseQuery(final GetDelegierungSozQueryTypeDto queryType, final UUID sozialdienstId) {
        final var me = benutzerService.getCurrentBenutzer();
        final var sozialdienstBenutzer = sozialdienstBenutzerRepository.requireById(me.getId());
        return switch (queryType) {
            case ALLE -> delegierungRepository.getFindAlleOfSozialdienstQuery(sozialdienstId);
            case ALLE_BEARBEITBAR_MEINE -> delegierungRepository
                .getFindAlleMeineQuery(sozialdienstBenutzer, sozialdienstId);
        };
    }

    public void fallNummer(final JPAQuery<Delegierung> query, final String fallNummer) {
        query.where(qDelegierung.delegierterFall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void vorname(final JPAQuery<Delegierung> query, final String vorname) {
        query.where(qDelegierung.persoenlicheAngaben.vorname.containsIgnoreCase(vorname));
    }

    public void nachname(final JPAQuery<Delegierung> query, final String nachname) {
        query.where(qDelegierung.persoenlicheAngaben.nachname.containsIgnoreCase(nachname));
    }

    public void wohnort(final JPAQuery<Delegierung> query, final String wohnort) {
        query.where(qDelegierung.persoenlicheAngaben.adresse.ort.containsIgnoreCase(wohnort));
    }

    public void geburtsdtaum(final JPAQuery<Delegierung> query, final LocalDate geburtsdatum) {
        query.where(qDelegierung.persoenlicheAngaben.geburtsdatum.eq(geburtsdatum));
    }

    public void delegierungAngenommen(final JPAQuery<Delegierung> query, final Boolean delegierungAngenommen) {
        query.where(qDelegierung.delegierungAngenommen.eq(delegierungAngenommen));
    }

    public void orderBy(
        final JPAQuery<Delegierung> query,
        final SozDashboardColumnDto column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case FALLNUMMER -> qDelegierung.delegierterFall.fallNummer;
            case VORNAME -> qDelegierung.persoenlicheAngaben.vorname;
            case NACHNAME -> qDelegierung.persoenlicheAngaben.nachname;
            case WOHNORT -> qDelegierung.persoenlicheAngaben.adresse.ort;
            case DELEGIERUNG_ANGENOMMEN -> qDelegierung.persoenlicheAngaben.adresse.ort;
            case GEBURTSDATUM -> qDelegierung.persoenlicheAngaben.geburtsdatum;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Delegierung> query) {
        query.orderBy(qDelegierung.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Delegierung> query) {
        return query.clone().select(qDelegierung.count());
    }

    public void paginate(final JPAQuery<Delegierung> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
