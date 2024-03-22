package ch.dvbern.stip.api.generator.api;

import ch.dvbern.stip.api.generator.api.model.gesuch.*;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class GesuchTestSpecGenerator {
    public static GesuchTrancheUpdateDtoSpec gesuchTrancheDtoSpec() {
        final var model = new GesuchTrancheUpdateDtoSpec();
        model.setId(UUID.randomUUID());
        return model;
    }

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecPersonInAusbildung =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildung);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecFamiliensituation =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituation);
        });

    @Test
    void test() {
        var foo = gesuchUpdateDtoSpecAusbildung;
        System.out.println();
    }

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecPartner =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(PartnerUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPartner);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecAusbildung =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(AusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAusbildung);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecAuszahlung =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(AuszahlungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecAuszahlung);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecGeschwister =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(GeschwisterUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecGeschwisters);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecLebenslauf =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(LebenslaufItemUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecLebenslauf);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecElterns =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(ElternUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecElterns);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecEinnahmenKosten =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(EinnahmenKostenUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecEinnahmenKosten);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecKinder =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(KindUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecKinder);
        });

    private static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFull =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> {
            model.setPersonInAusbildung(PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec);
            model.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(16));
            model.getPersonInAusbildung().setZivilstand(ZivilstandDtoSpec.VERHEIRATET);

            model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec);
            model.setAusbildung(AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec);
            model.setPartner(PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec);
            model.setAuszahlung(AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec);

            model.setLebenslaufItems(LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs
                .stream()
                .peek(li -> {
                    li.setVon(LocalDate.now().minusMonths(2 + 4).with(firstDayOfMonth())
                        .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
                    li.setBis(LocalDate.now().minusMonths(2).with(lastDayOfMonth())
                        .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
                })
                .toList()
            );

            model.setElterns(ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2));
            model.getElterns().get(0).setElternTyp(ElternTypDtoSpec.VATER);
            model.getElterns().get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
            model.getElterns().get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
            model.getElterns().get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);

            model.setEinnahmenKosten(EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec);
            model.setKinds(KindUpdateDtoSpecModel.kindUpdateDtoSpecs);
        });

    public static final GesuchUpdateDtoSpec gesuchUpdateDtoSpecFull =
        TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model, faker) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.fromString(faker.internet().uuid()));
            model.getGesuchTrancheToWorkWith().setGesuchFormular(gesuchFormularUpdateDtoSpecFull);
        });
}
