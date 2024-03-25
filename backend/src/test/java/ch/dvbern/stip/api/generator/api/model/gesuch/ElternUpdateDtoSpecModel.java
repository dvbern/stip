package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

import java.time.LocalDate;
import java.util.List;

public class ElternUpdateDtoSpecModel {
    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs(final int amount) {
        return TestUtil.createUpdateDtoSpecs(ElternUpdateDtoSpec::new, (model, faker) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec);
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
            model.setElternTyp(ElternTypDtoSpec.VATER);
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1920, 1, 1), LocalDate.of(2002, 1, 1)));
            model.setIdentischerZivilrechtlicherWohnsitz(false);
            model.setIdentischerZivilrechtlicherWohnsitzOrt("Bern");
            model.setIdentischerZivilrechtlicherWohnsitzPLZ("3000");
            model.setTelefonnummer(faker.phoneNumber().cellPhone());
            model.setSozialhilfebeitraegeAusbezahlt(faker.bool().bool());
            model.setAusweisbFluechtling(faker.bool().bool());
            model.setErgaenzungsleistungAusbezahlt(faker.bool().bool());
        }, amount);
    }

<<<<<<< HEAD
    public static final Model<List<ElternUpdateDtoSpec>> elternUpdateDtoSpecModel =
        Instancio.ofList(ElternUpdateDtoSpec.class).size(1)
            .ignore(field(ElternUpdateDtoSpec::getId))
            .set(field(ElternUpdateDtoSpec::getAdresse), Instancio.create(AdresseSpecModel.adresseSpecModel))
            .set(
                field(ElternUpdateDtoSpec::getSozialversicherungsnummer),
                TestConstants.AHV_NUMMER_VALID_VATTER
            )
            .set(field(ElternUpdateDtoSpec::getElternTyp), ElternTypDtoSpec.VATER)
            .generate(
                field(ElternUpdateDtoSpec::getGeburtsdatum),
                gen -> gen.temporal().localDate().range(LocalDate.of(1920, 1, 1), LocalDate.of(1980, 1,
                    1
                ))
            )
            .assign(Assign.given(
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz),
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitzOrt))
                .set(When.is(false), "Bern")
                .set(When.is(true), null))
            .assign(Assign.given(
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz),
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitzPLZ))
                .set(When.is(false), "3000")
                .set(When.is(true), null))
            .toModel();
=======
    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs() {
        return elternUpdateDtoSpecs(1);
    }
>>>>>>> feature/KSTIP-919

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecElterns =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setElterns(elternUpdateDtoSpecs()));
}
