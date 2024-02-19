package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public final class AdresseSpecModel {
    public static final Model<AdresseDtoSpec> adresseSpecModel =
        Instancio.of(AdresseDtoSpec.class)
            .set(field(AdresseDtoSpec::getLand), LandDtoSpec.CH)
            .ignore(field(AdresseDtoSpec::getId))
            .toModel();
}
