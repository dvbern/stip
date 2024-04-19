package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;

public final class AdresseSpecModel {
    public static final AdresseDtoSpec adresseDtoSpec =
        TestUtil.createUpdateDtoSpec(AdresseDtoSpec::new, (model, faker) -> {
            model.setLand(LandDtoSpec.CH);
            model.setCoAdresse(faker.address().secondaryAddress());
            model.setStrasse(faker.address().streetName());
            model.setHausnummer(faker.address().streetAddressNumber());
            model.setPlz("3000");
            model.setOrt(faker.address().cityName());
        });
}
