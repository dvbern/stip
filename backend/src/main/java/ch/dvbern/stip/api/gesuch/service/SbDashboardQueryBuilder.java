package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.time.LocalTime;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardQueryBuilder {
    private final QGesuch gesuch = QGesuch.gesuch;
    private final QGesuchFormular formular = QGesuchFormular.gesuchFormular;

    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;

    public JPAQuery<Gesuch> baseQuery(final int page, final int pageSize, final GetGesucheSBQueryType queryType) {
        final var meId = benutzerService.getCurrentBenutzer().getId();

        final var query = switch (queryType) {
            case ALLE_BEARBEITBAR -> gesuchRepository.getFindAlleBearbeitbarQuery();
            case ALLE_BEARBEITBAR_MEINE -> gesuchRepository.getFindAlleMeineBearbeitbar(meId);
            case ALLE_MEINE -> gesuchRepository.getFindAlleMeine(meId);
            case ALLE -> gesuchRepository.getFindAlleQuery();
        };

        return query.offset((long) pageSize * page).limit(pageSize);
    }

    public void fallNummer(final JPAQuery<Gesuch> query, final String fallNummer) {
        query.where(gesuch.fall.fallNummer.like(fallNummer));
    }

    public void piaNachname(final JPAQuery<Gesuch> query, final String nachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.like(nachname));
    }

    public void piaVorname(final JPAQuery<Gesuch> query, final String vorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.like(vorname));
    }

    void joinFormular(final JPAQuery<Gesuch> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query.join(formular).on(gesuch.latestGesuchTranche.gesuchFormular.id.eq(formular.id));
    }

    public void piaGeburtsdatum(final JPAQuery<Gesuch> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void status(final JPAQuery<Gesuch> query, final Gesuchstatus status) {
        query.where(gesuch.gesuchStatus.eq(status));
    }

    public void bearbeiter(final JPAQuery<Gesuch> query, final String bearbeiter) {
        query.where(gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname.like(bearbeiter)
            .or(gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.vorname.like(bearbeiter)));
    }

    public void letzteAktivitaet(
        final JPAQuery<Gesuch> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(gesuch.timestampMutiert.goe(from.atStartOfDay())
            .and(gesuch.timestampMutiert.loe(to.atTime(LocalTime.MAX))));
    }

    public void orderBy(final JPAQuery<Gesuch> query, final SbDashboardColumn column, final SortOrder sortOrder) {
        final var fieldSpecified = switch (column) {
            case FALLNUMMER -> gesuch.fall.fallNummer;
            case TYP -> throw new NotImplementedException();
            case PIA_NACHNAME -> gesuch.latestGesuchTranche.gesuchFormular.personInAusbildung.nachname;
            case PIA_VORNAME -> gesuch.latestGesuchTranche.gesuchFormular.personInAusbildung.vorname;
            case PIA_GEBURTSDATUM -> gesuch.latestGesuchTranche.gesuchFormular.personInAusbildung.geburtsdatum;
            case STATUS -> gesuch.gesuchStatus;
            case BEARBEITER -> gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname;
            case LETZTE_AKTIVITAET -> gesuch.timestampMutiert;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }
}
