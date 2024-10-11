package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardGesuchMapper {
    public List<SbDashboardGesuchDto> toDto(final Gesuch gesuch) {
        final var dtos = new ArrayList<SbDashboardGesuchDto>();
        // TODO KSTIP-1538: Check if this executes 2*tranchen queries
        gesuch.getOldestGesuchTranche().ifPresent(tranche -> dtos.add(toDto(gesuch, tranche)));
        gesuch.getAenderungZuUeberpruefen().ifPresent(tranche -> dtos.add(toDto(gesuch, tranche)));

        return dtos;
    }

    SbDashboardGesuchDto toDto(final Gesuch gesuch, final GesuchTranche gesuchTranche) {
        final var target = new SbDashboardGesuchDto();
        // TODO KSTIP-1538: Check if this executes 2*fall queries
        target.setId(gesuch.getId());
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

        target.setStatus(gesuch.getGesuchStatus());
        final var zuordnung = gesuch.getFall().getSachbearbeiterZuordnung();
        if (zuordnung != null) {
            target.setBearbeiter(zuordnung.getSachbearbeiter().getFullName());
        }
        target.setLetzteAktivitaet(gesuch.getTimestampMutiert().toLocalDate());

        return target;
    }
}
