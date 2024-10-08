package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungssituationDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KindUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;

public class KindUpdateDtoSpecModel {
    public static List<KindUpdateDtoSpec> kindUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(KindUpdateDtoSpec::new, (model) -> {
            model.setVorname("Test");
            model.setNachname("Kind");
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2002, 1, 1)));
            model.setAusbildungssituation(TestUtil.getRandomElementFromArray(AusbildungssituationDtoSpec.values()));
            model.setWohnsitzAnteilPia(TestUtil.getRandomInt(0,100));
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecKinder() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setKinds(kindUpdateDtoSpecs())
        );
    }
}
