package ch.dvbern.stip.test.generator.api.model;

import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.PartnerDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.PartnerUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.util.TestConstants.AHV_NUMMER_VALID_PARTNER;
import static org.instancio.Select.field;

public final class PartnerUpdateDtoSpecModel {

	public static final Model<PartnerUpdateDtoSpec> partnerUpdateDtoSpecModel =
			Instancio.of(PartnerUpdateDtoSpec.class)
					.set(field(PartnerDtoSpec::getSozialversicherungsnummer), AHV_NUMMER_VALID_PARTNER)
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecAusbildungModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getPartner),
							Instancio.create(partnerUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
					.ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
					.ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
					.toModel();
}
