package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;

public final class AdresseSpecModel {
    public static AdresseDtoSpec adresseDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AdresseDtoSpec::new, (model) -> {
            model.setLand(LandDtoSpec.CH);
            model.setCoAdresse("");
            model.setStrasse("Nussbaumstrasse");
            model.setHausnummer("22");
            model.setPlz("3006");
            model.setOrt("Bern");
        });
    }
}
