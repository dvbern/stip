package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KontoinhaberDtoSpec;

public final class AuszahlungUpdateDtoSpecModel {
    public static final AuszahlungUpdateDtoSpec auszahlungUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(AuszahlungUpdateDtoSpec::new, (model, faker) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec);
            model.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setKontoinhaber(KontoinhaberDtoSpec.SOZIALDIENST_INSTITUTION);
        });

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAuszahlung =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setAuszahlung(AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec));
}
