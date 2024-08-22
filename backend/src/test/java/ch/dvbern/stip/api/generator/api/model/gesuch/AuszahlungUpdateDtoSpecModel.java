package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KontoinhaberDtoSpec;

public final class AuszahlungUpdateDtoSpecModel {
    public static AuszahlungUpdateDtoSpec auszahlungUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AuszahlungUpdateDtoSpec::new, (model) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
            model.setVorname("John");
            model.setNachname("Doe");
            model.setKontoinhaber(KontoinhaberDtoSpec.SOZIALDIENST_INSTITUTION);
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAuszahlung() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setAuszahlung(AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec())
        );
    }
}
