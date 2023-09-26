package ch.dvbern.stip.test.gesuch;

import java.time.LocalDate;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.DateRange;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import ch.dvbern.stip.api.gesuch.service.GesuchService;

import static ch.dvbern.stip.api.common.type.Anrede.HERR;
import static ch.dvbern.stip.api.common.type.Wohnsitz.FAMILIE;
import static ch.dvbern.stip.api.personinausbildung.type.Sprache.DEUTSCH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.GESCHIEDEN_GERICHTLICH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.KONKUBINAT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERHEIRATET;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERWITWET;
import static ch.dvbern.stip.api.stammdaten.type.Land.CH;
import static ch.dvbern.stip.test.util.TestConstants.AHV_NUMMER_VALID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@Slf4j
class GesuchServiceTest {

	@Inject
	GesuchService gesuchService;

	@InjectMock
	GesuchRepository gesuchRepository;

	@Test
	void testVerheiratetToLedigShouldResetPartner() {
		final Gesuch gesuch = initMinimalGesuchWithZivilstand();
		updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, LEDIG);

		MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
	}

	@Test
	void testLedigToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != LEDIG) {
				final Gesuch gesuch = initMinimalGesuchWithZivilstand();
				updateFromZivilstandToZivilstand(gesuch, LEDIG, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerwitwetToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != VERWITWET) {
				final Gesuch gesuch = initMinimalGesuchWithZivilstand();
				updateFromZivilstandToZivilstand(gesuch, VERWITWET, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testGeschiedenToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != GESCHIEDEN_GERICHTLICH) {
				final Gesuch gesuch = initMinimalGesuchWithZivilstand();
				updateFromZivilstandToZivilstand(gesuch, GESCHIEDEN_GERICHTLICH, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	@Test
	void testVerheiratetToKonkubinatEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testVerheiratetToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, VERHEIRATET, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToKonkubinatVerheiratetShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { KONKUBINAT, VERHEIRATET }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testEingetragenePartnerschaftToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testkonkubinatToVerheiratetEingetragenePartnerschaftShouldNotResetPartner() {
		for (var zivilstand : new Zivilstand[] { VERHEIRATET, EINGETRAGENE_PARTNERSCHAFT }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
		}
	}

	@Test
	void testKonkubinatToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
		for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
			final Gesuch gesuch = initMinimalGesuchWithZivilstand();
			updateFromZivilstandToZivilstand(gesuch, KONKUBINAT, zivilstand);
			MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.nullValue());
		}
	}

	@Test
	void testAufgeloestePartnerschaftToEveryOtherZivilstandShouldNotResetPartner() {
		for (var zivilstand : Zivilstand.values()) {
			if (zivilstand != AUFGELOESTE_PARTNERSCHAFT) {
				final Gesuch gesuch = initMinimalGesuchWithZivilstand();
				updateFromZivilstandToZivilstand(gesuch, AUFGELOESTE_PARTNERSCHAFT, zivilstand);
				MatcherAssert.assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), Matchers.notNullValue());
			}
		}
	}

	private void updateFromZivilstandToZivilstand(Gesuch gesuch, Zivilstand from, Zivilstand to) {
		gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().setZivilstand(from);
		final GesuchUpdateDto gesuchUpdateDto = initGesuchUpdateDtoWithZivilstand(to);
		when(gesuchRepository.requireById(any())).thenReturn(gesuch);

		gesuchService.updateGesuch(any(), gesuchUpdateDto);
	}

	private Gesuch initMinimalGesuchWithZivilstand() {
		final Gesuch gesuch = new Gesuch();
		final Fall fall = new Fall();
		final Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
		gesuchsperiode.setGueltigkeit(new DateRange(LocalDate.of(2023, 8, 1), LocalDate.of(2024, 7, 31)));
		final GesuchFormular gesuchFormularToWorkWith = new GesuchFormular();
		final Partner partner = new Partner();
		final PersonInAusbildung personInAusbildung = initMinimalPersonInAusbildung();
		gesuchFormularToWorkWith.setPartner(partner);
		gesuchFormularToWorkWith.setPersonInAusbildung(personInAusbildung);
		gesuch.setGesuchFormularToWorkWith(gesuchFormularToWorkWith);
		gesuch.setFall(fall);
		gesuch.setGesuchsperiode(gesuchsperiode);

		return gesuch;
	}

	@NotNull
	private static PersonInAusbildung initMinimalPersonInAusbildung() {
		final PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		var adresse = initMinimalAdresse();
		personInAusbildung.setZivilstand(LEDIG);
		personInAusbildung.setGeburtsdatum(LocalDate.of(1965, 1, 1));
		personInAusbildung.setAdresse(adresse);
		personInAusbildung.setAnrede(HERR);
		personInAusbildung.setEmail("test@test.ch");
		personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 10, 10));
		personInAusbildung.setNachname("Tester");
		personInAusbildung.setVorname("Prosper");
		personInAusbildung.setNationalitaet(CH);
		personInAusbildung.setTelefonnummer("078 888 88 88");
		personInAusbildung.setDigitaleKommunikation(true);
		personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
		personInAusbildung.setKorrespondenzSprache(DEUTSCH);
		personInAusbildung.setSozialhilfebeitraege(false);
		personInAusbildung.setSozialversicherungsnummer(AHV_NUMMER_VALID);
		personInAusbildung.setQuellenbesteuert(false);
		personInAusbildung.setWohnsitz(FAMILIE);
		personInAusbildung.setHeimatort("Bern");
		return personInAusbildung;
	}

	private static Adresse initMinimalAdresse() {
		var adresse = new Adresse();
		adresse.setLand(CH);
		adresse.setPlz("3000");
		adresse.setOrt("Bern");
		adresse.setStrasse("Strasse");
		return adresse;
	}

	private GesuchUpdateDto initGesuchUpdateDtoWithZivilstand(Zivilstand zivilstand) {
		final GesuchUpdateDto gesuchDto = new GesuchUpdateDto();
		final GesuchFormularUpdateDto gesuchFormularDto = new GesuchFormularUpdateDto();
		final PersonInAusbildungUpdateDto personInAusbildungDto = new PersonInAusbildungUpdateDto();
		final AdresseDto adresseDto = initMinimalAdresseDto();
		personInAusbildungDto.setAnrede(HERR);
		personInAusbildungDto.setEmail("test@test.ch");
		personInAusbildungDto.setGeburtsdatum(LocalDate.of(2000, 10, 10));
		personInAusbildungDto.setNachname("Tester");
		personInAusbildungDto.setVorname("Prosper");
		personInAusbildungDto.setNationalitaet(CH);
		personInAusbildungDto.setTelefonnummer("078 888 88 88");
		personInAusbildungDto.setDigitaleKommunikation(true);
		personInAusbildungDto.setIdentischerZivilrechtlicherWohnsitz(true);
		personInAusbildungDto.setKorrespondenzSprache(DEUTSCH);
		personInAusbildungDto.setSozialhilfebeitraege(false);
		personInAusbildungDto.setSozialversicherungsnummer(AHV_NUMMER_VALID);
		personInAusbildungDto.setQuellenbesteuert(false);
		personInAusbildungDto.setWohnsitz(FAMILIE);
		personInAusbildungDto.setHeimatort("Bern");
		personInAusbildungDto.setAdresse(adresseDto);
		personInAusbildungDto.setZivilstand(zivilstand);
		gesuchFormularDto.setPersonInAusbildung(personInAusbildungDto);
		gesuchDto.setGesuchFormularToWorkWith(gesuchFormularDto);

		return gesuchDto;
	}

	@NotNull
	private static AdresseDto initMinimalAdresseDto() {
		final AdresseDto adresseDto = new AdresseDto();
		adresseDto.setLand(CH);
		adresseDto.setPlz("3000");
		adresseDto.setOrt("Bern");
		adresseDto.setStrasse("Strasse");
		return adresseDto;
	}

}
