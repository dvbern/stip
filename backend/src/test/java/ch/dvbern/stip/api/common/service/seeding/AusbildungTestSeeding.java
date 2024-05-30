package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.repo.BildungsartRepository;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.util.TestConstants;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AusbildungTestSeeding extends Seeder {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final BildungsartRepository bildungsartRepository;

    private Bildungsart bildungsart;

    @Override
    @Startup
    public void startup() {
        seed();
    }

    @Override
    protected void doSeed() {
		createBildungsart();
        seedUni();
        seedFh();
    }

    @Override
    protected List<String> getProfiles() {
        return List.of("test");
    }

    protected void createBildungsart() {
        final var art = new Bildungsart()
			.setBeschreibung("Test Beschreibung")
			.setBfs(1)
			.setBildungsstufe(Bildungsstufe.TERTIAER);

        bildungsartRepository.persistAndFlush(art);
        bildungsart = art;
		TestConstants.TEST_BILDUNGSART_ID = bildungsart.getId();
    }

    private void seedUni() {
        final var uniBern = new Ausbildungsstaette()
            .setNameDe("Uni Bern")
            .setNameFr("Uni Berne");

        ausbildungsstaetteRepository.persistAndFlush(uniBern);
        TestConstants.TEST_AUSBILDUNGSSTAETTE_ID = uniBern.getId();

        final var uniBeGang1 = (Ausbildungsgang) new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setBildungsart(bildungsart)
            .setAusbildungsstaette(uniBern);

        final var uniBeGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
			.setBildungsart(bildungsart)
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
			.setBildungsart(bildungsart)
            .setAusbildungsstaette(bfh);

        final var bfhGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
			.setBildungsart(bildungsart)
            .setAusbildungsstaette(bfh);

        ausbildungsgangRepository.persist(List.of(bfhGang1, bfhGang2));
    }
}
