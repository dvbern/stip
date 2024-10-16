package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.time.LocalTime;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardQueryBuilder {
    private final QGesuchFormular formular = QGesuchFormular.gesuchFormular;
    private final QGesuchTranche tranche = QGesuchTranche.gesuchTranche;

    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;

    public JPAQuery<GesuchTranche> baseQuery(final GetGesucheSBQueryType queryType) {
        final var meId = benutzerService.getCurrentBenutzer().getId();

        final var query = switch (queryType) {
            case ALLE_BEARBEITBAR -> gesuchRepository.getFindAlleBearbeitbarQuery();
            case ALLE_BEARBEITBAR_MEINE -> gesuchRepository.getFindAlleMeineBearbeitbarQuery(meId);
            case ALLE_MEINE -> gesuchRepository.getFindAlleMeineQuery(meId);
            case ALLE -> gesuchRepository.getFindAlleQuery();
        };

        return query.where(tranche.gesuchFormular.personInAusbildung.vorname.isNotNull()
            .and(tranche.gesuchFormular.personInAusbildung.nachname.isNotNull()));
    }

    public void fallNummer(final JPAQuery<GesuchTranche> query, final String fallNummer) {
        query.where(tranche.gesuch.fall.fallNummer.containsIgnoreCase(fallNummer));
    }

    public void piaNachname(final JPAQuery<GesuchTranche> query, final String nachname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.nachname.containsIgnoreCase(nachname));
    }

    public void piaVorname(final JPAQuery<GesuchTranche> query, final String vorname) {
        joinFormular(query);
        query.where(formular.personInAusbildung.vorname.containsIgnoreCase(vorname));
    }

    void joinFormular(final JPAQuery<GesuchTranche> query) {
        // This join is required, because QueryDSL doesn't init the path to PiA
        query.join(formular).on(tranche.gesuchFormular.id.eq(formular.id));
    }

    public void piaGeburtsdatum(final JPAQuery<GesuchTranche> query, final LocalDate geburtsdatum) {
        joinFormular(query);
        query.where(formular.personInAusbildung.geburtsdatum.eq(geburtsdatum));
    }

    public void status(final JPAQuery<GesuchTranche> query, final Gesuchstatus status) {
        if (status == Gesuchstatus.IN_BEARBEITUNG_SB) {
            query.where(tranche.status.eq(GesuchTrancheStatus.UEBERPRUEFEN));
        }

        query.where(tranche.gesuch.gesuchStatus.eq(status));
    }

    public void bearbeiter(final JPAQuery<GesuchTranche> query, final String bearbeiter) {
        query.where(tranche.gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname.containsIgnoreCase(bearbeiter)
            .or(tranche.gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.vorname.containsIgnoreCase(bearbeiter)));
    }

    public void letzteAktivitaet(
        final JPAQuery<GesuchTranche> query,
        final LocalDate from,
        final LocalDate to
    ) {
        query.where(tranche.gesuch.timestampMutiert.between(from.atStartOfDay(), to.atTime(LocalTime.MAX)));
    }

    public void typ(final JPAQuery<GesuchTranche> query, GesuchTrancheTyp typ) {
        query.where(tranche.typ.eq(typ));
    }

    public void orderBy(final JPAQuery<GesuchTranche> query, final SbDashboardColumn column, final SortOrder sortOrder) {
        final var fieldSpecified = switch (column) {
            case FALLNUMMER -> tranche.gesuch.fall.fallNummer;
            case TYP -> tranche.typ;
            case PIA_NACHNAME -> tranche.gesuchFormular.personInAusbildung.nachname;
            case PIA_VORNAME -> tranche.gesuchFormular.personInAusbildung.vorname;
            case PIA_GEBURTSDATUM -> tranche.gesuchFormular.personInAusbildung.geburtsdatum;
            case STATUS -> tranche.gesuch.gesuchStatus;
            case BEARBEITER -> tranche.gesuch.fall.sachbearbeiterZuordnung.sachbearbeiter.nachname;
            case LETZTE_AKTIVITAET -> tranche.gesuch.timestampMutiert;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<GesuchTranche> query) {
        query.orderBy(tranche.gesuch.timestampMutiert.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<GesuchTranche> query) {
        return query.clone().select(tranche.count());
    }

    public void paginate(final JPAQuery<GesuchTranche> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
