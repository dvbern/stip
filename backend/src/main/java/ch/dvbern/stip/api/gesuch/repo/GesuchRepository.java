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

package ch.dvbern.stip.api.gesuch.repo;

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.QGesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.QPersonInAusbildung;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchRepository implements BaseRepository<Gesuch> {
    private final EntityManager entityManager;

    public Stream<Gesuch> findForGs(final UUID gesuchstellerId) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var gesuch = QGesuch.gesuch;

        final var query = queryFactory
            .selectFrom(gesuch)
            .where(gesuch.fall.gesuchsteller.id.eq(gesuchstellerId));
        return query.stream();
    }

    public JPAQuery<Gesuch> getFindAlleMeineQuery(final UUID benutzerId) {
        return addMeineFilter(benutzerId, getFindAlleQuery());
    }

    public JPAQuery<Gesuch> getFindAlleMeineBearbeitbarQuery(final UUID benutzerId) {
        return addMeineFilter(benutzerId, getFindAlleBearbeitbarQuery());
    }

    public JPAQuery<Gesuch> getFindAlleMeineInJurBearbeitungQuery() {
        return getFindAlleJurBearbeitungQuery();
    }

    public JPAQuery<Gesuch> getFindAlleQuery() {
        // TODO KSTIP-1587/ 1590: Implement Status Filter?
        return new JPAQueryFactory(entityManager).selectFrom(QGesuch.gesuch);
    }

    public JPAQuery<Gesuch> getFindAlleJurBearbeitungQuery() {
        // TODO KSTIP-1587/ 1590: Implement Status Filter?
        final var query = getFindAlleQuery();
        return addStatusFilter(
            query,
            Gesuchstatus.JURISTISCHE_ABKLAERUNG,
            Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG
        );
    }

    public JPAQuery<Gesuch> getFindAlleBearbeitbarQuery() {
        // TODO KSTIP-1587/ 1590: Implement Status Filter?
        final var query = getFindAlleQuery();
        return addStatusFilter(
            query,
            Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
            Gesuchstatus.IN_BEARBEITUNG_SB,
            Gesuchstatus.FEHLENDE_DOKUMENTE,
            Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
            Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN,
            Gesuchstatus.NICHT_BEITRAGSBERECHTIGT,
            Gesuchstatus.IN_FREIGABE,
            Gesuchstatus.VERFUEGT,
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            Gesuchstatus.VERSANDBEREIT,
            Gesuchstatus.VERSENDET,
            Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
            Gesuchstatus.STIPENDIENANSPRUCH
        );
    }

    private JPAQuery<Gesuch> addStatusFilter(
        final JPAQuery<Gesuch> query,
        final Gesuchstatus... toInclude
    ) {
        final var gesuch = QGesuch.gesuch;
        query.where(gesuch.gesuchStatus.in(toInclude));
        return query;
    }

    private JPAQuery<Gesuch> addMeineFilter(final UUID benutzerId, final JPAQuery<Gesuch> query) {
        final var gesuch = QGesuch.gesuch;
        final var zuordnung = QZuordnung.zuordnung;

        query.join(zuordnung)
            .on(gesuch.fall.id.eq(zuordnung.fall.id))
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId));

        return query;
    }

    public Stream<Gesuch> findAllForFall(UUID fallId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuch = QGesuch.gesuch;

        var query = queryFactory
            .select(gesuch)
            .from(gesuch)
            .where(gesuch.fall.id.eq(fallId));
        return query.stream();
    }

    public Stream<Gesuch> findAllNewestWithPia() {
        final var gesuch = QGesuch.gesuch;
        final var tranche = QGesuchTranche.gesuchTranche;
        final var formular = QGesuchFormular.gesuchFormular;
        final var pia = QPersonInAusbildung.personInAusbildung;
        final var gesuchsperiode = QGesuchsperiode.gesuchsperiode;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuch)
            .join(tranche)
            .on(tranche.gesuch.id.eq(gesuch.id))
            .join(formular)
            .on(formular.tranche.id.eq(tranche.id))
            .join(pia)
            .on(formular.personInAusbildung.id.eq(pia.id))
            .join(gesuchsperiode)
            .on(gesuch.gesuchsperiode.id.eq(gesuchsperiode.id))
            .where(
                gesuch.id.in(
                    JPAExpressions
                        .select(gesuch.id)
                        .from(gesuch)
                        .join(tranche)
                        .on(tranche.gesuch.id.eq(gesuch.id))
                        .join(formular)
                        .on(formular.tranche.id.eq(tranche.id))
                        .join(pia)
                        .on(formular.personInAusbildung.id.eq(pia.id))
                        .join(gesuchsperiode)
                        .on(gesuch.gesuchsperiode.id.eq(gesuchsperiode.id))
                        .limit(1)
                )
            )
            .orderBy(tranche.gueltigkeit.gueltigBis.desc())
            .stream();
    }

    public Stream<Gesuch> findGesucheBySvNummer(String svNummer) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuch = QGesuch.gesuch;
        var gesuchTranche = QGesuchTranche.gesuchTranche;
        var gesuchFormular = QGesuchFormular.gesuchFormular;
        var personInAubsilung = QPersonInAusbildung.personInAusbildung;

        return queryFactory.select(gesuch)
            .distinct()
            .from(gesuchTranche)
            .join(gesuchTranche.gesuchFormular, gesuchFormular)
            .join(gesuchFormular.personInAusbildung, personInAubsilung)
            .where(personInAubsilung.sozialversicherungsnummer.eq(svNummer))
            .stream();
    }

    public Gesuch requireGesuchByTrancheId(final UUID gesuchTrancheId) {
        final var gesuch = QGesuch.gesuch;
        final var gesuchTranche = QGesuchTranche.gesuchTranche;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuch)
            .join(gesuchTranche)
            .on(gesuchTranche.gesuch.id.eq(gesuch.id))
            .where(gesuchTranche.id.eq(gesuchTrancheId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }
}
