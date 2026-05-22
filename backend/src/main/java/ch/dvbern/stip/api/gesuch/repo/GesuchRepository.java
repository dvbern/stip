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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.QAusbildung;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.QGesuchsperiode;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.QGesuchTranche;
import ch.dvbern.stip.api.notiz.entity.QGesuchNotiz;
import ch.dvbern.stip.api.personinausbildung.entity.QPersonInAusbildung;
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
    private final QGesuch Q_GESUCH = QGesuch.gesuch;
    private final QGesuchTranche Q_TRANCHE = QGesuchTranche.gesuchTranche;

    public Stream<Gesuch> findForGs(final UUID gesuchstellerId) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var ausbildung = QAusbildung.ausbildung;

        final var query = queryFactory
            .selectFrom(Q_GESUCH)
            .join(ausbildung)
            .on(Q_GESUCH.ausbildung.id.eq(ausbildung.id))
            .where(ausbildung.fall.gesuchsteller.id.eq(gesuchstellerId));
        return query.stream();
    }

    public JPAQuery<Gesuch> getFindAlleQuery() {
        return new JPAQueryFactory(entityManager).selectFrom(Q_GESUCH);
    }

    public JPAQuery<Gesuch> getFindAllePendenteQuery() {
        final var gesuch = QGesuch.gesuch;
        final var gesuchNotiz = QGesuchNotiz.gesuchNotiz;

        return getFindAlleQuery()
            .join(gesuchNotiz)
            .on(gesuchNotiz.gesuch.id.eq(gesuch.id))
            .where(gesuchNotiz.pendenzAbgeschlossen.not());
    }

    public JPAQuery<Gesuch> addStatusFilter(
        final JPAQuery<Gesuch> query,
        final Gesuchstatus... toInclude
    ) {
        query.where(Q_GESUCH.gesuchStatus.in(toInclude));
        return query;
    }

    public Stream<Gesuch> findAllForFall(UUID fallId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        final var ausbildung = QAusbildung.ausbildung;

        var query = queryFactory
            .selectFrom(Q_GESUCH)
            .join(ausbildung)
            .on(Q_GESUCH.ausbildung.id.eq(ausbildung.id))
            .where(ausbildung.fall.id.eq(fallId));
        return query.stream();
    }

    public Stream<Gesuch> findAllNewestWithPia() {
        final var formular = QGesuchFormular.gesuchFormular;
        final var pia = QPersonInAusbildung.personInAusbildung;
        final var gesuchsperiode = QGesuchsperiode.gesuchsperiode;

        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_GESUCH)
            .join(Q_TRANCHE)
            .on(Q_TRANCHE.gesuch.id.eq(Q_GESUCH.id))
            .join(formular)
            .on(formular.tranche.id.eq(Q_TRANCHE.id))
            .join(pia)
            .on(formular.personInAusbildung.id.eq(pia.id))
            .join(gesuchsperiode)
            .on(Q_GESUCH.gesuchsperiode.id.eq(gesuchsperiode.id))
            .where(
                Q_GESUCH.id.in(
                    JPAExpressions
                        .select(Q_GESUCH.id)
                        .from(Q_GESUCH)
                        .join(Q_TRANCHE)
                        .on(Q_TRANCHE.gesuch.id.eq(Q_GESUCH.id))
                        .join(formular)
                        .on(formular.tranche.id.eq(Q_TRANCHE.id))
                        .join(pia)
                        .on(formular.personInAusbildung.id.eq(pia.id))
                        .join(gesuchsperiode)
                        .on(Q_GESUCH.gesuchsperiode.id.eq(gesuchsperiode.id))
                        .limit(1)
                )
            )
            .orderBy(Q_TRANCHE.gueltigkeit.gueltigBis.desc())
            .stream();
    }

    public Stream<Gesuch> findGesucheBySvNummer(String svNummer) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchTranche = QGesuchTranche.gesuchTranche;
        var gesuchFormular = QGesuchFormular.gesuchFormular;
        var personInAusbildung = QPersonInAusbildung.personInAusbildung;

        return queryFactory.select(Q_GESUCH)
            .distinct()
            .from(gesuchTranche)
            .join(gesuchTranche.gesuchFormular, gesuchFormular)
            .join(gesuchFormular.personInAusbildung, personInAusbildung)
            .where(personInAusbildung.sozialversicherungsnummer.eq(svNummer))
            .stream();
    }

    public Gesuch requireGesuchByTrancheId(final UUID gesuchTrancheId) {
        final var gesuchTranche = QGesuchTranche.gesuchTranche;

        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_GESUCH)
            .join(gesuchTranche)
            .on(gesuchTranche.gesuch.id.eq(Q_GESUCH.id))
            .where(gesuchTranche.id.eq(gesuchTrancheId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    public Stream<Gesuch> getAllWartenAufUnterschriftenblatt() {
        return addStatusFilter(getFindAlleQuery(), Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT).stream();
    }

    public List<Gesuch> getAllFehlendeDokumente() {
        return addStatusFilter(getFindAlleQuery(), Gesuchstatus.FEHLENDE_DOKUMENTE).stream().toList();
    }

    public Optional<GesuchFormular> getLatestGesuchFormularWithPiaForFall(final UUID fallId) {
        final var gesuch = QGesuch.gesuch;
        final var formular = QGesuchFormular.gesuchFormular;
        final var pia = QPersonInAusbildung.personInAusbildung;
        final var ausbildung = QAusbildung.ausbildung;
        final var fall = QFall.fall;

        return new JPAQueryFactory(entityManager)
            .selectFrom(formular)
            .join(Q_TRANCHE)
            .on(Q_TRANCHE.gesuch.id.eq(gesuch.id))
            .join(formular)
            .on(formular.tranche.id.eq(Q_TRANCHE.id))
            .join(pia)
            .on(formular.personInAusbildung.id.eq(pia.id))
            .join(ausbildung)
            .on(gesuch.ausbildung.id.eq(ausbildung.id))
            .join(fall)
            .on(ausbildung.fall.id.eq(fallId))
            .orderBy(pia.timestampMutiert.desc())
            .stream()
            .findFirst();
    }

    public Stream<Gesuch> findGesuchWithPendingSapAction() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_GESUCH)
            .where(Q_GESUCH.pendingSapAction.isNotNull())
            .stream();
    }

    public List<Gesuch> findGesuchsByGesuchsperiodeIdWithPendingRemainderPayment(final UUID gesuchsperiodeId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_GESUCH)
            .where(
                Q_GESUCH.gesuchsperiode.id.eq(gesuchsperiodeId)
                    .and(
                        Q_GESUCH.remainderPaymentExecuted.not()
                    )
            )
            .fetch();
    }
}
