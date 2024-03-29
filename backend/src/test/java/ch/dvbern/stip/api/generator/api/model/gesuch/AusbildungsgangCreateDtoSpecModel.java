package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDtoSpec;
import ch.dvbern.stip.generated.dto.BildungsartDtoSpec;

import java.util.UUID;

public class AusbildungsgangCreateDtoSpecModel {
    public static final AusbildungsgangCreateDtoSpec ausbildungsgangCreateDtoSpec =
        TestUtil.createUpdateDtoSpec(AusbildungsgangCreateDtoSpec::new, (model, faker) -> {
            model.setAusbildungsort("Bern");
            model.setBezeichnungDe("Bachelor Informatik");
            model.setBezeichnungFr("Bachelor Informatik");
            model.setAusbildungsrichtung(BildungsartDtoSpec.UNIVERSITAETEN_ETH);
            model.setAusbildungsstaetteId(UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"));
        });
}
