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

package ch.dvbern.stip.api.darlehen.service;

import java.time.LocalDate;
import java.time.LocalTime;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.darlehen.entity.QDarlehen;
import ch.dvbern.stip.api.darlehen.repo.DarlehenRepository;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.darlehen.type.GetDarlehenSbQueryType;
import ch.dvbern.stip.api.darlehen.type.SbDarlehenDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchformular.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.QGesuchTranche;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenDashboardQueryBuilder {
    private static final QDarlehen darlehen = QDarlehen.darlehen;
    private static final QGesuchTranche tranche = QGesuchTranche.gesuchTranche;
    private static final QGesuchFormular formular = QGesuchFormular.gesuchFormular;

    private final DarlehenRepository darlehenRepository;
    private final BenutzerService benutzerService;

    public JPAQuery<Darlehen> baseQuery(final GetDarlehenSbQueryType queryType) {
        final var benutzerId = benutzerService.getCurrentBenutzer().getId();

        final var query = switch (queryType) {
            case ALLE_DARLEHEN -> darlehenRepository.getAlleQuery();
            case MEINE_DARLEHEN -> darlehenRepository.getMeineQuery(benutzerId);
            case ALLE_BEARBEITBAR -> darlehenRepository.getAlleBearbeitbarQuery();
            case MEINE_BEARBEITBAR -> darlehenRepository.getMeineBearbeitbarQuery(benutzerId);
        };

        query
            .join(tranche)
            .on(tranche.gesuch.id.eq(darlehen.relatedGesuch.id));

        return query;
    }

    void joinFormular(final JPAQuery<Darlehen> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query.join(formular).on(tranche.gesuchFormular.id.eq(formular.id));
    }

    public void fallNummer(final JPAQuery<Darlehen> query, final String fallNummer) {
        query.where(darlehen.fall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void piaNachname(final JPAQuery<Darlehen> query, final String piaNachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.containsIgnoreCase(piaNachname));
    }

    public void piaVorname(final JPAQuery<Darlehen> query, final String piaVorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.containsIgnoreCase(piaVorname));
    }

    public void piaGeburtsdatum(final JPAQuery<Darlehen> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void status(final JPAQuery<Darlehen> query, final String status) {
        query.where(darlehen.status.eq(DarlehenStatus.valueOf(status)));
    }

    public void bearbeiter(final JPAQuery<Darlehen> query, final String bearbeiter) {
        query.where(
            darlehen.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname.eq(bearbeiter)
                .or(darlehen.fall.sachbearbeiterZuordnung.sachbearbeiter.vorname.eq(bearbeiter))
        );
    }

    public void letzteAktivitaet(
        final JPAQuery<Darlehen> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(darlehen.timestampMutiert.between(from.atStartOfDay(), to.atTime(LocalTime.MAX)));
    }

    public void orderBy(
        final JPAQuery<Darlehen> query,
        final SbDarlehenDashboardColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case FALLNUMMER -> darlehen.fall.fallNummer;
            case PIA_NACHNAME -> {
                joinFormular(query);
                yield formular.personInAusbildung.nachname;
            }
            case PIA_VORNAME -> {
                joinFormular(query);
                yield formular.personInAusbildung.vorname;
            }
            case PIA_GEBURTSDATUM -> {
                joinFormular(query);
                yield formular.personInAusbildung.geburtsdatum;
            }
            case STATUS -> darlehen.status;
            case BEARBEITER -> darlehen.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname;
            case LETZTE_AKTIVITAET -> darlehen.timestampMutiert;
        };

        final var oderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(oderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Darlehen> query) {
        query.orderBy(darlehen.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Darlehen> query) {
        return query.clone().select(darlehen.count());
    }

    public void paginate(final JPAQuery<Darlehen> query, final int page, final int pageSize) {
        query.offset((long) page * pageSize).limit(pageSize);
    }
}
