package ch.dvbern.stip.api.generator.api;

import java.util.UUID;

import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.KindUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PartnerUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.ZivilstandDtoSpec;

public class GesuchTestSpecGenerator {
    public static GesuchTrancheUpdateDtoSpec gesuchTrancheDtoSpec() {
        var model = new GesuchTrancheUpdateDtoSpec();
        model.setId(UUID.randomUUID());
        return model;
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecPersonInAusbildung() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildung()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecFamiliensituation() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituation()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecPartner() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                PartnerUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPartner()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecAusbildung() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                AusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAusbildung()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecAuszahlung() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                AuszahlungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAuszahlung()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecGeschwister() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                GeschwisterUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecGeschwisters()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecLebenslauf() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                LebenslaufItemUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecLebenslauf()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecElterns() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                ElternUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecElterns()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSteuerdatenTabs() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                SteuerdatenUpdateTabsDtoSpecModel.gesuchFormularUpdateDtoSpecSteuerdaten()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecEinnahmenKosten() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                EinnahmenKostenUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecEinnahmenKosten()
            );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecKinder() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(
                KindUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecKinder()
            );
        });
    }

    private static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFull() {
        return TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> {
            model.setPersonInAusbildung(PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec());
            model.getPersonInAusbildung().setZivilstand(ZivilstandDtoSpec.VERHEIRATET);
            model.setAusbildung(AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec());
            model.setLebenslaufItems(LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs());
            model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec());
            model.setElterns(ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2));
            model.getElterns().get(0).setElternTyp(ElternTypDtoSpec.VATER);
            model.getElterns().get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
            model.getElterns().get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
            model.getElterns().get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);
            model.setSteuerdaten(SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpecs(SteuerdatenTypDtoSpec.FAMILIE));
            model.setPartner(PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec());
            model.setKinds(KindUpdateDtoSpecModel.kindUpdateDtoSpecs());
            model.setAuszahlung(AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec());
            model.setEinnahmenKosten(EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec());
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecFull() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(gesuchFormularUpdateDtoSpecFull());
        });
    }
}
