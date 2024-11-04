package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = AusbildungDashboardItemMapper.class)
public abstract class FallDashboardItemMapper {
    @Inject
    NotificationService notificationService;

    @Mapping(source = "ausbildungs", target = "ausbildungDashboardItems")
    @Mapping(source = ".", target = "fall")
    public abstract FallDashboardItemDto toDto(final Fall fall);

    @AfterMapping
    protected void setAusbildungDashboardItemsIfNull(
        @MappingTarget final FallDashboardItemDto dto
    ) {
        if (dto.getAusbildungDashboardItems() == null) {
            dto.setAusbildungDashboardItems(new ArrayList<>());
        }
    }

    @AfterMapping
    protected void setNotifications(
        final Fall entity,
        @MappingTarget final FallDashboardItemDto dto
    ) {
        dto.setNotifications(notificationService.getNotificationsForUser(entity.getGesuchsteller().getId()));
    }
}
