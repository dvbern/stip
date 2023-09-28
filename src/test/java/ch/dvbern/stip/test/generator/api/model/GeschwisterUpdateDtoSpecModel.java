package ch.dvbern.stip.test.generator.api.model;

import ch.dvbern.oss.stip.contract.test.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public final class GeschwisterUpdateDtoSpecModel {

	public static final Model<GeschwisterUpdateDtoSpec> geschwisterUpdateDtoSpecModel =
			Instancio.of(GeschwisterUpdateDtoSpec.class)
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecAusbildungModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getGeschwisters),
							Instancio.create(geschwisterUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
					.ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
					.toModel();
}
