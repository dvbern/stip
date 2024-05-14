package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.UUID;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDtoSpec;
import ch.dvbern.stip.generated.dto.BildungsartDtoSpec;

public class AusbildungsgangCreateDtoSpecModel {
    public static final AusbildungsgangCreateDtoSpec ausbildungsgangCreateDtoSpec =
        TestUtil.createUpdateDtoSpec(AusbildungsgangCreateDtoSpec::new, (model, faker) -> {
            model.setBezeichnungDe("Bachelor Informatik");
            model.setBezeichnungFr("Bachelor Informatik");
            model.setBildungsart(new BildungsartDtoSpec()); //TODO: use test constance and update entity to hold id
            model.setAusbildungsstaetteId(UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"));
        });
}
