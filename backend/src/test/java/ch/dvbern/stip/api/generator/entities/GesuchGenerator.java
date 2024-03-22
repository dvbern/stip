package ch.dvbern.stip.api.generator.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.generator.entities.service.GesuchUpdateDtoMapper;
import ch.dvbern.stip.api.generator.entities.service.GesuchUpdateDtoMapperImpl;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.ElternAbwesenheitsGrundDtoSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDtoSpec;
import org.instancio.Instancio;

import static ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator.gesuchTrancheDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator.gesuchUpdateDtoSpecFullModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel.elternUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.PartnerUpdateDtoSpecModel.partnerUpdateDtoSpecModel;
import static ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpecModel;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.instancio.Select.field;

public final class GesuchGenerator {
    private GesuchGenerator() {
    }

    public static GesuchUpdateDto createFullGesuch() {
        GesuchUpdateDtoSpec gesuchFormular = Instancio.of(gesuchUpdateDtoSpecFullModel).create();
        return new GesuchUpdateDtoMapperImpl().toEntity(gesuchFormular);
    }

    public static GesuchUpdateDto createGesuch() {
        GesuchFormularUpdateDtoSpec gesuchFormularToWorkWith = new GesuchFormularUpdateDtoSpec();
        gesuchFormularToWorkWith.setPersonInAusbildung(createPersonInAusbildung());
        gesuchFormularToWorkWith.setElterns(createElterns());
        gesuchFormularToWorkWith.setFamiliensituation(createFamiliensituation());
        gesuchFormularToWorkWith.setEinnahmenKosten(createEinnahmeKosten());
        gesuchFormularToWorkWith.setLebenslaufItems(createLebenslaufItems());
        gesuchFormularToWorkWith.setAuszahlung(createAuszahlung());
        gesuchFormularToWorkWith.setPartner(createPartner());

        GesuchTrancheUpdateDtoSpec gesuchTrancheDtoSpec = createGesuchTranche();
        gesuchTrancheDtoSpec.setGesuchFormular(gesuchFormularToWorkWith);

        GesuchUpdateDtoSpec gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
        gesuchUpdateDtoSpec.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec);
        GesuchUpdateDtoMapper gesuchUpdateDtoMapper = new GesuchUpdateDtoMapperImpl();
        return gesuchUpdateDtoMapper.toEntity(gesuchUpdateDtoSpec);
    }

    private static List<LebenslaufItemUpdateDtoSpec> createLebenslaufItems() {
        return Instancio.of(lebenslaufItemUpdateDtoSpecModel).create();
    }

    public static Gesuch initGesuch() {
        var gesuch = new Gesuch()
            .setFall(new Fall())
            .setGesuchsperiode(new Gesuchsperiode().setGueltigkeit(GUELTIGKEIT_PERIODE_23_24));
        gesuch.getGesuchTranchen().add((GesuchTranche) new GesuchTranche()
            .setGueltigkeit(GUELTIGKEIT_PERIODE_23_24)
            .setGesuch(gesuch)
            .setId(UUID.randomUUID()));
        return gesuch;
    }

    public static GesuchTranche initGesuchTranche() {
        return initGesuch().getGesuchTranchen().get(0);
    }

    private static FamiliensituationUpdateDtoSpec createFamiliensituation() {
        return Instancio.of(familiensituationUpdateDtoSpecModel)
            .set(field(FamiliensituationUpdateDtoSpec::getElternVerheiratetZusammen), true)
            .set(field(FamiliensituationUpdateDtoSpec::getVaterUnbekanntVerstorben), ElternAbwesenheitsGrundDtoSpec.WEDER_NOCH)
            .set(field(FamiliensituationUpdateDtoSpec::getMutterUnbekanntVerstorben), ElternAbwesenheitsGrundDtoSpec.WEDER_NOCH)
            .create();
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

    private static AuszahlungUpdateDtoSpec createAuszahlung() {
        AuszahlungUpdateDtoSpec auszahlungUpdateDto = Instancio.of(auszahlungUpdateDtoSpecModel)
            .set(
                field(AuszahlungUpdateDtoSpec::getIban),
                TestConstants.IBAN_CH_NUMMER_VALID
            )
            .create();
        return auszahlungUpdateDto;
    }

    private static GesuchTrancheUpdateDtoSpec createGesuchTranche() {
        return Instancio.of(gesuchTrancheDtoSpecModel).create();
    }
}
