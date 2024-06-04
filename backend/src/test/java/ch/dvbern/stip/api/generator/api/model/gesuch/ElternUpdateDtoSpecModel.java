package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

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
            model.setSozialhilfebeitraegeAusbezahlt(true);
            model.setAusweisbFluechtling(false);
            model.setErgaenzungsleistungAusbezahlt(true);
            model.setWohnkosten(100);
        }, amount);
    }

    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs() {
        return elternUpdateDtoSpecs(1);
    }

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecElterns =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setElterns(elternUpdateDtoSpecs()));
}
