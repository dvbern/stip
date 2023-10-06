package ch.dvbern.stip.test.generator.entities;

import ch.dvbern.oss.stip.contract.test.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ElternTypDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ElternUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.FamiliensituationUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.PartnerUpdateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.PersonInAusbildungUpdateDtoSpec;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.test.generator.entities.service.GesuchUpdateDtoMapper;
import ch.dvbern.stip.test.generator.entities.service.GesuchUpdateDtoMapperImpl;
import ch.dvbern.stip.test.util.TestConstants;
import org.instancio.Instancio;

import java.util.ArrayList;
import java.util.List;

import static ch.dvbern.stip.test.generator.api.model.EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.ElternUpdateDtoSpecModel.elternUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.PartnerUpdateDtoSpecModel.partnerUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpecModel;
import static ch.dvbern.stip.test.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.instancio.Select.field;


public final class GesuchGenerator {
	private GesuchGenerator() {
	}

	public static GesuchUpdateDto createGesuch() {
		GesuchFormularUpdateDtoSpec gesuchFormularToWorkWith = new GesuchFormularUpdateDtoSpec();
		gesuchFormularToWorkWith.setPersonInAusbildung(createPersonInAusbildung());
		gesuchFormularToWorkWith.setElterns(createElterns());
		gesuchFormularToWorkWith.setFamiliensituation(createFamiliensituation());
		gesuchFormularToWorkWith.setEinnahmenKosten(createEinnahmeKosten());
		GesuchUpdateDtoSpec gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
		gesuchUpdateDtoSpec.setGesuchFormularToWorkWith(gesuchFormularToWorkWith);
		GesuchUpdateDtoMapper gesuchUpdateDtoMapper = new GesuchUpdateDtoMapperImpl();
		return gesuchUpdateDtoMapper.toEntity(gesuchUpdateDtoSpec);
	}

	public static Gesuch initGesuch() {
		return new Gesuch()
				.setFall(new Fall())
				.setGesuchsperiode(new Gesuchsperiode().setGueltigkeit(GUELTIGKEIT_PERIODE_23_24));
	}

	private static FamiliensituationUpdateDtoSpec createFamiliensituation() {
		return Instancio.of(familiensituationUpdateDtoSpecModel)
						.set(field(FamiliensituationUpdateDtoSpec::getElternVerheiratetZusammen), true).create();
	}

	private static List<ElternUpdateDtoSpec> createElterns() {
		List<ElternUpdateDtoSpec> elterns = new ArrayList<>();
		ElternUpdateDtoSpec mutter = createEltern();
		mutter.setElternTyp(ElternTypDtoSpec.MUTTER);
		mutter.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);
		elterns.add(mutter);
		elterns.add(createEltern());
		return elterns;
	}

	private static ElternUpdateDtoSpec createEltern() {
		return Instancio.of(elternUpdateDtoSpecModel).create().get(0);
	}

	private static PartnerUpdateDtoSpec createPartner() {
		PartnerUpdateDtoSpec partnerDtoSpec = Instancio.of(partnerUpdateDtoSpecModel).create();
		return partnerDtoSpec;
	}

	private static PersonInAusbildungUpdateDtoSpec createPersonInAusbildung() {
		PersonInAusbildungUpdateDtoSpec personInAusbildungUpdateDtoSpec =
				Instancio.of(personInAusbildungUpdateDtoSpecModel).create();
		return personInAusbildungUpdateDtoSpec;
	}

	private static EinnahmenKostenUpdateDtoSpec createEinnahmeKosten() {
		EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDto = Instancio.of(einnahmenKostenUpdateDtoSpecModel)
				.set(field(EinnahmenKostenUpdateDtoSpec::getVerdienstRealisiert), false).create();
		return einnahmenKostenUpdateDto;
	}
}
