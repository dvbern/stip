package ch.dvbern.stip.test.generator.api.model.gesuch;

import ch.dvbern.stip.generated.test.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.test.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.UUID;

import static org.instancio.Select.field;

public final class AusbildungUpdateDtoSpecModel {

	public static final Model<AusbildungUpdateDtoSpec> ausbildungUpdateDtoSpecModel =
			Instancio.of(AusbildungUpdateDtoSpec.class)
					.set(field(AusbildungUpdateDtoSpec::getAusbildungBegin), "01.2022")
					.set(field(AusbildungUpdateDtoSpec::getAusbildungEnd), "02.2022")
					.set(field(AusbildungUpdateDtoSpec::getAusbildungNichtGefunden), false)
					.ignore(field(AusbildungUpdateDtoSpec::getAlternativeAusbildungsgang))
					.ignore(field(AusbildungUpdateDtoSpec::getAlternativeAusbildungsstaette))
					.set(field(AusbildungUpdateDtoSpec::getAusbildungsgangId), UUID.fromString("3a8c2023-f29e-4466-a2d7-411a7d032f42"))
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecAusbildungModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getAusbildung),
							Instancio.create(ausbildungUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
					.ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
					.ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
					.ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
					.ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
					.ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
					.ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
					.toModel();
}
