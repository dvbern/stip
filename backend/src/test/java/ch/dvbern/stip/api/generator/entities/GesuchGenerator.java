package ch.dvbern.stip.api.generator.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PartnerUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.entities.service.GesuchUpdateDtoMapper;
import ch.dvbern.stip.api.generator.entities.service.GesuchUpdateDtoMapperImpl;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.partner.service.PartnerMapperImpl;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.generated.dto.*;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;

public final class GesuchGenerator {
    private GesuchGenerator() {
    }

    public static GesuchUpdateDto createFullGesuch() {
        GesuchUpdateDtoSpec gesuchFormular = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull;
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
        gesuchFormularToWorkWith.setSteuerdaten(new ArrayList<>());
        gesuchFormularToWorkWith.getSteuerdaten().add(new SteuerdatenUpdateDtoSpec());

        GesuchTrancheUpdateDtoSpec gesuchTrancheDtoSpec = createGesuchTranche();
        gesuchTrancheDtoSpec.setGesuchFormular(gesuchFormularToWorkWith);

        GesuchUpdateDtoSpec gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
        gesuchUpdateDtoSpec.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec);
        GesuchUpdateDtoMapper gesuchUpdateDtoMapper = new GesuchUpdateDtoMapperImpl();
        return gesuchUpdateDtoMapper.toEntity(gesuchUpdateDtoSpec);
    }

    private static List<LebenslaufItemUpdateDtoSpec> createLebenslaufItems() {
        return LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs;
    }

    public static Gesuch initGesuch() {
        var gesuch = new Gesuch()
            .setFall(new Fall())
            .setGesuchsperiode(
                new Gesuchsperiode()
                    .setGesuchsjahr(new Gesuchsjahr().setTechnischesJahr(2023))
                    .setGesuchsperiodeStart(GUELTIGKEIT_PERIODE_23_24.getGueltigAb())
                    .setGesuchsperiodeStopp(GUELTIGKEIT_PERIODE_23_24.getGueltigBis())
            );
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
        final var familienSituationUpdateDto = FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec;
        familienSituationUpdateDto.setElternVerheiratetZusammen(true);
        return familienSituationUpdateDto;
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
        return ElternUpdateDtoSpecModel.elternUpdateDtoSpecs().get(0);
    }

    private static PartnerUpdateDtoSpec createPartner() {
        PartnerUpdateDtoSpec partnerDtoSpec = PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec;
        return partnerDtoSpec;
    }

    private static PersonInAusbildungUpdateDtoSpec createPersonInAusbildung() {
        return PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec;
    }

    private static EinnahmenKostenUpdateDtoSpec createEinnahmeKosten() {
        EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDto =
            EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec;
        einnahmenKostenUpdateDto.setVerdienstRealisiert(false);
        return einnahmenKostenUpdateDto;
    }

    private static AuszahlungUpdateDtoSpec createAuszahlung() {
        AuszahlungUpdateDtoSpec auszahlungUpdateDto = AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec;
        auszahlungUpdateDto.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        return auszahlungUpdateDto;
    }

    private static GesuchTrancheUpdateDtoSpec createGesuchTranche() {
        return GesuchTestSpecGenerator.gesuchTrancheDtoSpec();
    }
}
