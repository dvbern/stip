package ch.dvbern.stip.api.generator.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
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
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
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
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_FIXED;

public final class GesuchGenerator {
    private GesuchGenerator() {
    }

    public static GesuchUpdateDto createFullGesuch() {
        GesuchUpdateDtoSpec gesuchFormular = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
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
        return LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs();
    }

    /* return a new instance of Gesuch with a valid GesuchPeriode */
    public static Gesuch initGesuch() {
        final DateRange gueltigkeitsRange;
        if (GUELTIGKEIT_PERIODE_23_24 != null) {
            gueltigkeitsRange = GUELTIGKEIT_PERIODE_23_24;
        } else {
            gueltigkeitsRange = GUELTIGKEIT_PERIODE_FIXED;
        }

        var ausbildungDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();

        var ausbildung = new Ausbildung()
            .setFall(new Fall())
            .setAusbildungsgang((Ausbildungsgang) new Ausbildungsgang().setId(ausbildungDtoSpec.getAusbildungsgangId()))
            .setAlternativeAusbildungsgang(ausbildungDtoSpec.getAlternativeAusbildungsgang())
            .setAlternativeAusbildungsstaette(ausbildungDtoSpec.getAlternativeAusbildungsstaette())
            .setFachrichtung(ausbildungDtoSpec.getFachrichtung())
            .setAusbildungNichtGefunden(ausbildungDtoSpec.getAusbildungNichtGefunden())
            .setAusbildungBegin(LocalDate.parse(ausbildungDtoSpec.getAusbildungBegin()))
            .setAusbildungEnd(LocalDate.parse(ausbildungDtoSpec.getAusbildungEnd()))
            .setPensum(AusbildungsPensum.VOLLZEIT)
            .setAusbildungsort(ausbildungDtoSpec.getAusbildungsort())
            .setIsAusbildungAusland(ausbildungDtoSpec.getIsAusbildungAusland());
        var gesuch = new Gesuch()
            .setAusbildung(ausbildung)
            .setGesuchsperiode(
                new Gesuchsperiode()
                    .setGesuchsjahr(new Gesuchsjahr().setTechnischesJahr(2023))
                    .setGesuchsperiodeStart(gueltigkeitsRange.getGueltigAb())
                    .setGesuchsperiodeStopp(gueltigkeitsRange.getGueltigBis())
            );
        gesuch.getGesuchTranchen().add((GesuchTranche) new GesuchTranche()
            .setGueltigkeit(gueltigkeitsRange)
            .setGesuch(gesuch)
            .setId(UUID.randomUUID()));
        return gesuch;
    }

    public static GesuchTranche initGesuchTranche() {
        return initGesuch().getGesuchTranchen().get(0);
    }

    private static FamiliensituationUpdateDtoSpec createFamiliensituation() {
        final var familienSituationUpdateDto = FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec();
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
        PartnerUpdateDtoSpec partnerDtoSpec = PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec();
        return partnerDtoSpec;
    }

    private static PersonInAusbildungUpdateDtoSpec createPersonInAusbildung() {
        return PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec();
    }

    private static EinnahmenKostenUpdateDtoSpec createEinnahmeKosten() {
        EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDto =
            EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec();
        einnahmenKostenUpdateDto.setVerdienstRealisiert(false);
        return einnahmenKostenUpdateDto;
    }

    private static AuszahlungUpdateDtoSpec createAuszahlung() {
        AuszahlungUpdateDtoSpec auszahlungUpdateDto = AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec();
        auszahlungUpdateDto.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        return auszahlungUpdateDto;
    }

    private static GesuchTrancheUpdateDtoSpec createGesuchTranche() {
        return GesuchTestSpecGenerator.gesuchTrancheDtoSpec();
    }
}
