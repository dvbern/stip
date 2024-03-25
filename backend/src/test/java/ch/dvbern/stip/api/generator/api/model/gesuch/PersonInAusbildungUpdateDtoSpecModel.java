package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG;

public final class PersonInAusbildungUpdateDtoSpecModel {
    public static final PersonInAusbildungUpdateDtoSpec personInAusbildungUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(PersonInAusbildungUpdateDtoSpec::new, (model, faker) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec);
            model.setIdentischerZivilrechtlicherWohnsitz(false);
            model.setNationalitaet(LandDtoSpec.CH);
            model.setWohnsitz(WohnsitzDtoSpec.MUTTER_VATER);
            model.setNiederlassungsstatus(null);
            model.setEmail("valid@mailbucket.dvbern.ch");
            model.setSozialversicherungsnummer(AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG);
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1920, 1, 1), LocalDate.of(2002, 1, 1)));
            model.setWohnsitzAnteilMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setWohnsitzAnteilVater(BigDecimal.valueOf(100).subtract(model.getWohnsitzAnteilMutter()));
            model.setAnrede(TestUtil.getRandomElementFromArray(AnredeDtoSpec.values()));
            model.setTelefonnummer(faker.phoneNumber().cellPhone());
            model.setEinreisedatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1980, 1, 1), LocalDate.of(2000, 1, 1)));
            model.setHeimatort(faker.address().cityName());
            model.setZivilstand(TestUtil.getRandomElementFromArray(ZivilstandDtoSpec.values()));
            model.setSozialhilfebeitraege(faker.bool().bool());
            model.setVormundschaft(faker.bool().bool());
            model.setIdentischerZivilrechtlicherWohnsitzOrt("Bern");
            model.setIdentischerZivilrechtlicherWohnsitzPLZ("3000");
            model.setKorrespondenzSprache(TestUtil.getRandomElementFromArray(SpracheDtoSpec.values()));
        });

<<<<<<< HEAD
    public static final Model<PersonInAusbildungUpdateDtoSpec> personInAusbildungUpdateDtoSpecModel =
        Instancio.of(PersonInAusbildungUpdateDtoSpec.class)
            .set(
                field(PersonInAusbildungUpdateDtoSpec::getAdresse),
                Instancio.create(AdresseSpecModel.adresseSpecModel))
            .set(field(PersonInAusbildungUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz), false)
            .set(field(PersonInAusbildungUpdateDtoSpec::getNationalitaet), LandDtoSpec.CH)
            .set(field(PersonInAusbildungUpdateDtoSpec::getWohnsitz), WohnsitzDtoSpec.MUTTER_VATER)
            .set(field(PersonInAusbildungUpdateDtoSpec::getNiederlassungsstatus), null)
            .set(field(PersonInAusbildungUpdateDtoSpec::getEmail), "valid@mailbucket.dvbern.ch")
            .set(
                field(PersonInAusbildungUpdateDtoSpec::getSozialversicherungsnummer),
                AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getVorname),
                gen -> gen.oneOf("Sarah", "Elijah", "Nu'ab", "André", "Özgür")
            )
            .set(
                field(PersonInAusbildungUpdateDtoSpec::getGeburtsdatum),
                LocalDate.now().minusYears(16)
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getNachname),
                gen -> gen.oneOf("Müller", "Sánchez", "Sato", "Singh", "Li", "García", "Nguyen")
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilMutter),
                gen -> gen.ints().range(0, 100).as(BigDecimal::valueOf)
            )
            .assign(Assign.valueOf(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilMutter)
                .to(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilVater)
                .as((BigDecimal i) -> BigDecimal.valueOf(100).subtract(i)))
            .ignore(field(PersonInAusbildungUpdateDtoSpec::getVermoegenVorjahr))
            .toModel();
    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecPersonInAusbildungModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung),
                Instancio.create(personInAusbildungUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
=======
    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecPersonInAusbildung =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setPersonInAusbildung(personInAusbildungUpdateDtoSpec));
>>>>>>> feature/KSTIP-919
}
