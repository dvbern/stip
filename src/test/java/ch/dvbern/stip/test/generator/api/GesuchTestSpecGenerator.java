package ch.dvbern.stip.test.generator.api;

import ch.dvbern.oss.stip.contract.test.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ElternUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.KindUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.LebenslaufItemUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.generator.api.model.AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.AusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.AuszahlungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAuszahlungModel;
import static ch.dvbern.stip.test.generator.api.model.FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituationModel;
import static ch.dvbern.stip.test.generator.api.model.PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpecModel;
import static org.instancio.Select.field;

public class GesuchTestSpecGenerator {
	private static final Model<LebenslaufItemUpdateDtoSpec> lebenslaufItemUpdateDtoSpecModel =
			Instancio.of(LebenslaufItemUpdateDtoSpec.class)
					.toModel();
	private static final Model<GeschwisterUpdateDtoSpec> geschwisterUpdateDtoSpecModel =
			Instancio.of(GeschwisterUpdateDtoSpec.class)
					.toModel();

	private static final Model<ElternUpdateDtoSpec> elternUpdateDtoSpecModel =
			Instancio.of(ElternUpdateDtoSpec.class)
					.toModel();

	private static final Model<KindUpdateDtoSpec> kindUpdateDtoSpecModel =
			Instancio.of(KindUpdateDtoSpec.class)
					.toModel();

	private static final Model<EinnahmenKostenUpdateDtoSpec> einnahmenKostenUpdateDtoSpecModel =
			Instancio.of(EinnahmenKostenUpdateDtoSpec.class)
					.toModel();

	private static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecModelFull = Instancio.of(
					GesuchFormularUpdateDtoSpec.class)
			.set(
					field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung),
					Instancio.create(personInAusbildungUpdateDtoSpecModel))
			.set(
					field(GesuchFormularUpdateDtoSpec::getFamiliensituation),
					Instancio.create(familiensituationUpdateDtoSpecModel))
			.set(
					field(GesuchFormularUpdateDtoSpec::getAusbildung),
					Instancio.create(ausbildungUpdateDtoSpecModel))
			.toModel();
	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecFullModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecModelFull)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecPersonInAusbildungModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecPersonInAusbildungModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecFamiliensituationModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecFamiliensituationModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecAusbildungModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecAusbildungModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecPartnerModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecAusbildungModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecAuszahlungModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecAuszahlungModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecGeschwisterModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecAusbildungModel)
					)
					.toModel();
}
