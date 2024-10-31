package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.AusbildungDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemDto;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class AusbildungDashboardItemMapper {
    @Inject
    GesuchDashboardItemMapper gesuchDashboardItemMapper;

    @Inject
    GesuchTrancheService gesuchTrancheService;

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

    GesuchDashboardItemDto map(final Gesuch gesuch) {
        final var gesuchTranchen = gesuchTrancheService.getAllTranchenForGesuch(gesuch.getId());
        final var offeneAenderung = gesuchTranchen.stream()
            .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.AENDERUNG)
                && tranche.getStatus().equals(GesuchTrancheStatus.IN_BEARBEITUNG_GS))
            .findFirst().orElse(null);

        final var missingDocumentsTrancheIdAndCount = gesuchTranchen.stream()
            .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.TRANCHE))
            .map(tranche -> ImmutablePair.of(
                tranche.getId(),
                gesuchTrancheService.getRequiredDokumentTypes(tranche.getId()).size()
            ))
            .filter(pair -> pair.getRight() > 0)
            .findFirst();

        return gesuchDashboardItemMapper.toDto(gesuch, offeneAenderung, missingDocumentsTrancheIdAndCount);
    }
}
