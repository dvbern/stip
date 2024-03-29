package ch.dvbern.stip.api.generator.api.model.benutzer;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDtoSpec;

import java.util.UUID;

public class BenutzerUpdateDtoSpecModel {
    public static final BenutzerUpdateDtoSpec benutzerUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(BenutzerUpdateDtoSpec::new, (model, faker) -> {
            model.setVorname("Fritz");
            model.setNachname("Tester");
            model.setSozialversicherungsnummer("756.1234.5678.97");
            model.setBenutzereinstellungen(new BenutzereinstellungenUpdateDtoSpec());
            model.getBenutzereinstellungen().setId(UUID.fromString(faker.internet().uuid()));
            model.getBenutzereinstellungen().setDigitaleKommunikation(faker.bool().bool());
        });
}
