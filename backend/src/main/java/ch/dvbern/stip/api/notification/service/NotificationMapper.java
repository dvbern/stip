package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.generated.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface NotificationMapper {
    Notification toEntity(NotificationDto notificationDto);

    @Mapping(source = "gesuch.id", target = "gesuchId")
    NotificationDto toDto(Notification notification);

    Notification partialUpdate(NotificationDto notificationDto, @MappingTarget Notification notification);
}
