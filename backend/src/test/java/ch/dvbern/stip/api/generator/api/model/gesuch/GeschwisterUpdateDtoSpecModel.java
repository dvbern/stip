package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungssituationDtoSpec;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;

public final class GeschwisterUpdateDtoSpecModel {
    public static List<GeschwisterUpdateDtoSpec> geschwisterUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(GeschwisterUpdateDtoSpec::new, (model) -> {
            model.setVorname("Test");
            model.setNachname("Geschwister");
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2002, 1, 1)));
            model.setWohnsitz(WohnsitzDtoSpec.MUTTER_VATER);
            model.setAusbildungssituation(AusbildungssituationDtoSpec.IN_AUSBILDUNG);
            model.setWohnsitzAnteilMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setWohnsitzAnteilVater(BigDecimal.valueOf(100).subtract(model.getWohnsitzAnteilMutter()));
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecGeschwisters() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setGeschwisters(geschwisterUpdateDtoSpecs())
        );
    }
}
