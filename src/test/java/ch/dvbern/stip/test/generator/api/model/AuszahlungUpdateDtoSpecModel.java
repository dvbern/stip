package ch.dvbern.stip.test.generator.api.model;

import ch.dvbern.oss.stip.contract.test.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public final class AuszahlungUpdateDtoSpecModel {

	public static final Model<AuszahlungUpdateDtoSpec> auszahlungUpdateDtoSpecModel =
			Instancio.of(AuszahlungUpdateDtoSpec.class)
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecAuszahlungModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getAuszahlung),
							Instancio.create(auszahlungUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
					.ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
					.ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
					.toModel();
}
