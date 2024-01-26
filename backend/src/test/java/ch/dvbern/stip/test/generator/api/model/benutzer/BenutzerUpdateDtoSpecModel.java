package ch.dvbern.stip.test.generator.api.model.benutzer;

import ch.dvbern.stip.generated.test.dto.BenutzerUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class BenutzerUpdateDtoSpecModel {

	public static final Model<BenutzerUpdateDtoSpec> benutzerUpdateDtoSpecModel =
			Instancio.of(BenutzerUpdateDtoSpec.class)
					.set(field(BenutzerUpdateDtoSpec::getVorname), "Fritz")
					.set(field(BenutzerUpdateDtoSpec::getNachname), "Tester")
					.set(field(BenutzerUpdateDtoSpec::getSozialversicherungsnummer), "756.1234.5678.97")
					.toModel();
}
