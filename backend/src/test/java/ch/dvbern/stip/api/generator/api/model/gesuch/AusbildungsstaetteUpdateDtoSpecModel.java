package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class AusbildungsstaetteUpdateDtoSpecModel {

    public static final Model<AusbildungsstaetteUpdateDtoSpec> ausbildungsstaetteUpdateDtoSpecModel =
        Instancio.of(AusbildungsstaetteUpdateDtoSpec.class)
            .set(field(AusbildungsstaetteUpdateDtoSpec::getNameDe), "Uni Bern")
            .set(field(AusbildungsstaetteUpdateDtoSpec::getNameFr), "Uni Bern")
            .ignore(field(AusbildungsstaetteUpdateDtoSpec::getId))
            .toModel();
}
