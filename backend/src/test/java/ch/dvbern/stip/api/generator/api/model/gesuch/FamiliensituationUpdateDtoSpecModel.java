package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public final class FamiliensituationUpdateDtoSpecModel {
    public static final FamiliensituationUpdateDtoSpec familiensituationUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(FamiliensituationUpdateDtoSpec::new, (model, faker) -> {
            model.setElternVerheiratetZusammen(true);
        });

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFamiliensituation =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setFamiliensituation(familiensituationUpdateDtoSpec));
}
