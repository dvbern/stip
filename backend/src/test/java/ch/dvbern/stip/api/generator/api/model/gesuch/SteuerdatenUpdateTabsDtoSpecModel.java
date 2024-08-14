package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDtoSpec;

public class SteuerdatenUpdateTabsDtoSpecModel {
    public static SteuerdatenUpdateDtoSpec steuerdatenDtoSpec(final SteuerdatenTypDtoSpec steuerdatenTypDtoSpec) {
        return TestUtil.createUpdateDtoSpec(SteuerdatenUpdateDtoSpec::new, (model, faker) -> {
            model.setSteuerdatenTyp(steuerdatenTypDtoSpec);
            model.setTotalEinkuenfte(TestUtil.getRandomInt(1, 10000));
            model.setEigenmietwert(TestUtil.getRandomInt(1, 10000));
            model.setIsArbeitsverhaeltnisSelbstaendig(false);
            model.saeule3a(TestUtil.getRandomInt(1, 10000));
            model.saeule2(TestUtil.getRandomInt(1, 10000));
            model.setKinderalimente(TestUtil.getRandomInt(1, 10000));
            model.ergaenzungsleistungen(TestUtil.getRandomInt(1, 10000));
            model.setVermoegen(TestUtil.getRandomInt(1, 10000));
            model.setSteuernKantonGemeinde(TestUtil.getRandomInt(1, 10000));
            model.steuernBund(TestUtil.getRandomInt(1, 10000));
            model.setFahrkosten(TestUtil.getRandomInt(1, 10000));
            model.setFahrkostenPartner(TestUtil.getRandomInt(1, 10000));
            model.setVerpflegung(TestUtil.getRandomInt(1, 10000));
            model.setVerpflegungPartner(TestUtil.getRandomInt(1, 10000));
            model.setVeranlagungsCode(0);
            model.setSteuerjahr(2022);
        });
    }

    public static List<SteuerdatenUpdateDtoSpec> steuerdatenDtoSpecs(final SteuerdatenTypDtoSpec... steuerdatenTypDtoSpecs) {
        final var list = new ArrayList<SteuerdatenUpdateDtoSpec>();
        for (final var typ : steuerdatenTypDtoSpecs) {
            list.add(steuerdatenDtoSpec(typ));
        }

        return list;
    }

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecSteuerdaten =
        TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model, faker) -> {
                model.setSteuerdaten(steuerdatenDtoSpecs(SteuerdatenTypDtoSpec.FAMILIE));
                model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec);
            }
        );
}
