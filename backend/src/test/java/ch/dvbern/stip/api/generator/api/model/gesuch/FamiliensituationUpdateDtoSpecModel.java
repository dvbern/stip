package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.*;

import java.math.BigDecimal;

public final class FamiliensituationUpdateDtoSpecModel {
    public static final FamiliensituationUpdateDtoSpec familiensituationUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(FamiliensituationUpdateDtoSpec::new, (model, faker) -> {
            model.setObhut(ElternschaftsteilungDtoSpec.GEMEINSAM);
            model.setGerichtlicheAlimentenregelung(false);
            model.setElternVerheiratetZusammen(false);
            model.setElternteilUnbekanntVerstorben(false);
            model.setMutterWiederverheiratet(false);
            model.setVaterWiederverheiratet(false);
            model.setWerZahltAlimente(null);
            model.setObhutMutter(TestUtil.getRandomBigDecimal(0, 100, 0));
            model.setObhutVater(BigDecimal.valueOf(100).subtract(model.getObhutMutter()));
            model.setSorgerecht(ElternschaftsteilungDtoSpec.MUTTER);
            model.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrundDtoSpec.VERSTORBEN);
            model.setVaterUnbekanntGrund(ElternUnbekanntheitsGrundDtoSpec.FEHLENDE_ANERKENNUNG);
            model.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrundDtoSpec.UNBEKANNT);
            model.setMutterUnbekanntGrund(ElternUnbekanntheitsGrundDtoSpec.UNBEKANNTER_AUFENTHALTSORT);
        });

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFamiliensituation =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setFamiliensituation(familiensituationUpdateDtoSpec));
}
