package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.repo.BildungsartRepository;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.config.service.ConfigService;
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
    private final BildungsartRepository bildungsartRepository;
    private final ConfigService configService;

    protected Bildungsart bildungsartSekundarstufeZwei;
    protected Bildungsart bildungsartTertiaer;

    @Override
    @Startup
    public void startup() {
        seed();
    }

    @Override
    protected void doSeed() {
        if (ausbildungsstaetteRepository.count() == 0) {
            LOG.info("Seeding Uni and FH");

            createBildungsarten();
            seedUni();
            seedFh();
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    protected void createBildungsarten() {
        final var artSek = new Bildungsart()
            .setBeschreibung("Gymnasium")
            .setBfs(-1)
            .setBildungsstufe(Bildungsstufe.SEKUNDAR_2);
        final var artTertiaer = new Bildungsart()
            .setBeschreibung("Universität")
            .setBfs(-2)
            .setBildungsstufe(Bildungsstufe.TERTIAER);

        bildungsartRepository.persistAndFlush(artSek);
        bildungsartRepository.persistAndFlush(artTertiaer);
        bildungsartSekundarstufeZwei = artSek;
        bildungsartTertiaer = artTertiaer;
    }

    protected void seedUni() {
        final var uniBern = new Ausbildungsstaette()
            .setNameDe("Universität Bern")
            .setNameFr("Université de Berne");

        ausbildungsstaetteRepository.persistAndFlush(uniBern);

        final var uniBeGang1 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setBildungsart(bildungsartTertiaer)
            .setAusbildungsstaette(uniBern);

        final var uniBeGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setBildungsart(bildungsartTertiaer)
            .setAusbildungsstaette(uniBern);

        ausbildungsgangRepository.persist(List.of(uniBeGang1, uniBeGang2));
    }

    protected void seedFh() {
        final var bfh = new Ausbildungsstaette()
            .setNameDe("Berner Fachhochschule")
            .setNameFr("Haute école spécialisée bernoise");

        ausbildungsstaetteRepository.persistAndFlush(bfh);

        final var bfhGang1 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setBildungsart(bildungsartTertiaer)
            .setAusbildungsstaette(bfh);

        final var bfhGang2 = new Ausbildungsgang()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setBildungsart(bildungsartTertiaer)
            .setAusbildungsstaette(bfh);

        final var bfhGang3 = new Ausbildungsgang()
            .setBezeichnungDe("Gymnasium")
            .setBezeichnungFr("Gymnasium")
            .setBildungsart(bildungsartSekundarstufeZwei)
            .setAusbildungsstaette(bfh);

        ausbildungsgangRepository.persist(List.of(bfhGang1, bfhGang2, bfhGang3));
    }
}
