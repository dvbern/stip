package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungssituationDtoSpec;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class GeschwisterUpdateDtoSpecModel {
    public static final List<GeschwisterUpdateDtoSpec> geschwisterUpdateDtoSpecs =
        TestUtil.createUpdateDtoSpecs(GeschwisterUpdateDtoSpec::new, (model, faker) -> {
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1990, 1, 1), LocalDate.of(2002, 1, 1)));
            model.setWohnsitz(WohnsitzDtoSpec.MUTTER_VATER);
            model.setAusbildungssituation(AusbildungssituationDtoSpec.IN_AUSBILDUNG);
            model.setWohnsitzAnteilMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setWohnsitzAnteilVater(BigDecimal.valueOf(100).subtract(model.getWohnsitzAnteilMutter()));
        }, 1);

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecGeschwisters =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setGeschwisters(geschwisterUpdateDtoSpecs));
}
