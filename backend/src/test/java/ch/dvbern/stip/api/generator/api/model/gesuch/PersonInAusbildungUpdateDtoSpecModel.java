package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.math.BigDecimal;
import java.time.LocalDate;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AnredeDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SpracheDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;
import ch.dvbern.stip.generated.dto.ZivilstandDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

public final class PersonInAusbildungUpdateDtoSpecModel {
    public static PersonInAusbildungUpdateDtoSpec personInAusbildungUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(PersonInAusbildungUpdateDtoSpec::new, (model, faker) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setIdentischerZivilrechtlicherWohnsitz(false);
            model.setNationalitaet(LandDtoSpec.CH);
            model.setWohnsitz(WohnsitzDtoSpec.MUTTER_VATER);
            model.setNiederlassungsstatus(null);
            model.setEmail("valid@mailbucket.dvbern.ch");
            model.setSozialversicherungsnummer(AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG);
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setGeburtsdatum(LocalDate.now().with(firstDayOfYear()).minusYears(17));
            model.setWohnsitzAnteilMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setWohnsitzAnteilVater(BigDecimal.valueOf(100).subtract(model.getWohnsitzAnteilMutter()));
            model.setAnrede(TestUtil.getRandomElementFromArray(AnredeDtoSpec.values()));
            model.setTelefonnummer(faker.phoneNumber().cellPhone());
            model.setEinreisedatum(TestUtil.getRandomLocalDateBetween(
                LocalDate.of(1980, 1, 1),
                LocalDate.of(2000, 1, 1))
            );
            model.setHeimatort(faker.address().cityName());
            model.setZivilstand(ZivilstandDtoSpec.LEDIG);
            model.setSozialhilfebeitraege(true);
            model.setVormundschaft(false);
            model.setIdentischerZivilrechtlicherWohnsitzOrt("Bern");
            model.setIdentischerZivilrechtlicherWohnsitzPLZ("3011");
            model.setKorrespondenzSprache(TestUtil.getRandomElementFromArray(SpracheDtoSpec.values()));
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecPersonInAusbildung() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model, faker) -> model.setPersonInAusbildung(personInAusbildungUpdateDtoSpec())
        );
    }
}
