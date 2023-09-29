package ch.dvbern.stip.test.generator.api;

import ch.dvbern.oss.stip.contract.test.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.KindUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.generator.api.model.AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.AusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.AuszahlungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAuszahlungModel;
import static ch.dvbern.stip.test.generator.api.model.EinnahmenKostenUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecEinnahmenKostenModel;
import static ch.dvbern.stip.test.generator.api.model.ElternUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecElternsModel;
import static ch.dvbern.stip.test.generator.api.model.FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituationModel;
import static ch.dvbern.stip.test.generator.api.model.GeschwisterUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecGeschwistersModel;
import static ch.dvbern.stip.test.generator.api.model.KindUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecKinderModel;
import static ch.dvbern.stip.test.generator.api.model.LebenslaufItemUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecLebenslaufModel;
import static ch.dvbern.stip.test.generator.api.model.PartnerUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPartnerModel;
import static ch.dvbern.stip.test.generator.api.model.PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpecModel;
import static org.instancio.Select.field;

public class GesuchTestSpecGenerator {

	private static final Model<KindUpdateDtoSpec> kindUpdateDtoSpecModel =
			Instancio.of(KindUpdateDtoSpec.class)
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
	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecPartnerModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecPartnerModel)
					)
					.toModel();
	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecAusbildungModel =
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
							Instancio.create(gesuchFormularUpdateDtoSpecGeschwistersModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecLebenslaufModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecLebenslaufModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecElternsModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecElternsModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecEinnahmenKostenModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecEinnahmenKostenModel)
					)
					.toModel();

	public static final Model<GesuchUpdateDtoSpec> gesuchUpdateDtoSpecKinderModel =
			Instancio.of(GesuchUpdateDtoSpec.class)
					.set(
							field(GesuchUpdateDtoSpec::getGesuchFormularToWorkWith),
							Instancio.create(gesuchFormularUpdateDtoSpecKinderModel)
					)
					.toModel();


}
