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
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.QAusbildung;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.type.RoleFeature;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbGesucheDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchformular.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.entity.QGesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardQueryBuilder {
    private static final QGesuchFormular formular = QGesuchFormular.gesuchFormular;
    private static final QGesuch gesuch = QGesuch.gesuch;
    private static final QAusbildung ausbildung = QAusbildung.ausbildung;
    private static final QGesuchTranche tranche = QGesuchTranche.gesuchTranche;

    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;

    public JPAQuery<Gesuch> baseGesuchQuery(final GetGesucheSBQueryType queryType) {
        final var query = switch (queryType) {
            case ALLE -> gesuchRepository.getFindAlleQuery();
            case DRUCKBAR_VERFUEGUNGEN -> {
                final var baseQuery = gesuchRepository.getFindAlleQuery();
                gesuchRepository.addStatusFilter(baseQuery, Gesuchstatus.VERFUEGUNG_DRUCKBEREIT);
                yield baseQuery;
            }
            case DRUCKBAR_DATENSCHUTZBRIEFE -> {
                final var baseQuery = gesuchRepository.getFindAlleQuery();
                gesuchRepository.addStatusFilter(baseQuery, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT);
                yield baseQuery;
            }
            case PENDENTE -> gesuchRepository.getFindAllePendenteQuery();
        };

        final var trancheSub = new QGesuchTranche("sub2");
        final var joinSubselect = JPAExpressions.select(trancheSub.id)
            .from(trancheSub)
            .where(
                trancheSub.gesuch.id.eq(gesuch.id)
                    .and(
                        trancheSub.gueltigkeit.gueltigBis.eq(
                            JPAExpressions.select(trancheSub.gueltigkeit.gueltigBis.max())
                                .from(trancheSub)
                                .where(trancheSub.gesuch.id.eq(gesuch.id))
                        )
                    )
                    .and(trancheSub.typ.eq(GesuchTrancheTyp.TRANCHE))
            );

        query.join(gesuch.gesuchTranchen, tranche).where(tranche.id.in(joinSubselect));

        joinFormular(query);
        return query;
    }

    public JPAQuery<GesuchTranche> baseAenderungQuery() {
        final var query = gesuchTrancheRepository.getFindAlleAenderungsQuery()
            .join(tranche.gesuch, gesuch);

        joinFormular(query);
        return query.where(
            formular.personInAusbildung.vorname.isNotNull()
                .and(formular.personInAusbildung.nachname.isNotNull())
        );
    }

    public void onlyBearbeitbarGesuchs(JPAQuery<Gesuch> query) {
        gesuchRepository.addStatusFilter(
            query,
            benutzerService.getSetByUserRole(
                RoleFeature.forFreigabe(Gesuchstatus.IN_FREIGABE),
                RoleFeature.forSachbearbeiter(
                    Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
                    Gesuchstatus.IN_BEARBEITUNG_SB,
                    Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN,
                    Gesuchstatus.NICHT_BEITRAGSBERECHTIGT,
                    Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
                    Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT,
                    Gesuchstatus.DATENSCHUTZBRIEF_VERSANDBEREIT,
                    Gesuchstatus.VERFUEGUNG_DRUCKBEREIT,
                    Gesuchstatus.VERFUEGUNG_VERSENDET,
                    Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT,
                    Gesuchstatus.VERFUEGUNG_VERSANDBEREIT
                )
            ).toArray(new Gesuchstatus[0])
        );
    }

    public void onlyBearbeitbarAenderungs(JPAQuery<GesuchTranche> query) {
        query.where(
            tranche.status.in(GesuchTrancheStatus.UEBERPRUEFEN, GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
        );
    }

    public void onlyCurrentBenutzer(final JPAQuery<? extends AbstractEntity> query, final UUID benutzerId) {
        final var ausbildung = QAusbildung.ausbildung;
        final var zuordnung = QZuordnung.zuordnung;

        query.join(ausbildung)
            .on(gesuch.ausbildung.id.eq(ausbildung.id))
            .join(zuordnung)
            .on(ausbildung.fall.id.eq(zuordnung.fall.id))
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId));
    }

    public void fallNummer(final JPAQuery<? extends AbstractEntity> query, final String fallNummer) {
        joinGesuch(query);
        query.join(ausbildung).on(gesuch.ausbildung.id.eq(ausbildung.id));
        query.where(ausbildung.fall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void piaNachname(final JPAQuery<? extends AbstractEntity> query, final String nachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.containsIgnoreCase(nachname));
    }

    public void piaVorname(final JPAQuery<? extends AbstractEntity> query, final String vorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.containsIgnoreCase(vorname));
    }

    private void joinFormular(final JPAQuery<? extends AbstractEntity> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query
            .join(formular)
            .on(tranche.gesuchFormular.id.eq(formular.id))
            .where(
                formular.personInAusbildung.vorname.isNotNull()
                    .and(formular.personInAusbildung.nachname.isNotNull())
            );
    }

    private void joinGesuch(final JPAQuery<? extends AbstractEntity> query) {
        query.join(gesuch).on(tranche.gesuch.id.eq(gesuch.id));
    }

    public void piaGeburtsdatum(final JPAQuery<? extends AbstractEntity> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void gesuchStatus(final JPAQuery<Gesuch> query, final String status) {
        query.where(tranche.gesuch.gesuchStatus.eq(Gesuchstatus.valueOf(status)));
    }

    public void trancheStatus(final JPAQuery<GesuchTranche> query, final String status) {
        query.where(tranche.status.eq(GesuchTrancheStatus.valueOf(status)));
    }

    public void bearbeiter(final JPAQuery<? extends AbstractEntity> query, final String bearbeiter) {
        joinGesuch(query);
        query.join(ausbildung).on(gesuch.ausbildung.id.eq(ausbildung.id));
        query.join(QZuordnung.zuordnung).on(ausbildung.fall.sachbearbeiterZuordnung.id.eq(QZuordnung.zuordnung.id));
        query.where(
            QZuordnung.zuordnung.sachbearbeiter.nachname.containsIgnoreCase(bearbeiter)
                .or(QZuordnung.zuordnung.sachbearbeiter.vorname.containsIgnoreCase(bearbeiter))
        );
    }

    public void letzteAktivitaet(
        final JPAQuery<? extends AbstractEntity> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(tranche.gesuch.timestampMutiert.between(from.atStartOfDay(), to.atTime(LocalTime.MAX)));
    }

    public void orderBy(
        final JPAQuery<? extends AbstractEntity> query,
        final SbGesucheDashboardColumn column,
        final SortOrder sortOrder
    ) {
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

    public void defaultOrder(final JPAQuery<? extends AbstractEntity> query) {
        query.orderBy(tranche.gesuch.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<? extends AbstractEntity> query) {
        return query.clone().select(tranche.count());
    }

    public void paginate(final JPAQuery<? extends AbstractEntity> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
