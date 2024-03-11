package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDtoSpec;
import ch.dvbern.stip.generated.dto.BildungsartDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class AusbildungsgangCreateDtoSpecModel {
	public static final Model<AusbildungsgangCreateDtoSpec> ausbildungsgangCreateDtoSpecModel =
			Instancio.of(AusbildungsgangCreateDtoSpec.class)
					.set(field(AusbildungsgangCreateDtoSpec::getBezeichnungDe), "Bachelor Informatik")
					.set(field(AusbildungsgangCreateDtoSpec::getBezeichnungFr), "Bachelor Informatik")
					.set(field(AusbildungsgangCreateDtoSpec::getAusbildungsrichtung), BildungsartDtoSpec.UNIVERSITAETEN_ETH)
					.set(field(AusbildungsgangCreateDtoSpec::getAusbildungsstaetteId), UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"))
					.toModel();
}
