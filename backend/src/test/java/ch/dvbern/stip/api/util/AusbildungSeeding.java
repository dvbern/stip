package ch.dvbern.stip.api.util;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.repo.BildungsartRepository;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.service.seeding.Seeder;
import io.quarkus.runtime.Startup;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;

@QuarkusTest
@RequiredArgsConstructor
public class AusbildungSeeding extends Seeder {
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

	private void createBildungsart() {
		bildungsart = new Bildungsart();
		bildungsart.setBeschreibung("TEST");
		bildungsart.setBildungsstufe(Bildungsstufe.TERTIAER);
		bildungsart.setBfs(3);

		bildungsartRepository.persistAndFlush(bildungsart);
	}

	private void seedUni() {

		final var uniBern = new Ausbildungsstaette()
				.setNameDe("Uni Bern")
				.setNameFr("Uni Berne");

		ausbildungsstaetteRepository.persistAndFlush(uniBern);

		final var uniBeGang1 = (Ausbildungsgang) new Ausbildungsgang()
				.setBezeichnungDe("Bsc. Informatik")
				.setBezeichnungFr("Bsc. Informatique")
				.setBildungsart(bildungsart)
				.setAusbildungsstaette(uniBern)
				.setId(TestConstants.TEST_AUSBILDUNGSGANG_ID);

		final var uniBeGang2 = (Ausbildungsgang) new Ausbildungsgang()
				.setBezeichnungDe("Bsc. Biologie")
				.setBezeichnungFr("Bsc. Biologie")
				.setBildungsart(bildungsart)
				.setAusbildungsstaette(uniBern)
				.setId(TestConstants.TEST_AUSBILDUNGSGANG_ID);

		ausbildungsgangRepository.persist(List.of(uniBeGang1, uniBeGang2));
	}

	private void seedFh() {

		final var bfh = new Ausbildungsstaette()
				.setNameDe("Berner Fachhochschule")
				.setNameFr("Haute école spécialisée bernoise");

		ausbildungsstaetteRepository.persistAndFlush(bfh);

		final var bfhGang1 = (Ausbildungsgang) new Ausbildungsgang()
				.setBezeichnungDe("Bsc. Informatik")
				.setBezeichnungFr("Bsc. Informatique")
				.setBildungsart(bildungsart)
				.setAusbildungsstaette(bfh)
				.setId(TestConstants.TEST_AUSBILDUNGSGANG_ID);

		final var bfhGang2 = (Ausbildungsgang) new Ausbildungsgang()
				.setBezeichnungDe("Bsc. Biologie")
				.setBezeichnungFr("Bsc. Biologie")
				.setBildungsart(bildungsart)
				.setAusbildungsstaette(bfh)
				.setId(TestConstants.TEST_AUSBILDUNGSGANG_ID);

		ausbildungsgangRepository.persist(List.of(bfhGang1, bfhGang2));
	}
}
