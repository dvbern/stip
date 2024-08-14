package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDtoSpec;

public class AusbildungsstaetteUpdateDtoSpecModel {
    public static AusbildungsstaetteUpdateDtoSpec ausbildungsstaetteUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungsstaetteUpdateDtoSpec::new, (model, faker) -> {
            model.setNameDe("Uni Bern");
            model.setNameFr("Uni Bern");
        });
    }
}
