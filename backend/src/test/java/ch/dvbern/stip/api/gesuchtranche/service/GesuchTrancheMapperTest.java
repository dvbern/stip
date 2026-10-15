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

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.service.AbschlussService;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapperImpl;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GeschwisterDto;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GesuchTrancheMapperTest {
    GesuchTrancheMapper gesuchTrancheMapper;
    AbschlussService abschlussService;

    @BeforeEach
    void setup() {
        gesuchTrancheMapper = new GesuchTrancheMapperImpl(Mockito.mock(GesuchFormularMapper.class));
        gesuchTrancheMapper.geschwisterMapper = new GeschwisterMapperImpl();
        abschlussService = Mockito.mock(AbschlussService.class);
        gesuchTrancheMapper.abschlussService = abschlussService;
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

    @Test
    void testPartialUpdateRejectsBerufsbefaehigendAbschluss() {
        final var abschlussId = UUID.randomUUID();
        final var update = createUpdateWithBerufsbefaehigend(false);
        update.getGesuchFormular().getLebenslaufItems().getFirst().setAbschlussId(abschlussId);
        final var abschluss = new Abschluss();
        abschluss.setBerufsbefaehigenderAbschluss(true);
        Mockito.when(abschlussService.requireById(abschlussId)).thenReturn(abschluss);
        final var target = new GesuchTranche();
        target.setGesuchFormular(new GesuchFormular());

        gesuchTrancheMapper.partialUpdate(update, target, true);

        assertThat(
            update.getGesuchFormular().getLebenslaufItems().getFirst().getBerufsbefaehigend(),
            is(true)
        );
    }

    @Test
    void testPartialUpdateClearsBerufsbefaehigendWithoutAbschluss() {
        final var update = createUpdateWithBerufsbefaehigend(true);

        gesuchTrancheMapper.beforeMappingRejectBerufsbefaehigendAbschluss(update);

        assertThat(
            update.getGesuchFormular().getLebenslaufItems().getFirst().getBerufsbefaehigend(),
            nullValue()
        );
        Mockito.verifyNoInteractions(abschlussService);
    }

    @Test
    void testPartialUpdateAcceptsBerufsbefaehigendAbschluss() {
        final var update = createUpdateWithBerufsbefaehigend(true);
        final var target = new GesuchTranche();
        target.setGesuchFormular(new GesuchFormular());

        gesuchTrancheMapper.partialUpdate(update, target, false);

        assertThat(
            update.getGesuchFormular().getLebenslaufItems().getFirst().getBerufsbefaehigend(),
            is(true)
        );
    }

    @Test
    void testRemoveBerufsbefaehigendAbschlussClearsMappedValue() {
        final var lebenslaufItem = new LebenslaufItemDto();
        lebenslaufItem.setBerufsbefaehigend(true);
        final var formular = new GesuchFormularDto();
        formular.setLebenslaufItems(List.of(lebenslaufItem));
        final var tranche = new GesuchTrancheDto();
        tranche.setGesuchFormular(formular);

        gesuchTrancheMapper.removeBerufsbefaehigendAbschluss(tranche);

        assertThat(lebenslaufItem.getBerufsbefaehigend(), nullValue());
    }

    private GesuchTrancheUpdateDto createUpdateWithBerufsbefaehigend(final boolean berufsbefaehigend) {
        final var lebenslaufItem = new LebenslaufItemUpdateDto();
        lebenslaufItem.setBerufsbefaehigend(berufsbefaehigend);
        final var formular = new GesuchFormularUpdateDto();
        formular.setLebenslaufItems(List.of(lebenslaufItem));
        final var tranche = new GesuchTrancheUpdateDto();
        tranche.setGesuchFormular(formular);
        return tranche;
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
