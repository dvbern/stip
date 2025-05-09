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

package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.AusbildungDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemDto;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class AusbildungDashboardItemMapper {
    @Inject
    GesuchDashboardItemMapper gesuchDashboardItemMapper;

    @Inject
    GesuchTrancheService gesuchTrancheService;

    @Inject
    AusbildungAuthorizer ausbildungAuthorizer;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @Mapping(
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    @Mapping(
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    @Mapping(source = "fall.id", target = "fallId")
    public abstract AusbildungDashboardItemDto toDto(final Ausbildung ausbildung);

    @AfterMapping
    protected void setGesuchDashboardItemsIfNull(
        @MappingTarget final AusbildungDashboardItemDto dto
    ) {
        if (dto.getGesuchs() == null) {
            dto.setGesuchs(new ArrayList<>());
        }
    }

    @AfterMapping
    protected void setAusbildungEditable(
        @MappingTarget final AusbildungDashboardItemDto dto
    ) {
        dto.setEditable(ausbildungAuthorizer.canUpdateCheck(dto.getId()));
    }

    GesuchDashboardItemDto map(final Gesuch gesuch) {
        final var gesuchTranchen = gesuch.getGesuchTranchen();

        final var offeneAenderung = gesuchTranchen.stream()
            .filter(
                tranche -> tranche.getTyp().equals(GesuchTrancheTyp.AENDERUNG)
                && Set
                    .of(
                        GesuchTrancheStatus.IN_BEARBEITUNG_GS,
                        GesuchTrancheStatus.UEBERPRUEFEN,
                        GesuchTrancheStatus.FEHLENDE_DOKUMENTE
                    )
                    .contains(tranche.getStatus())
            )
            .map(gesuchTrancheMapper::toSlimDto)
            .findFirst()
            .orElse(null);

        Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount = Optional.empty();

        final boolean anyFehlendeDokumente = gesuch.getGesuchStatus() != Gesuchstatus.FEHLENDE_DOKUMENTE
        || gesuchTranchen.stream()
            .anyMatch(
                gesuchTranche -> gesuchTranche.getStatus() == GesuchTrancheStatus.FEHLENDE_DOKUMENTE
            );

        if (!anyFehlendeDokumente) {
            missingDocumentsTrancheIdAndCount = gesuchTranchen.stream()
                .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.TRANCHE))
                .map(
                    tranche -> ImmutablePair.of(
                        tranche.getId(),
                        gesuchTrancheService.getAllRequiredDokumentTypes(tranche.getId()).size()
                    )
                )
                .filter(pair -> pair.getRight() > 0)
                .findFirst();
        }

        return gesuchDashboardItemMapper.toDto(gesuch, offeneAenderung, missingDocumentsTrancheIdAndCount);
    }

}
