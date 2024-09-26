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
        return TestUtil.createUpdateDtoSpecs(ElternUpdateDtoSpec::new, (model) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setVorname("Test");
            model.setNachname("Elternteil");
            model.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
            model.setElternTyp(ElternTypDtoSpec.VATER);
            model.setWohnkosten(1);
            model.setSozialhilfebeitraege(1);
            model.setErgaenzungsleistungen(1);
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1920, 1, 1), LocalDate.of(2002, 1, 1)));
            model.setIdentischerZivilrechtlicherWohnsitz(false);
            model.setIdentischerZivilrechtlicherWohnsitzOrt("Bern");
            model.setIdentischerZivilrechtlicherWohnsitzPLZ("3011");
            model.setTelefonnummer("+41 79 111 11 11");
            model.setAusweisbFluechtling(false);
        }, amount);
    }

    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs() {
        return elternUpdateDtoSpecs(1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecElterns() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setElterns(elternUpdateDtoSpecs())
        );
    }
}
