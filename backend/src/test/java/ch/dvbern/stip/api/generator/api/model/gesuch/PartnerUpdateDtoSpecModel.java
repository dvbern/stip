package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PARTNER;

public class PartnerUpdateDtoSpecModel {
    public static PartnerUpdateDtoSpec partnerUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec::new, (model) -> {
            model.setVorname("Test");
            model.setNachname("Partner");
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2002, 1, 1))
            );
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setSozialversicherungsnummer(AHV_NUMMER_VALID_PARTNER);
            model.setAusbildungMitEinkommenOderErwerbstaetig(true);
            model.setJahreseinkommen(5000);
            model.setFahrkosten(2500);
            model.setVerpflegungskosten(TestUtil.getRandomInt(1, 2000));
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecPartner() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setPartner(partnerUpdateDtoSpec())
        );
    }
}
