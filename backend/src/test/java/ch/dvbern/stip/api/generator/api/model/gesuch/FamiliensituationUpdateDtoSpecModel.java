package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public final class FamiliensituationUpdateDtoSpecModel {
    public static FamiliensituationUpdateDtoSpec familiensituationUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(FamiliensituationUpdateDtoSpec::new, (model) -> {
            model.setElternVerheiratetZusammen(true);
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFamiliensituation() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setFamiliensituation(familiensituationUpdateDtoSpec())
        );
    }
}
