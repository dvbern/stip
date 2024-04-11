package ch.dvbern.stip.api.zuordnung.service;

import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
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

//         Load all StammdatenSachbearbeiterZuordnungen from DB
        final var stammdaten = sachbearbeiterZuordnungStammdatenRepository.findAll().stream().toList();
        final var zuordnungen = zuordnungRepository.findAllWithType(ZuordnungType.AUTOMATIC);

        // Stream through all Faelle (special projection? only have the Zuordnung and the PiA Name loaded?)
        zuordnungen.forEach(zuordnung -> {
//            zuordnung.getFall().get
        });



        // Set the Zuordnung accordingly
    }
}
