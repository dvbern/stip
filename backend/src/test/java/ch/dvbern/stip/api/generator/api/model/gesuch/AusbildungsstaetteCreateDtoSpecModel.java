package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class AusbildungsstaetteCreateDtoSpecModel {
	public static final Model<AusbildungsstaetteCreateDtoSpec> ausbildungsstaetteCreateDtoSpecModel =
			Instancio.of(AusbildungsstaetteCreateDtoSpec.class)
					.set(field(AusbildungsstaetteCreateDtoSpec::getNameDe), "Uni Bern")
					.set(field(AusbildungsstaetteCreateDtoSpec::getNameFr), "Uni Bern")
					.toModel();
}
