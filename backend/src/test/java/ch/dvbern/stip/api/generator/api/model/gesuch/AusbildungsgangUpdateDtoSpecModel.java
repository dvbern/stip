package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.BildungsartDtoSpec;

public class AusbildungsgangUpdateDtoSpecModel {
    public static final AusbildungsgangUpdateDtoSpec ausbildungsgangUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(AusbildungsgangUpdateDtoSpec::new, (model, faker) -> {
            model.setBezeichnungDe("Bachelor Informatik");
            model.setBezeichnungFr("Bachelor Informatik");
            model.setAusbildungsrichtung(BildungsartDtoSpec.UNIVERSITAETEN_ETH);
            model.setAusbildungsstaetteId(AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpec.getId());
            model.setAusbildungsort("Bern");
        });
}
