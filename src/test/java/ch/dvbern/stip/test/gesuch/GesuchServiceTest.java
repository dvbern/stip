package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.test.util.GesuchGenerator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@Slf4j
class GesuchServiceTest {

	@Inject
	GesuchService gesuchService;

	@Inject
	GesuchMapper gesuchMapper;

	@InjectMock
	GesuchRepository gesuchRepository;

	@Test
	void testVerheiratetToLedigShouldResetPartner() {
		final Gesuch gesuch = GesuchGenerator.createGesuch();
		updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, LEDIG);

		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
	}

	@Test
	void testLedigToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != LEDIG) {
				final Gesuch gesuch = GesuchGenerator.createGesuch();
				updateFromZivilstandToZivilstand(gesuch, LEDIG, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerwitwetToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != VERWITWET) {
				final Gesuch gesuch = GesuchGenerator.createGesuch();
				updateFromZivilstandToZivilstand(gesuch, VERWITWET, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testGeschiedenToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != GESCHIEDEN_GERICHTLICH) {
				final Gesuch gesuch = GesuchGenerator.createGesuch();
				updateFromZivilstandToZivilstand(gesuch, GESCHIEDEN_GERICHTLICH, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerheiratetToKonkubinatEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testVerheiratetToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToKonkubinatVerheiratetShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, VERHEIRATET }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testkonkubinatToVerheiratetEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { VERHEIRATET, EINGETRAGENE_PARTNERSCHAFT }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testKonkubinatToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = GesuchGenerator.createGesuch();
			updateFromZivilstandToZivilstand(gesuch, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testAufgeloestePartnerschaftToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != AUFGELOESTE_PARTNERSCHAFT) {
				final Gesuch gesuch = GesuchGenerator.createGesuch();
				updateFromZivilstandToZivilstand(gesuch, AUFGELOESTE_PARTNERSCHAFT, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void resetElternDataIfChangeFromMutterToGemeinsam() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.not(0));
		updateWerZahltAlimente(gesuch, Elternschaftsteilung.MUTTER, Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.is(0));
	}

	@Test
	void resetElternDataIfChangeFromVaterToGemeinsam() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.not(0));
		updateWerZahltAlimente(gesuch, Elternschaftsteilung.VATER, Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.is(0));
	}

	@Test
	void noResetElternDataIfNoChangeGemeinsam() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.not(0));
		var anzahlElternBevoreUpdate = gesuch.getGesuchFormularToWorkWith().getElterns().size();

		updateWerZahltAlimente(gesuch, Elternschaftsteilung.GEMEINSAM, Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.is(anzahlElternBevoreUpdate));
	}

	@Test
	void noResetElternDataIfChangeToEltrenschaftAufteilungNotGemeinsam() {
		for (var elternschaftsteilung : Elternschaftsteilung.values()) {
			Gesuch gesuch = GesuchGenerator.createGesuch();
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.not(0));
			var anzahlElternBevoreUpdate = gesuch.getGesuchFormularToWorkWith().getElterns().size();

			updateWerZahltAlimente(gesuch, elternschaftsteilung, Elternschaftsteilung.MUTTER);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.is(anzahlElternBevoreUpdate));
		}
	}

	private void updateWerZahltAlimente(Gesuch gesuch, Elternschaftsteilung from, Elternschaftsteilung to) {
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setGerichtlicheAlimentenregelung(true);
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setWerZahltAlimente(from);

		GesuchUpdateDto gesuchUpdateDto = gesuchMapper.toGesuchUpdateDto(gesuch);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setWerZahltAlimente(to);

		when(gesuchRepository.requireById(any())).thenReturn(gesuch);
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
	}

	private void updateFromZivilstandToZivilstand(Gesuch gesuch, Zivilstand from, Zivilstand to) {
		gesuch.getGesuchFormularToWorkWith().setPartner(GesuchGenerator.createPartner());
		gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().setZivilstand(from);
		final GesuchUpdateDto gesuchUpdateDto = gesuchMapper.toGesuchUpdateDto(gesuch);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getPersonInAusbildung().setZivilstand(to);
		when(gesuchRepository.requireById(any())).thenReturn(gesuch);

		gesuchService.updateGesuch(any(), gesuchUpdateDto);
	}
}
