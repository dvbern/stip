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

    public Stream<Gesuch> findAlle() {
        return getFindAlleQuery().stream();
    }

    public Stream<Gesuch> findAlleBearbeitbar() {
        return getFindAlleBearbeitbarQuery().stream();
    }

    public Stream<Gesuch> findAlleMeine(final UUID benutzerId) {
        return addMeineFilter(benutzerId, getFindAlleQuery()).stream();
    }

    public Stream<Gesuch> findAlleMeineBearbeitbar(final UUID benutzerId) {
        return addMeineFilter(benutzerId, getFindAlleBearbeitbarQuery()).stream();
    }

    private JPAQuery<Gesuch> getFindAlleQuery() {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var gesuch = QGesuch.gesuch;
        return queryFactory.selectFrom(gesuch);
    }

    private JPAQuery<Gesuch> getFindAlleBearbeitbarQuery() {
        final var query = getFindAlleQuery();
        return addStatusFilter(
            query,
            Gesuchstatus.IN_BEARBEITUNG_GS,
            Gesuchstatus.EINGEREICHT
        );
    }

    private JPAQuery<Gesuch> addStatusFilter(
        final JPAQuery<Gesuch> query,
        final Gesuchstatus... toExclude
    ) {
        final var gesuch = QGesuch.gesuch;
        query.where(gesuch.gesuchStatus.notIn(toExclude));
        return query;
    }

    private JPAQuery<Gesuch> addMeineFilter(final UUID benutzerId, final JPAQuery<Gesuch> query) {
        final var gesuch = QGesuch.gesuch;
        final var zuordnung = QZuordnung.zuordnung;

        query.join(zuordnung).on(gesuch.fall.id.eq(zuordnung.fall.id))
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
            .join(tranche).on(tranche.gesuch.id.eq(gesuch.id))
            .join(formular).on(formular.tranche.id.eq(tranche.id))
            .join(pia).on(formular.personInAusbildung.id.eq(pia.id))
            .join(gesuchsperiode).on(gesuch.gesuchsperiode.id.eq(gesuchsperiode.id))
            .where(
                gesuch.id.in(
                    JPAExpressions
                        .select(gesuch.id)
                        .from(gesuch)
                        .join(tranche).on(tranche.gesuch.id.eq(gesuch.id))
                        .join(formular).on(formular.tranche.id.eq(tranche.id))
                        .join(pia).on(formular.personInAusbildung.id.eq(pia.id))
                        .join(gesuchsperiode).on(gesuch.gesuchsperiode.id.eq(gesuchsperiode.id))
                        .orderBy(gesuchsperiode.gesuchsperiodeStart.desc(), gesuch.fall.id.asc())
                        .limit(1)
                )
            )
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
            .join(gesuchTranche).on(gesuchTranche.gesuch.id.eq(gesuch.id))
            .where(gesuchTranche.id.eq(gesuchTrancheId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }
}
