package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.PartnerNullRequiredWhenAlleinstehendConstraintValidator;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.GESCHIEDEN_GERICHTLICH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.KONKUBINAT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERHEIRATET;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERWITWET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PartnerNullRequiredWhenAllinstehendConstraintValidatorTest {
	PartnerNullRequiredWhenAlleinstehendConstraintValidator validator =
			new PartnerNullRequiredWhenAlleinstehendConstraintValidator();

	@Test
	void personInAusbildungLedigGeschiedenAufgeloestOrVerwittwetAndPartnerNotNullShouldNotBeValid() {
		for (var zivilstand : new Zivilstand[] {LEDIG, GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET}) {
			GesuchFormular gesuchFormular = new GesuchFormular();
			gesuchFormular.setPersonInAusbildung(new PersonInAusbildung());
			gesuchFormular.setPartner(new Partner());
			gesuchFormular.getPersonInAusbildung().setZivilstand(zivilstand);

			assertThat(validator.isValid(gesuchFormular, null), is(false));
		}
	}

	@Test
	void personInAusbildungLedigGeschiedenAufgeloestOrVerwittwetAndPartnerNullShouldBeValid() {
		for (var zivilstand : new Zivilstand[] {LEDIG, GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET}) {
			GesuchFormular gesuchFormular = new GesuchFormular();
			gesuchFormular.setPersonInAusbildung(new PersonInAusbildung());
			gesuchFormular.setPartner(null);
			gesuchFormular.getPersonInAusbildung().setZivilstand(zivilstand);

			assertThat(validator.isValid(gesuchFormular, null), is(true));
		}
	}

	@Test
	void personInAusbildungVerheiratetKonkubinatPartnerschaftAndPartnerNullShouldNotBeValid() {
		for (var zivilstand : new Zivilstand[] {VERHEIRATET, KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT}) {
			GesuchFormular gesuchFormular = new GesuchFormular();
			gesuchFormular.setPersonInAusbildung(new PersonInAusbildung());
			gesuchFormular.setPartner(null);
			gesuchFormular.getPersonInAusbildung().setZivilstand(zivilstand);

			assertThat(validator.isValid(gesuchFormular, null), is(false));
		}
	}

	@Test
	void personInAusbildungVerheiratetKonkubinatPartnerschaftAndPartnerNotNullShouldBeValid() {
		for (var zivilstand : new Zivilstand[] {VERHEIRATET, KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT}) {
			GesuchFormular gesuchFormular = new GesuchFormular();
			gesuchFormular.setPersonInAusbildung(new PersonInAusbildung());
			gesuchFormular.setPartner(new Partner());
			gesuchFormular.getPersonInAusbildung().setZivilstand(zivilstand);

			assertThat(validator.isValid(gesuchFormular, null), is(true));
		}
	}
}
