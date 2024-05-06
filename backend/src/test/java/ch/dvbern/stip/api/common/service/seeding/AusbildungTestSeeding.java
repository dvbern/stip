package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.common.type.Bildungsart;
import ch.dvbern.stip.api.util.TestConstants;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AusbildungTestSeeding extends Seeder {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;

    @Override
    @Startup
    public void startup() {
        seed();
    }

    @Override
    protected void doSeed() {
        seedUni();
        seedFh();
    }

    @Override
    protected List<String> getProfiles() {
        return List.of("test");
    }

    private void seedUni() {
        final var uniBern = new Ausbildungsstaette()
            .setNameDe("Uni Bern")
            .setNameFr("Uni Berne");

        ausbildungsstaetteRepository.persistAndFlush(uniBern);

        final var uniBeGang1 = (Ausbildungsgang) new Ausbildungsgang()
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
        TestConstants.TEST_AUSBILDUNGSGANG_ID = ausbildungsgangRepository.findAll().firstResult().getId();
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
