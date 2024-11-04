package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardGesuchMapper {
    public SbDashboardGesuchDto toDto(final Gesuch gesuch, final GesuchTrancheTyp typ) {
        return switch (typ) {
            case TRANCHE -> toDto(gesuch, gesuch.getLatestGesuchTranche(), typ);
            case AENDERUNG -> toDto(gesuch, gesuch.getAenderungZuUeberpruefen().orElseThrow(), typ);
        };
    }

    SbDashboardGesuchDto toDto(final Gesuch gesuch, final GesuchTranche gesuchTranche, final GesuchTrancheTyp typ) {
        final var target = new SbDashboardGesuchDto();
        target.setId(gesuch.getId());
        target.setGesuchTrancheId(gesuchTranche.getId());
        target.setFallNummer(gesuch.getFall().getFallNummer());
        target.setTyp(gesuchTranche.getTyp());

        final var pia = gesuchTranche.getGesuchFormular().getPersonInAusbildung();
        if (pia != null) {
            target.setPiaVorname(pia.getVorname());
            target.setPiaNachname(pia.getNachname());
            target.setPiaGeburtsdatum(pia.getGeburtsdatum());
        } else {
            LOG.warn("Gesuch was loaded to SB Dashboard that had no PiA");
        }

        target.setGesuchStatus(gesuch.getGesuchStatus());
        target.setTrancheStatus(gesuchTranche.getStatus());

        final var zuordnung = gesuch.getFall().getSachbearbeiterZuordnung();
        if (zuordnung != null) {
            target.setBearbeiter(zuordnung.getSachbearbeiter().getFullName());
        }
        target.setLetzteAktivitaet(gesuch.getTimestampMutiert().toLocalDate());

        return target;
    }
}
