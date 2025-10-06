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

package ch.dvbern.stip.api.common.service;

import java.util.HashSet;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class EntityOverrideMapperTest {
    private final EntityOverrideMapper entityOverrideMapper = Mappers.getMapper(EntityOverrideMapper.class);

    @Test
    void resetValueOnEntry() {
        // Arrange
        final var mutter = createOfType(ElternTyp.MUTTER);
        final var vater = createOfType(ElternTyp.VATER);

        final var sourceMutter = mutter.getLeft().setWohnkosten(100);
        final var sourceVater = vater.getLeft();
        final var sourceEltern = new HashSet<Eltern>();
        sourceEltern.add(sourceMutter);
        sourceEltern.add(sourceVater);

        final var targetMutter = mutter.getRight().setWohnkosten(500);
        final var targetVater = vater.getRight();
        final var targetEltern = new HashSet<Eltern>();
        targetEltern.add(targetMutter);
        targetEltern.add(targetVater);

        // Act
        entityOverrideMapper.overrideFromToEltern(sourceEltern, targetEltern);

        // Assert
        assertThat(targetMutter.getWohnkosten(), is(100));
    }

    @Test
    void restoreDeletedEntry() {
        // Arrange
        final var mutter = createOfType(ElternTyp.MUTTER);
        final var vater = createOfType(ElternTyp.VATER);

        final var sourceMutter = mutter.getLeft();
        final var sourceVater = vater.getLeft();
        final var sourceEltern = new HashSet<Eltern>();
        sourceEltern.add(sourceMutter);
        sourceEltern.add(sourceVater);

        final var targetMutter = mutter.getRight();
        final var targetEltern = new HashSet<Eltern>();
        targetEltern.add(targetMutter);

        // Act
        entityOverrideMapper.overrideFromToEltern(sourceEltern, targetEltern);

        // Assert
        assertThat(targetEltern.size(), is(2));

        final var targetVater = targetEltern.stream()
            .filter(eltern -> eltern.getElternTyp() == ElternTyp.VATER)
            .findFirst();
        assertThat(targetVater.isPresent(), is(true));
    }

    @Test
    void removeAddedEntry() {
        // Arrange
        final var mutter = createOfType(ElternTyp.MUTTER);
        final var vater = createOfType(ElternTyp.VATER);

        final var sourceMutter = mutter.getLeft();
        final var sourceEltern = new HashSet<Eltern>();
        sourceEltern.add(sourceMutter);

        final var targetMutter = mutter.getRight();
        final var targetVater = vater.getRight();
        final var targetEltern = new HashSet<Eltern>();
        targetEltern.add(targetMutter);
        targetEltern.add(targetVater);

        // Act
        entityOverrideMapper.overrideFromToEltern(sourceEltern, targetEltern);

        // Assert
        assertThat(targetEltern.size(), is(1));
        final var existing = targetEltern.stream().findFirst().get();
        assertThat(existing, is(not(nullValue())));
    }

    private Pair<Eltern, Eltern> createOfType(final ElternTyp elternTyp) {
        final var id = UUID.randomUUID();
        return Pair.of(
            (Eltern) new Eltern().setElternTyp(elternTyp).setAdresse(new Adresse()).setId(id),
            (Eltern) new Eltern().setElternTyp(elternTyp).setAdresse(new Adresse()).setId(id)
        );
    }
}
