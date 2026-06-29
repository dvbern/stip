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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapperImpl;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GeschwisterDto;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class GesuchTrancheMapperTest {
    GesuchTrancheMapper gesuchTrancheMapper;

    @BeforeEach
    void setup() {
        gesuchTrancheMapper = new GesuchTrancheMapperImpl(null);
        gesuchTrancheMapper.geschwisterMapper = new GeschwisterMapperImpl();
    }

    @Test
    void testRemoveHiddenGeschwistersRemovesHiddenGeschwisters() {
        // Arrange
        final var publicGeschwister = createGeschwister(UUID.randomUUID(), false);
        final var publicGeschwisterDto = new GeschwisterDto();
        publicGeschwisterDto.setId(publicGeschwister.getId());

        final var hiddenGeschwister = createGeschwister(UUID.randomUUID(), true);
        final var hiddenGeschwisterDto = new GeschwisterDto();
        hiddenGeschwisterDto.setId(hiddenGeschwister.getId());

        final var formular = new GesuchFormular();
        formular.setGeschwisters(Set.of(publicGeschwister, hiddenGeschwister));
        final var tranche = new GesuchTranche();
        tranche.setGesuchFormular(formular);

        final var dtoFormular = new GesuchFormularDto();
        dtoFormular.setGeschwisters(List.of(publicGeschwisterDto, hiddenGeschwisterDto));
        final var trancheDto = new GesuchTrancheDto();
        trancheDto.setGesuchFormular(dtoFormular);

        // Act
        gesuchTrancheMapper.removeHiddenGeschwistersData(trancheDto, tranche);

        // Assert
        assertThat(trancheDto.getGesuchFormular().getGeschwisters().size(), is(1));
        assertThat(trancheDto.getGesuchFormular().getGeschwisters(), contains(publicGeschwisterDto));
    }

    @Test
    void testBeforeMappingAddHiddenGeschwistersAddsHiddenGeschwisters() {
        // Arrange
        final var publicGeschwister = createGeschwister(UUID.randomUUID(), false);
        final var publicGeschwisterDto = new GeschwisterUpdateDto();
        publicGeschwisterDto.setId(publicGeschwister.getId());

        final var hiddenGeschwister = createGeschwister(UUID.randomUUID(), true);
        final var hiddenGeschwisterDto = new GeschwisterUpdateDto();
        hiddenGeschwisterDto.setId(hiddenGeschwister.getId());

        final var formular = new GesuchFormular();
        formular.setGeschwisters(Set.of(publicGeschwister, hiddenGeschwister));
        final var tranche = new GesuchTranche();
        tranche.setGesuchFormular(formular);

        final var formularDto = new GesuchFormularUpdateDto();
        formularDto.setGeschwisters(List.of(publicGeschwisterDto));
        final var trancheDto = new GesuchTrancheUpdateDto();
        trancheDto.setGesuchFormular(formularDto);

        // Act
        gesuchTrancheMapper.beforeMappingAddHiddenGeschwisters(trancheDto, tranche);

        // Assert
        assertThat(formularDto.getGeschwisters().size(), is(2));
        assertThat(
            formularDto.getGeschwisters().stream().map(GeschwisterUpdateDto::getId).toList(),
            containsInAnyOrder(publicGeschwisterDto.getId(), hiddenGeschwisterDto.getId())
        );
    }

    private Geschwister createGeschwister(
        final UUID uuid,
        final boolean hidden
    ) {
        final var geschwister = new Geschwister();
        geschwister.setId(uuid);
        geschwister.setHidden(hidden);
        return geschwister;
    }

}
