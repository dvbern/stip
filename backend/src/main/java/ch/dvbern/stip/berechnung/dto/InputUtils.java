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

package ch.dvbern.stip.berechnung.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.dto.PersonValueItemDto;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class InputUtils {
    public static final int PIA_COUNT = 1;

    public static ElternTyp fromSteuerdatenTyp(SteuerdatenTyp steuerdatenTyp) {
        return switch (steuerdatenTyp) {
            case SteuerdatenTyp.MUTTER -> ElternTyp.MUTTER;
            case SteuerdatenTyp.VATER, SteuerdatenTyp.FAMILIE -> ElternTyp.VATER;
        };
    }

    public static Pair<Eltern, Optional<Eltern>> getElterteileToUse(
        List<Eltern> eltern,
        boolean verheiratetOderZusammen,
        ElternTyp elternTyp
    ) {
        final var elternsByType = eltern.stream().collect(Collectors.toMap(Eltern::getElternTyp, Function.identity()));
        return Pair.of(
            elternsByType.remove(elternTyp),
            verheiratetOderZusammen ? elternsByType.values().stream().findFirst() : Optional.empty()
        );
    }

    public static int sumValues(List<PersonValueItemDto> values) {
        return values.stream().mapToInt(PersonValueItemDto::getValue).sum();
    }

    public static int sumNullables(Integer... values) {
        return Arrays.stream(values).filter(Objects::nonNull).mapToInt(value -> value).sum();
    }

    public static int toJahresWert(final int monatsWert) {
        return monatsWert * 12;
    }

    public static Integer toJahresWert(final Integer monatsWert) {
        if (Objects.isNull(monatsWert)) {
            return null;
        }
        return monatsWert * 12;
    }
}
