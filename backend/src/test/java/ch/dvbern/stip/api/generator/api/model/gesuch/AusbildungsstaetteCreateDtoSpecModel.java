package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class AusbildungsstaetteCreateDtoSpecModel {
	public static final Model<AusbildungsstaetteCreateDtoSpec> ausbildungsstaetteCreateDtoSpecModel =
			Instancio.of(AusbildungsstaetteCreateDtoSpec.class)
					.set(field(AusbildungsstaetteCreateDtoSpec::getNameDe), "Uni Bern")
					.set(field(AusbildungsstaetteCreateDtoSpec::getNameFr), "Uni Bern")
					.set(field(AusbildungsstaetteCreateDtoSpec::getId), UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"))
					.toModel();
}
