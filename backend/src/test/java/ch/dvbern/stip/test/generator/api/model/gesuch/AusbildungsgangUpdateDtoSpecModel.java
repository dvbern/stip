package ch.dvbern.stip.test.generator.api.model.gesuch;

import ch.dvbern.oss.stip.contract.test.dto.AusbildungsgangUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.BildungsartDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.generator.api.model.gesuch.AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpecModel;
import static org.instancio.Select.field;

public class AusbildungsgangUpdateDtoSpecModel {
	public static final Model<AusbildungsgangUpdateDtoSpec> ausbildungsgangUpdateDtoSpecModel =
			Instancio.of(AusbildungsgangUpdateDtoSpec.class)
					.set(field(AusbildungsgangUpdateDtoSpec::getBezeichnungDe), "Bachelor Informatik")
					.set(field(AusbildungsgangUpdateDtoSpec::getBezeichnungFr), "Bachelor Informatik")
					.set(field(AusbildungsgangUpdateDtoSpec::getAusbildungsrichtung), BildungsartDtoSpec.UNIVERSITAETEN_ETH)
					.set(field(AusbildungsgangUpdateDtoSpec::getAusbildungsstaette), Instancio.of(ausbildungsstaetteUpdateDtoSpecModel).create())
					.toModel();
}
