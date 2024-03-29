package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDtoSpec;

public class AusbildungsstaetteCreateDtoSpecModel {
    public static final AusbildungsstaetteCreateDtoSpec ausbildungsstaetteCreateDtoSpec =
        TestUtil.createUpdateDtoSpec(AusbildungsstaetteCreateDtoSpec::new, (model, faker) -> {
            model.setNameDe("Uni Bern");
            model.setNameFr("Uni Bern");
        });
}
