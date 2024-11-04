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

package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.time.LocalTime;

import ch.dvbern.stip.api.ausbildung.entity.QAusbildung;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardQueryBuilder {
    private static final QGesuchFormular formular = QGesuchFormular.gesuchFormular;
    private static final QGesuch gesuch = QGesuch.gesuch;
    private static final QAusbildung ausbildung = QAusbildung.ausbildung;
    private QGesuchTranche tranche;

    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;

    public JPAQuery<Gesuch> baseQuery(final GetGesucheSBQueryType queryType, final GesuchTrancheTyp trancheType) {
        final var meId = benutzerService.getCurrentBenutzer().getId();

        final var query = switch (queryType) {
        case ALLE_BEARBEITBAR -> gesuchRepository.getFindAlleBearbeitbarQuery();
        case ALLE_BEARBEITBAR_MEINE -> gesuchRepository.getFindAlleMeineBearbeitbarQuery(meId);
        case ALLE_MEINE -> gesuchRepository.getFindAlleMeineQuery(meId);
        case ALLE -> gesuchRepository.getFindAlleQuery();
        };

        tranche = switch (trancheType) {
        case TRANCHE -> gesuch.latestGesuchTranche;
        case AENDERUNG -> gesuch.aenderungZuUeberpruefen;
        };

        joinFormular(query);
        return query.where(
            formular.personInAusbildung.vorname.isNotNull()
                .and(formular.personInAusbildung.nachname.isNotNull())
        );
    }

    public void fallNummer(final JPAQuery<Gesuch> query, final String fallNummer) {
        joinGesuch(query);
        query.join(ausbildung).on(gesuch.ausbildung.id.eq(ausbildung.id));
        query.where(ausbildung.fall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void piaNachname(final JPAQuery<Gesuch> query, final String nachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.containsIgnoreCase(nachname));
    }

    public void piaVorname(final JPAQuery<Gesuch> query, final String vorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.containsIgnoreCase(vorname));
    }

    void joinFormular(final JPAQuery<Gesuch> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query.join(formular).on(tranche.gesuchFormular.id.eq(formular.id));
    }

    void joinGesuch(final JPAQuery<Gesuch> query) {
        query.join(gesuch).on(tranche.gesuch.id.eq(gesuch.id));
    }

    public void piaGeburtsdatum(final JPAQuery<Gesuch> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void status(final JPAQuery<Gesuch> query, final Gesuchstatus status) {
        if (status == Gesuchstatus.IN_BEARBEITUNG_SB) {
            query.where(tranche.status.eq(GesuchTrancheStatus.UEBERPRUEFEN));
        }

        query.where(tranche.gesuch.gesuchStatus.eq(status));
    }

    public void bearbeiter(final JPAQuery<Gesuch> query, final String bearbeiter) {
        joinGesuch(query);
        query.join(ausbildung).on(gesuch.ausbildung.id.eq(ausbildung.id));
        query.join(QZuordnung.zuordnung).on(ausbildung.fall.sachbearbeiterZuordnung.id.eq(QZuordnung.zuordnung.id));
        query.where(
            QZuordnung.zuordnung.sachbearbeiter.nachname.containsIgnoreCase(bearbeiter)
                .or(QZuordnung.zuordnung.sachbearbeiter.vorname.containsIgnoreCase(bearbeiter))
        );
    }

    public void letzteAktivitaet(
        final JPAQuery<Gesuch> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(tranche.gesuch.timestampMutiert.between(from.atStartOfDay(), to.atTime(LocalTime.MAX)));
    }

    public void orderBy(final JPAQuery<Gesuch> query, final SbDashboardColumn column, final SortOrder sortOrder) {
        final var fieldSpecified = switch (column) {
        case FALLNUMMER -> ausbildung.fall.fallNummer;
        case TYP -> tranche.typ;
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
        case STATUS -> gesuch.gesuchStatus;
        case BEARBEITER -> {
            final var fall = QFall.fall;
            query.join(ausbildung).on(gesuch.ausbildung.id.eq(ausbildung.id));
            query.join(fall).on(ausbildung.id.eq(fall.id));
            yield fall.sachbearbeiterZuordnung.sachbearbeiter.nachname;
        }
        case LETZTE_AKTIVITAET -> gesuch.timestampMutiert;
        };

        final var orderSpecifier = switch (sortOrder) {
        case ASCENDING -> fieldSpecified.asc();
        case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Gesuch> query) {
        query.orderBy(tranche.gesuch.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Gesuch> query) {
        return query.clone().select(tranche.count());
    }

    public void paginate(final JPAQuery<Gesuch> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
