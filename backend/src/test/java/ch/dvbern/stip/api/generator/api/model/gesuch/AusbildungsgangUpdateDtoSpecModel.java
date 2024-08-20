package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDtoSpec;

public class AusbildungsgangUpdateDtoSpecModel {
    public static AusbildungsgangUpdateDtoSpec ausbildungsgangUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungsgangUpdateDtoSpec::new, (model, faker) -> {
            model.setBezeichnungDe("Bachelor Informatik");
            model.setBezeichnungFr("Bachelor Informatik");
            model.setBildungskategorieId(TestConstants.TEST_BILDUNGSKATEGORIE_ID);
            model.setAusbildungsstaetteId(AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpec().getId());
        });
    }
}
