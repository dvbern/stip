/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.SteuererklaerungUpdateDtoSpec;

public class SteuererklaerungUpdateTabsDtoSpecModel {
    public static SteuererklaerungUpdateDtoSpec steuererklaerungDtoSpec(
        final SteuerdatenTypDtoSpec steuerdatenTypDtoSpec
    ) {
        return TestUtil.createUpdateDtoSpec(SteuererklaerungUpdateDtoSpec::new, (model) -> {
            model.setSteuerdatenTyp(steuerdatenTypDtoSpec);
            model.setSteuererklaerungInBern(TestUtil.getRandomBoolean());
        });
    }

    public static List<SteuererklaerungUpdateDtoSpec> steuererklaerungDtoSpecs(
        final SteuerdatenTypDtoSpec... steuerdatenTypDtoSpecs
    ) {
        final var list = new ArrayList<SteuererklaerungUpdateDtoSpec>();
        for (final var typ : steuerdatenTypDtoSpecs) {
            list.add(steuererklaerungDtoSpec(typ));
        }
        return list;
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecSteuererklaerung() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> {
                model.setSteuererklaerung(steuererklaerungDtoSpecs(SteuerdatenTypDtoSpec.FAMILIE));
                model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec());
            }
        );
    }
}
