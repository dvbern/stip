package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDtoSpec;

public class AusbildungsstaetteCreateDtoSpecModel {
    public static AusbildungsstaetteCreateDtoSpec ausbildungsstaetteCreateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungsstaetteCreateDtoSpec::new, (model) -> {
            model.setNameDe("Uni Bern");
            model.setNameFr("Uni Bern");
        });
    }
}
