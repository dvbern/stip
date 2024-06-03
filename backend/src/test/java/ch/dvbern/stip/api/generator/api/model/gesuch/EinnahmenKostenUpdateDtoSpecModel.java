package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class EinnahmenKostenUpdateDtoSpecModel {
    public static final EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(EinnahmenKostenUpdateDtoSpec::new, (model, faker) -> {
            model.setNettoerwerbseinkommen(TestUtil.getRandomInt());
            model.setZulagen(TestUtil.getRandomInt());
            model.setRenten(TestUtil.getRandomInt());
            model.setEoLeistungen(TestUtil.getRandomInt());
            model.setErgaenzungsleistungen(TestUtil.getRandomInt());
            model.setBeitraege(Math.min(TestUtil.getRandomInt(), 1));
            model.setAusbildungskostenSekundarstufeZwei(TestUtil.getRandomInt());
            model.setAusbildungskostenTertiaerstufe(TestUtil.getRandomInt());
            model.setFahrkosten(TestUtil.getRandomInt());
            model.setWohnkosten(TestUtil.getRandomInt());
            model.setVerdienstRealisiert(false);
            model.setWillDarlehen(false);
            model.setAuswaertigeMittagessenProWoche(faker.number().randomDigit());
            model.setBetreuungskostenKinder(TestUtil.getRandomInt());
        });

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKosten =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setEinnahmenKosten(einnahmenKostenUpdateDtoSpec));
}
