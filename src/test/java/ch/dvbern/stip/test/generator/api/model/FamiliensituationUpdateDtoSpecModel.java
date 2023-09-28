package ch.dvbern.stip.test.generator.api.model;

import java.math.BigDecimal;

import ch.dvbern.oss.stip.contract.test.dto.ElternschaftsteilungDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.FamiliensituationUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public final class FamiliensituationUpdateDtoSpecModel {

	public static final Model<FamiliensituationUpdateDtoSpec> familiensituationUpdateDtoSpecModel =
			Instancio.of(FamiliensituationUpdateDtoSpec.class)
					.set(field(FamiliensituationUpdateDtoSpec::getObhut), ElternschaftsteilungDtoSpec.GEMEINSAM)
					.set(field(FamiliensituationUpdateDtoSpec::getGerichtlicheAlimentenregelung), true)
					.generate(
							field(FamiliensituationUpdateDtoSpec::getObhutMutter),
							gen -> gen.ints().range(0, 100).as(BigDecimal::valueOf))
					.assign(Assign.valueOf(FamiliensituationUpdateDtoSpec::getObhutMutter)
							.to(FamiliensituationUpdateDtoSpec::getObhutVater)
							.as((BigDecimal i) -> BigDecimal.valueOf(100).subtract(i)))
					.toModel();

	public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecFamiliensituationModel =
			Instancio.of(
							GesuchFormularUpdateDtoSpec.class)
					.set(
							field(GesuchFormularUpdateDtoSpec::getFamiliensituation),
							Instancio.create(familiensituationUpdateDtoSpecModel))
					.ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
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
