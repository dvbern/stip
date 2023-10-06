package ch.dvbern.stip.test.generator.api.model;

import ch.dvbern.oss.stip.contract.test.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class EinnahmenKostenUpdateDtoSpecModel {

	public static final Model<EinnahmenKostenUpdateDtoSpec> einnahmenKostenUpdateDtoSpecModel =
			Instancio.of(EinnahmenKostenUpdateDtoSpec.class)
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecEinnahmenKostenModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten),
							Instancio.create(einnahmenKostenUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
					.ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
					.toModel();
}
