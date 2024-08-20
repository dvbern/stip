package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDtoSpec;

public class AusbildungsgangCreateDtoSpecModel {
    public static AusbildungsgangCreateDtoSpec ausbildungsgangCreateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungsgangCreateDtoSpec::new, (model, faker) -> {
            model.setBezeichnungDe("Bachelor Informatik");
            model.setBezeichnungFr("Bachelor Informatik");
            model.setBildungskategorieId(TestConstants.TEST_BILDUNGSKATEGORIE_ID); //TODO: use test constance and update entity to hold id
            model.setAusbildungsstaetteId(TestConstants.TEST_AUSBILDUNGSSTAETTE_ID);
        });
    }
}
