package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.common.type.Bildungsart;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class AusbildungSeeding extends Seeder {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;

    @Override
    @Startup
    void startup() {
        seed();
    }

    @Override
    void doSeed() {
        if (ausbildungsstaetteRepository.count() == 0) {
            LOG.info("Seeding Uni and FH");
            seedUni();
            seedFh();
        }
    }

    private void seedUni() {
        final var uniBern = new Ausbildungsstaette()
            .setNameDe("Universität Bern")
            .setNameFr("Université de Berne");

        ausbildungsstaetteRepository.persistAndFlush(uniBern);

        final var uniBeGang1 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setAusbildungsrichtung(Bildungsart.UNIVERSITAETEN_ETH)
            .setAusbildungsstaette(uniBern);

        final var uniBeGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setAusbildungsrichtung(Bildungsart.UNIVERSITAETEN_ETH)
            .setAusbildungsstaette(uniBern);

        ausbildungsgangRepository.persist(List.of(uniBeGang1, uniBeGang2));
    }

    private void seedFh() {
        final var bfh = new Ausbildungsstaette()
            .setNameDe("Berner Fachhochschule")
            .setNameFr("Haute école spécialisée bernoise");

        ausbildungsstaetteRepository.persistAndFlush(bfh);

        final var bfhGang1 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setAusbildungsrichtung(Bildungsart.FACHHOCHSCHULEN)
            .setAusbildungsstaette(bfh);

        final var bfhGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setAusbildungsrichtung(Bildungsart.FACHHOCHSCHULEN)
            .setAusbildungsstaette(bfh);

        ausbildungsgangRepository.persist(List.of(bfhGang1, bfhGang2));
    }
}
