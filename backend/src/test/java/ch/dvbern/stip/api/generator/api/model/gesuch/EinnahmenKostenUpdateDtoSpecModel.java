package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class EinnahmenKostenUpdateDtoSpecModel {
    public static final EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(EinnahmenKostenUpdateDtoSpec::new, (model, faker) -> {
            model.setNettoerwerbseinkommen(TestUtil.getRandomBigDecimal());
            model.setZulagen(TestUtil.getRandomBigDecimal());
            model.setRenten(TestUtil.getRandomBigDecimal());
            model.setEoLeistungen(TestUtil.getRandomBigDecimal());
            model.setErgaenzungsleistungen(TestUtil.getRandomBigDecimal());
            model.setBeitraege(TestUtil.getRandomBigDecimal());
            model.setAusbildungskostenSekundarstufeZwei(TestUtil.getRandomBigDecimal());
            model.setAusbildungskostenTertiaerstufe(TestUtil.getRandomBigDecimal());
            model.setFahrkosten(TestUtil.getRandomBigDecimal());
            model.setWohnkosten(TestUtil.getRandomBigDecimal());
            model.setVerdienstRealisiert(false);
            model.setWillDarlehen(false);
            model.setAuswaertigeMittagessenProWoche(faker.number().randomDigit());
        });

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKosten =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setEinnahmenKosten(einnahmenKostenUpdateDtoSpec));
}
