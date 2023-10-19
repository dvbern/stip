package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.test.generator.entities.GesuchGenerator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.*;
import static ch.dvbern.stip.test.generator.entities.GesuchGenerator.initGesuchTranche;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@Slf4j
class GesuchServiceTest {

	@Inject
	GesuchService gesuchService;

	@Inject
	GesuchTrancheMapper gesuchTrancheMapper;

	@Inject
	ElternMapper elternMapper;

	@Inject
	LebenslaufItemMapper lebenslaufItemMapper;

	@InjectMock
	GesuchRepository gesuchRepository;

	@Test
	void testVerheiratetToLedigShouldResetPartner() {
		final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, LEDIG);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
	}

	@Test
	void testLedigToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != LEDIG) {
				final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
				GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, LEDIG, zivilstand);
				MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerwitwetToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != VERWITWET) {
				final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
				GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERWITWET, zivilstand);
				MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testGeschiedenToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != GESCHIEDEN_GERICHTLICH) {
				final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
				GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, GESCHIEDEN_GERICHTLICH, zivilstand);
				MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerheiratetToKonkubinatEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testVerheiratetToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToKonkubinatVerheiratetShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, VERHEIRATET }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testkonkubinatToVerheiratetEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { VERHEIRATET, EINGETRAGENE_PARTNERSCHAFT }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testKonkubinatToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testAufgeloestePartnerschaftToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != AUFGELOESTE_PARTNERSCHAFT) {
				final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
				GesuchTranche tranche =
						updateFromZivilstandToZivilstand(gesuchUpdateDto, AUFGELOESTE_PARTNERSCHAFT, zivilstand);
				MatcherAssert.assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void resetElternDataIfChangeFromMutterToGemeinsam() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(), Matchers.not(0));
		GesuchTranche tranche = updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.MUTTER, Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(0));
	}

	@Test
	void resetElternDataIfChangeFromVaterToGemeinsam() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(), Matchers.not(0));
		GesuchTranche tranche = updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.VATER, Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(0));
	}

	@Test
	void noResetElternDataIfNoChangeGemeinsam() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		MatcherAssert.assertThat(gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(), Matchers.not(0));
		var anzahlElternBevoreUpdate = gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size();

		GesuchTranche tranche = updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.GEMEINSAM,
						Elternschaftsteilung.GEMEINSAM);
		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getElterns().size(),
				Matchers.is(anzahlElternBevoreUpdate));
	}

	@Test
	void noResetElternDataIfChangeToEltrenschaftAufteilungNotGemeinsam() {
		for (var elternschaftsteilung : Elternschaftsteilung.values()) {
			GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
			MatcherAssert.assertThat(
					gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(),
					Matchers.not(0));
			var anzahlElternBevoreUpdate = gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size();

			GesuchTranche tranche = updateWerZahltAlimente(gesuchUpdateDto, elternschaftsteilung, Elternschaftsteilung.MUTTER);
			MatcherAssert.assertThat(
					tranche.getGesuchFormular().getElterns().size(),
					Matchers.is(anzahlElternBevoreUpdate));
		}
	}

	@Test
	void noResetIfElternStayZusammen() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
		var anzahlElternBevoreUpdate = gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size();
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
		tranche.getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(anzahlElternBevoreUpdate));
	}

	@Test
	void noResetIfNotUnbekanntOrVerstorben() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
		tranche.getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(false);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternteilUnbekanntVerstorben(false);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
	}

	@Test
	void resetMutterIfUnbekannt() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.UNBEKANNT, ElternAbwesenheitsGrund.WEDER_NOCH);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
	}

	@Test
	void resetMutterIfVerstorben() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.VERSTORBEN, ElternAbwesenheitsGrund.WEDER_NOCH);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
	}

	@Test
	void resetVaterIfUnbekannt() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.WEDER_NOCH, ElternAbwesenheitsGrund.UNBEKANNT);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
	}

	@Test
	void resetVaterIfVerstorben() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.WEDER_NOCH, ElternAbwesenheitsGrund.VERSTORBEN);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
	}

	@Test
	void resetBothIfVerstorben() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.VERSTORBEN, ElternAbwesenheitsGrund.VERSTORBEN);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
	}

	@Test
	void resetBothIfUnbekannt() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);

		GesuchTranche tranche = updateElternteilUnbekanntVerstorben(gesuchUpdateDto, ElternAbwesenheitsGrund.UNBEKANNT, ElternAbwesenheitsGrund.UNBEKANNT);

		MatcherAssert.assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
		MatcherAssert.assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
	}

	@Test
	void resetAlimenteIfGesetzlicheAlimenteregelungFromNoFamsitToTrue() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(null);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(BigDecimal.valueOf(1000));
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(new FamiliensituationUpdateDto());
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(false);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(true);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
	}

	@Test
	void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToTrue() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(BigDecimal.valueOf(1000));

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, true, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
	}

	@Test
	void resetAlimenteIfGesetzlicheAlimenteregelungFromFalseToTrue() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(BigDecimal.valueOf(1000));

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, true, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
	}

	@Test
	void noResetAlimenteIfGesetzlicheAlimenteregelungFromTrueToTrue() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		BigDecimal alimente = BigDecimal.valueOf(1000);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(alimente);

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, true, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.is(alimente));
	}

	@Test
	void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToFalse() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(BigDecimal.valueOf(1000));

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, false, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
	}

	@Test
	void resetAlimenteIfGesetzlicheAlimenteregelungFromTrueToFalse() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(BigDecimal.valueOf(1000));

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, false, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
	}

	@Test
	void noResetAlimenteIfGesetzlicheAlimenteregelungFromFalseToFalse() {
		GesuchUpdateDto gesuchUpdateDto =  GesuchGenerator.createGesuch();
		BigDecimal alimente = BigDecimal.valueOf(1000);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(alimente);

		GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, false, gesuchUpdateDto);

		MatcherAssert.assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.is(alimente));
	}

	@Test
	void noResetAuswertigesMittagessenIfPersonIsAusbildungIsNullAfterUpdate() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(1);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
				Matchers.is(1));
	}

	@Test
	void noResetAuswertigesMittagessenIfPersonIsAusbildungIsNull() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(1);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
				Matchers.is(1));
	}

	@Test
	void noResetAuswertigesMittagessenIfWohnsitzFamilie() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(1);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilMutter(null);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilVater(null);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
				Matchers.is(1));
	}

	@Test
	void noResetAuswertigesMittagessenIfWohnsitzMutterVater() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(1);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
				Matchers.is(1));
	}

	@Test
	void resetAuswertigesMittagessenIfWohnsitzEigenerHaushalt() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(1);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilMutter(null);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilVater(null);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
				Matchers.nullValue());
	}

	@Test
	void einnahmenKostenNullResetAuswertigesMittagessen() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setEinnahmenKosten(null);

		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilMutter(null);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilVater(null);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getEinnahmenKosten(),
				Matchers.nullValue());
	}

	@Test
	void noResetLebenslaufIfGebrutsdatumNotChanged() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(1));

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(1));
	}

	@Test
	void resetLebenslaufIfGebrutsdatumChanged() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(1));

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(LocalDate.of(2000, 10, 11));
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(0));
	}

	@Test
	void noResetLebenslaufIfNoUpdateOfPersonInAusbildung() {
		GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
		GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(1));

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		MatcherAssert.assertThat(
				tranche.getGesuchFormular().getLebenslaufItems().size(),
				Matchers.is(1));
	}



	private GesuchTranche initTrancheFromGesuchUpdate(GesuchUpdateDto gesuchUpdateDto) {
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		return gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
	}

	private GesuchTranche updateGesetzlicheAlimenteRegel(@Nullable Boolean from, Boolean to, GesuchUpdateDto gesuchUpdateDto) {
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(from);
		GesuchTranche trache = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), trache);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(to);
		if (to) {
			gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);
		}

		when(gesuchRepository.requireById(any())).thenReturn(trache.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		return trache;
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

	private GesuchTranche updateElternteilUnbekanntVerstorben(GesuchUpdateDto gesuchUpdateDto, ElternAbwesenheitsGrund grundMutter, ElternAbwesenheitsGrund grundVater) {
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(false);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternteilUnbekanntVerstorben(true);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setMutterUnbekanntVerstorben(grundMutter);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setVaterUnbekanntVerstorben(grundVater);

		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		return tranche;
	}

	private GesuchTranche updateWerZahltAlimente(
			GesuchUpdateDto gesuchUpdateDto,
			Elternschaftsteilung from,
			Elternschaftsteilung to) {
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(true);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setWerZahltAlimente(from);
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);

		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setWerZahltAlimente(to);
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		return tranche;
	}

	private GesuchTranche updateFromZivilstandToZivilstand(GesuchUpdateDto gesuchUpdateDto, Zivilstand from, Zivilstand to) {
		GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPartner(new PartnerUpdateDto());
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(from);
		gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
		gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(to);
		when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
		gesuchService.updateGesuch(any(), gesuchUpdateDto);
		return tranche;
	}

	private GesuchTranche prepareGesuchTrancheWithIds(GesuchTrancheUpdateDto trancheUpdate){
		GesuchTranche tranche = initGesuchTranche();
		GesuchFormular gesuchFormular = new GesuchFormular();

		trancheUpdate.getGesuchFormular().getElterns().forEach(elternUpdateDto -> {
			elternUpdateDto.setId(UUID.randomUUID());
			gesuchFormular.getElterns().add(elternMapper.partialUpdate(elternUpdateDto, new Eltern()));
		});

		trancheUpdate.getGesuchFormular().getLebenslaufItems().forEach(item -> {
			item.setId(UUID.randomUUID());
			gesuchFormular.getLebenslaufItems().add(lebenslaufItemMapper.partialUpdate(item, new LebenslaufItem()));
		});

		return tranche.setGesuchFormular(gesuchFormular);
	}
}
