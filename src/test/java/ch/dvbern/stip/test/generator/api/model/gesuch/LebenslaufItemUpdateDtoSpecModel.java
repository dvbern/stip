package ch.dvbern.stip.test.generator.api.model.gesuch;

import java.util.List;

import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.LebenslaufItemUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public final class LebenslaufItemUpdateDtoSpecModel {

	public static final Model<List<LebenslaufItemUpdateDtoSpec>> lebenslaufItemUpdateDtoSpecModel =
			Instancio.ofList(LebenslaufItemUpdateDtoSpec.class).size(1)
					.ignore(field(LebenslaufItemUpdateDtoSpec::getId))
					.ignore(field(LebenslaufItemUpdateDtoSpec::getTaetigskeitsart))
					.set(field(LebenslaufItemUpdateDtoSpec::getVon), "01.2022")
					.set(field(LebenslaufItemUpdateDtoSpec::getBis), "02.2022")
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecLebenslaufModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getLebenslaufItems),
							Instancio.create(lebenslaufItemUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
					.ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
					.toModel();
}
