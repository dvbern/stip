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

    @Override
    @Startup
    public void startup() {
        seed();
    }

    @Override
    protected void doSeed() {
        if (ausbildungsstaetteRepository.count() == 0) {
            LOG.info("Seeding Uni and FH");

            final var bildungsarten = getBildungsarten();
            bildungsartRepository.persist(bildungsarten);
            bildungsartRepository.flush();

            final var ausbildungsstaetten = getAusbildungsstaette();
            ausbildungsstaetteRepository.persist(ausbildungsstaetten);
            ausbildungsstaetteRepository.flush();

            ausbildungsgangRepository.persist(getAusbildungsgaenge(
                ausbildungsstaetten,
                bildungsarten
            ));
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    private static List<Bildungsart> getBildungsarten() {
        return List.of(
            // Keep this order, or update dependencies as well
            createBildungsart("Universität", -2, Bildungsstufe.TERTIAER),
            createBildungsart("Gymnasium", -1, Bildungsstufe.SEKUNDAR_2)
        );
    }

    private static Bildungsart createBildungsart(
        final String beschreibung,
        final int bfs,
        final Bildungsstufe bildungsstufe
    ) {
        return new Bildungsart()
            .setBeschreibung(beschreibung)
            .setBildungsstufe(bildungsstufe)
            .setBfs(bfs);
    }

    private static List<Ausbildungsstaette> getAusbildungsstaette() {
        return List.of(
            // Keep this order, or update dependencies as well
            createAusbildungsstaette("Berner Fachhochschule", "Haute école spécialisée bernoise"),
            createAusbildungsstaette("Universität Bern", "Université de Berne"),
            createAusbildungsstaette("Lehrbetrieb", "Établissement"),
            createAusbildungsstaette("Gymnasium Lebermatt", "Lycée Lebermatt"),
            createAusbildungsstaette("BFF Bern", "BFF Berne"),
            createAusbildungsstaette("Universität Lausanne", "Université Lausanne")
        );
    }

    private static Ausbildungsstaette createAusbildungsstaette(final String nameDe, final String nameFr) {
        return new Ausbildungsstaette()
            .setNameDe(nameDe)
            .setNameFr(nameFr);
    }

    private static List<Ausbildungsgang> getAusbildungsgaenge(
        final List<Ausbildungsstaette> ausbildungsstaetten,
        final List<Bildungsart> bildungsarten
    ) {
        return List.of(
            createAusbildungsgang("Bachelor", "Bachelor", ausbildungsstaetten.get(0), bildungsarten.get(0)),
            createAusbildungsgang("Bachelor", "Bachelor", ausbildungsstaetten.get(1), bildungsarten.get(0)),
            createAusbildungsgang("Master", "Master", ausbildungsstaetten.get(1), bildungsarten.get(0)),
            createAusbildungsgang("Lehre EBA", "Apprentissage AFP", ausbildungsstaetten.get(2), bildungsarten.get(1)),
            createAusbildungsgang("Vorlehre", "Préapprentissage", ausbildungsstaetten.get(2), bildungsarten.get(1)),
            createAusbildungsgang("Maturität", "Maturité", ausbildungsstaetten.get(3), bildungsarten.get(1)),
            createAusbildungsgang("Lehre EFZ", "Apprentissage CFC", ausbildungsstaetten.get(2), bildungsarten.get(1)),
            createAusbildungsgang("Berufsvorbereitendes Schuljahr", "Année scolaire de préparation professionnelle", ausbildungsstaetten.get(4), bildungsarten.get(1))
        );
    }

    private static Ausbildungsgang createAusbildungsgang(
        final String bezeichnungDe,
        final String bezeichnungFr,
        final Ausbildungsstaette ausbildungsstaette,
        final Bildungsart bildungsart
    ) {
        return new Ausbildungsgang()
            .setBezeichnungDe(bezeichnungDe)
            .setBezeichnungFr(bezeichnungFr)
            .setAusbildungsstaette(ausbildungsstaette)
            .setBildungsart(bildungsart);
    }
}
