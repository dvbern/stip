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

package ch.dvbern.stip.api.gesuchformular.util;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Stream;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.provider.Arguments;

@UtilityClass
public class CreatePermutationUtil {
    public <TUpdate, TEntity> Stream<Arguments> createIntegerPermutations(
        final Function<Integer, TUpdate> applyNew,
        final Function<Integer, TEntity> applyOld,
        final DokumentTyp expected
    ) {
        return Stream.of(
            Arguments.of(
                applyNew.apply(null),
                applyOld.apply(null),
                null
            ),
            Arguments.of(
                applyNew.apply(10),
                applyOld.apply(10),
                null
            ),
            Arguments.of(
                applyNew.apply(10),
                applyOld.apply(null),
                expected
            ),
            Arguments.of(
                applyNew.apply(null),
                applyOld.apply(10),
                null
            ),
            Arguments.of(
                applyNew.apply(10),
                applyOld.apply(12),
                expected
            ),
            Arguments.of(
                applyNew.apply(null),
                applyOld.apply(0),
                null
            ),
            Arguments.of(
                applyNew.apply(10),
                applyOld.apply(0),
                expected
            ),
            Arguments.of(
                applyNew.apply(0),
                applyOld.apply(null),
                null
            ),
            Arguments.of(
                applyNew.apply(0),
                applyOld.apply(10),
                null
            )
        );
    }

    public <TUpdate, TEntity> Stream<Arguments> createLocalDatePermutations(
        final Function<LocalDate, TUpdate> applyNew,
        final Function<LocalDate, TEntity> applyOld,
        final DokumentTyp expected
    ) {
        return Stream.of(
            Arguments.of(
                applyNew.apply(null),
                applyOld.apply(null),
                null
            ),
            Arguments.of(
                applyNew.apply(LocalDate.of(2025, 1, 1)),
                applyOld.apply(LocalDate.of(2025, 1, 1)),
                null
            ),
            Arguments.of(
                applyNew.apply(LocalDate.of(2025, 1, 1)),
                applyOld.apply(null),
                expected
            ),
            Arguments.of(
                applyNew.apply(null),
                applyOld.apply(LocalDate.of(2025, 1, 1)),
                expected
            ),
            Arguments.of(
                applyNew.apply(LocalDate.of(2025, 1, 1)),
                applyOld.apply(LocalDate.of(2024, 2, 20)),
                expected
            )
        );
    }
}
