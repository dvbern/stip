package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
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

import java.util.Optional;
import java.util.Set;

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

	@Test
	void noResetIfElternStayZusammen() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);
		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.not(0));
		var anzahlElternBevoreUpdate = gesuch.getGesuchFormularToWorkWith().getElterns().size();

		GesuchUpdateDto gesuchUpdateDto = gesuchMapper.toGesuchUpdateDto(gesuch);

		when(gesuchRepository.requireById(any())).thenReturn(gesuch);
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), Matchers.is(anzahlElternBevoreUpdate));
	}

	@Test
	void noResetIfNotUnbekanntOrVerstorben() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		GesuchUpdateDto gesuchUpdateDto = gesuchMapper.toGesuchUpdateDto(gesuch);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(false);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setElternteilUnbekanntVerstorben(false);

		when(gesuchRepository.requireById(any())).thenReturn(gesuch);
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
	}

	@Test
	void resetMutterIfUnbekannt() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.UNBEKANNT, ElternAbwesenheitsGrund.WEDER_NOCH);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
	}

	@Test
	void resetMutterIfVerstorben() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.VERSTORBEN, ElternAbwesenheitsGrund.WEDER_NOCH);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
	}

	@Test
	void resetVaterIfUnbekannt() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.WEDER_NOCH, ElternAbwesenheitsGrund.UNBEKANNT);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
	}

	@Test
	void resetVaterIfVerstorben() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.WEDER_NOCH, ElternAbwesenheitsGrund.VERSTORBEN);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
	}

	@Test
	void resetBothIfVerstorben() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.VERSTORBEN, ElternAbwesenheitsGrund.VERSTORBEN);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
	}

	@Test
	void resetBothIfUnbekannt() {
		Gesuch gesuch = GesuchGenerator.createGesuch();
		gesuch.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(true);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(true));

		updateElternteilUnbekanntVerstorben(gesuch, ElternAbwesenheitsGrund.UNBEKANNT, ElternAbwesenheitsGrund.UNBEKANNT);

		MatcherAssert.assertThat(hasMutter(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(gesuch.getGesuchFormularToWorkWith().getElterns()), Matchers.is(false));
	}


	private boolean hasMutter(Set<Eltern> elterns) {
		return getElternFromElternsByElternTyp(elterns, ElternTyp.MUTTER).isPresent();
	}

	private boolean hasVater(Set<Eltern> elterns) {
		return getElternFromElternsByElternTyp(elterns, ElternTyp.VATER).isPresent();
	}

	private Optional<Eltern> getElternFromElternsByElternTyp(Set<Eltern> elterns, ElternTyp elternTyp) {
		return elterns.stream()
				.filter(eltern -> eltern.getElternTyp() == elternTyp)
				.findFirst();
	}

	private void updateElternteilUnbekanntVerstorben(Gesuch gesuch, ElternAbwesenheitsGrund grundMutter, ElternAbwesenheitsGrund grundVater) {
		GesuchUpdateDto gesuchUpdateDto = gesuchMapper.toGesuchUpdateDto(gesuch);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setElternVerheiratetZusammen(false);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setElternteilUnbekanntVerstorben(true);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setMutterUnbekanntVerstorben(grundMutter);
		gesuchUpdateDto.getGesuchFormularToWorkWith().getFamiliensituation().setVaterUnbekanntVerstorben(grundVater);

		when(gesuchRepository.requireById(any())).thenReturn(gesuch);
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
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
