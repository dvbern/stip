package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;

import java.time.LocalDate;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PARTNER;

public class PartnerUpdateDtoSpecModel {
    public static final PartnerUpdateDtoSpec partnerUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec::new, (model, faker) -> {
            model.setVorname(faker.name().firstName());
            model.setNachname(faker.name().lastName());
            model.setGeburtsdatum(TestUtil.getRandomLocalDateBetween(LocalDate.of(1990, 1, 1), LocalDate.of(2002, 1, 1)));
            model.setAdresse(AdresseSpecModel.adresseDtoSpec);
            model.setSozialversicherungsnummer(AHV_NUMMER_VALID_PARTNER);
            model.setAusbildungMitEinkommenOderErwerbstaetig(true);
            model.setFahrkosten(TestUtil.getRandomBigDecimal());
            model.setJahreseinkommen(TestUtil.getRandomBigDecimal());
            model.setVerpflegungskosten(TestUtil.getRandomBigDecimal());
        });

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecPartner =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setPartner(partnerUpdateDtoSpec));
}
