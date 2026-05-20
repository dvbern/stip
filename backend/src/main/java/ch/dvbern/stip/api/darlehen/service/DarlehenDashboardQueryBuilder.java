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
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.type.RoleFeature;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.darlehen.entity.QFreiwilligDarlehen;
import ch.dvbern.stip.api.darlehen.repo.FreiwilligDarlehenRepository;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.darlehen.type.SbFreiwilligDarlehenDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchformular.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.QGesuchTranche;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenDashboardQueryBuilder {
    private static final QFreiwilligDarlehen freiwilligDarlehen = QFreiwilligDarlehen.freiwilligDarlehen;
    private static final QGesuchTranche tranche = QGesuchTranche.gesuchTranche;
    private static final QGesuchFormular formular = QGesuchFormular.gesuchFormular;

    private final FreiwilligDarlehenRepository freiwilligDarlehenRepository;
    private final BenutzerService benutzerService;

    public JPAQuery<FreiwilligDarlehen> baseQuery() {
        final var query = freiwilligDarlehenRepository.getAlleQuery();

        query
            .join(tranche)
            .on(tranche.gesuch.id.eq(freiwilligDarlehen.relatedGesuch.id));

        return query;
    }

    public JPAQuery<FreiwilligDarlehen> onlyBearbeitbar(JPAQuery<FreiwilligDarlehen> query) {
        return query.where(
            freiwilligDarlehen.status.in(
                benutzerService.getSetByUserRole(
                    RoleFeature.forSachbearbeiter(DarlehenStatus.EINGEGEBEN),
                    RoleFeature.forFreigabe(DarlehenStatus.IN_FREIGABE)
                )
            )
        );
    }

    public JPAQuery<FreiwilligDarlehen> onlyMeine(final JPAQuery<FreiwilligDarlehen> query, final UUID benutzerId) {
        final var zuordnung = QZuordnung.zuordnung;

        query.join(zuordnung)
            .on(freiwilligDarlehen.fall.sachbearbeiterZuordnung.id.eq(zuordnung.id))
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId));

        return query;
    }

    void joinFormular(final JPAQuery<FreiwilligDarlehen> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query.join(formular).on(tranche.gesuchFormular.id.eq(formular.id));
    }

    public void fallNummer(final JPAQuery<FreiwilligDarlehen> query, final String fallNummer) {
        query.where(freiwilligDarlehen.fall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void piaNachname(final JPAQuery<FreiwilligDarlehen> query, final String piaNachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.containsIgnoreCase(piaNachname));
    }

    public void piaVorname(final JPAQuery<FreiwilligDarlehen> query, final String piaVorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.containsIgnoreCase(piaVorname));
    }

    public void piaGeburtsdatum(final JPAQuery<FreiwilligDarlehen> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void status(final JPAQuery<FreiwilligDarlehen> query, final String status) {
        query.where(freiwilligDarlehen.status.eq(DarlehenStatus.valueOf(status)));
    }

    public void bearbeiter(final JPAQuery<FreiwilligDarlehen> query, final String bearbeiter) {
        query.join(QZuordnung.zuordnung)
            .on(freiwilligDarlehen.fall.sachbearbeiterZuordnung.id.eq(QZuordnung.zuordnung.id));
        query.where(
            QZuordnung.zuordnung.sachbearbeiter.nachname.containsIgnoreCase(bearbeiter)
                .or(QZuordnung.zuordnung.sachbearbeiter.vorname.containsIgnoreCase(bearbeiter))
        );
    }

    public void letzteAktivitaet(
        final JPAQuery<FreiwilligDarlehen> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(freiwilligDarlehen.timestampMutiert.between(from.atStartOfDay(), to.atTime(LocalTime.MAX)));
    }

    public void orderBy(
        final JPAQuery<FreiwilligDarlehen> query,
        final SbFreiwilligDarlehenDashboardColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case FALLNUMMER -> freiwilligDarlehen.fall.fallNummer;
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
            case STATUS -> freiwilligDarlehen.status;
            case BEARBEITER -> freiwilligDarlehen.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname;
            case LETZTE_AKTIVITAET -> freiwilligDarlehen.timestampMutiert;
        };

        final var oderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(oderSpecifier);
    }

    public void defaultOrder(final JPAQuery<FreiwilligDarlehen> query) {
        query.orderBy(freiwilligDarlehen.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<FreiwilligDarlehen> query) {
        return query.clone().select(freiwilligDarlehen.count());
    }

    public void paginate(final JPAQuery<FreiwilligDarlehen> query, final int page, final int pageSize) {
        query.offset((long) page * pageSize).limit(pageSize);
    }
}
