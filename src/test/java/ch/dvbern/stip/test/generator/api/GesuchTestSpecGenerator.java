package ch.dvbern.stip.test.generator.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import ch.dvbern.oss.stip.contract.test.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ElternTypDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ElternUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.PersonInAusbildungUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ZivilstandDtoSpec;
import ch.dvbern.stip.test.util.TestConstants;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.generator.api.model.gesuch.AdresseSpecModel.adresseSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAuszahlungModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecEinnahmenKostenModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.ElternUpdateDtoSpecModel.elternUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.ElternUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecElternsModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituationModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel.geschwisterUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecGeschwistersModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.KindUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecKinderModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.KindUpdateDtoSpecModel.kinderUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecLebenslaufModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.PartnerUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPartnerModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.PartnerUpdateDtoSpecModel.partnerUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildungModel;
import static ch.dvbern.stip.test.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpecModel;
import static org.instancio.Select.field;

public class GesuchTestSpecGenerator {

	private static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecModelFull = Instancio.of(
					GesuchFormularUpdateDtoSpec.class)
			.set(
					field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung),
					Instancio.of(personInAusbildungUpdateDtoSpecModel)
							.set(field(PersonInAusbildungUpdateDtoSpec::getAdresse), Instancio.create(adresseSpecModel))
							.set(field(PersonInAusbildungUpdateDtoSpec::getGeburtsdatum), LocalDate.of(2000, 10, 10))
							.set(field(PersonInAusbildungUpdateDtoSpec::getZivilstand), ZivilstandDtoSpec.VERHEIRATET)
							.create())
			.set(
					field(GesuchFormularUpdateDtoSpec::getFamiliensituation),
					Instancio.create(familiensituationUpdateDtoSpecModel))
			.set(
					field(GesuchFormularUpdateDtoSpec::getAusbildung),
					Instancio.create(ausbildungUpdateDtoSpecModel))
			.set(
					field(GesuchFormularUpdateDtoSpec::getPartner),
					Instancio.create(partnerUpdateDtoSpecModel)
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getAuszahlung),
					Instancio.create(auszahlungUpdateDtoSpecModel)
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getGeschwisters),
					Instancio.create(geschwisterUpdateDtoSpecModel)
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getLebenslaufItems),
					Instancio.of(lebenslaufItemUpdateDtoSpecModel)
							.set(field(LebenslaufItemUpdateDtoSpec::getVon), "08.2016")
							.set(field(LebenslaufItemUpdateDtoSpec::getBis), "12.2021")
							.create()
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getElterns),
					Arrays.asList(
							Instancio.of(elternUpdateDtoSpecModel)
									.set(field(ElternUpdateDtoSpec::getElternTyp), ElternTypDtoSpec.VATER)
									.create()
									.get(0),
							Instancio.of(elternUpdateDtoSpecModel)
									.set(field(ElternUpdateDtoSpec::getElternTyp), ElternTypDtoSpec.MUTTER)
									.set(
											field(ElternUpdateDtoSpec::getSozialversicherungsnummer),
											TestConstants.AHV_NUMMER_VALID_MUTTER)
									.create()
									.get(0))
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten),
					Instancio.of(einnahmenKostenUpdateDtoSpecModel).set(
							field(EinnahmenKostenUpdateDtoSpec::getZulagen),
							BigDecimal.TEN).create()
			)
			.set(
					field(GesuchFormularUpdateDtoSpec::getKinds),
					Instancio.create(kinderUpdateDtoSpecModel)
			)
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
