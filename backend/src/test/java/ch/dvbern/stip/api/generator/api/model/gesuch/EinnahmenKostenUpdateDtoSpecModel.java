package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class EinnahmenKostenUpdateDtoSpecModel {
    public static final EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(EinnahmenKostenUpdateDtoSpec::new, (model, faker) -> {
            model.setNettoerwerbseinkommen(TestUtil.getRandomInt(1, 2500));
            model.setZulagen(TestUtil.getRandomInt(1, 2500));
            model.setRenten(TestUtil.getRandomInt(1, 2500));
            model.setEoLeistungen(TestUtil.getRandomInt(1, 2500));
            model.setErgaenzungsleistungen(TestUtil.getRandomInt(1, 2500));
            model.setBeitraege(Math.min(TestUtil.getRandomInt(1, 2500), 1));
            model.setAusbildungskostenSekundarstufeZwei(TestUtil.getRandomInt(1, 2500));
            model.setAusbildungskostenTertiaerstufe(TestUtil.getRandomInt(1, 2500));
            model.setFahrkosten(TestUtil.getRandomInt(1, 2500));
            model.setWohnkosten(TestUtil.getRandomInt(1, 2500));
            model.setVerdienstRealisiert(false);
            model.setWillDarlehen(false);
            model.setAuswaertigeMittagessenProWoche(faker.number().randomDigit());
            model.setBetreuungskostenKinder(TestUtil.getRandomInt(1, 2500));
            model.setVeranlagungsCode(TestUtil.getRandomInt(0,99));
        });

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKosten =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setEinnahmenKosten(einnahmenKostenUpdateDtoSpec));
}
