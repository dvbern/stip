package ch.dvbern.stip.api.zuordnung.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.BuchstabenRangeUtility;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class ZuordnungService {
    private final SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository;
    private final ZuordnungRepository zuordnungRepository;
    private final GesuchRepository gesuchRepository;

    public void updateZuordnungOnFall() {
        final var newestWithPia = gesuchRepository.findAllNewestWithPia().toList();
        LOG.info(newestWithPia.toString());

        // Load all StammdatenSachbearbeiterZuordnungen from DB
        final var stammdaten = sachbearbeiterZuordnungStammdatenRepository.findAll().stream().toList();
        final var zuordnungen = zuordnungRepository
            .findAllWithType(ZuordnungType.AUTOMATIC)
            .collect(Collectors.toMap(zuordnung -> zuordnung.getFall().getId(), zuordnung -> zuordnung));

        final var newZuordnungen = new ArrayList<Zuordnung>();

        // Stream through all Gesuche with a PiA attached
        newestWithPia.forEach(newest -> {
            // Find the SB to assign by PiA's last name and find potentially existing Zuordnung
            final var sbToAssign = findSbToAssign(stammdaten, getPia(newest));
            if (sbToAssign.isEmpty()) {
                return;
            }

            var zuordnung = zuordnungen.remove(newest.getFall().getId());
            if (zuordnung == null) {
                // If none exists, create a new one and add it to the list to persist
                zuordnung = new Zuordnung()
                    .setZuordnungType(ZuordnungType.AUTOMATIC)
                    .setFall(newest.getFall())
                    .setSachbearbeiter(sbToAssign.get());

                newZuordnungen.add(zuordnung);
            } else {
                // If one exists, update it
                zuordnung
                    .setSachbearbeiter(sbToAssign.get())
                    .setZuordnungType(ZuordnungType.AUTOMATIC);
            }
        });

        // Persist the new Zuordnungen
        zuordnungRepository.persist(newZuordnungen);

        // All existing automatic Zuordnungen should have been edited, if not, then at least one Fall has no bearbeiter
        // In that case, drop those, so we can easily query them
        if (!zuordnungen.isEmpty()) {
            zuordnungRepository.deleteByFallIds(zuordnungen.keySet());
        }
    }

    private Optional<Benutzer> findSbToAssign(
        final List<SachbearbeiterZuordnungStammdaten> stammdaten,
        final PersonInAusbildung pia
    ) {
        return stammdaten.stream().filter(x -> {
                final var range = pia.getKorrespondenzSprache() == Sprache.DEUTSCH ?
                    x.getBuchstabenDe() :
                    x.getBuchstabenFr();

                return BuchstabenRangeUtility.contains(range, pia.getNachname());
            })
            .map(SachbearbeiterZuordnungStammdaten::getBenutzer)
            .findFirst();
    }

    private PersonInAusbildung getPia(final Gesuch gesuch) {
        final var newest = gesuch.getNewestGesuchTranche();
        // No need for extra null checks, as the query that gets these implicitly does that
        return newest.map(gesuchTranche -> gesuchTranche.getGesuchFormular().getPersonInAusbildung()).orElse(null);

    }
}
