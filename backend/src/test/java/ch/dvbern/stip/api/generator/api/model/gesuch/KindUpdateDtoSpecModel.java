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
        return TestUtil.createUpdateDtoSpecs(KindUpdateDtoSpec::new, (model, faker) -> {
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2002, 1, 1)));
            model.setAusbildungssituation(TestUtil.getRandomElementFromArray(AusbildungssituationDtoSpec.values()));
            model.setWohnsitz(WohnsitzDtoSpec.MUTTER_VATER);
            model.setWohnsitzAnteilMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setWohnsitzAnteilVater(BigDecimal.valueOf(100).subtract(model.getWohnsitzAnteilMutter()));
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecKinder() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model, faker) -> model.setKinds(kindUpdateDtoSpecs())
        );
    }
}
