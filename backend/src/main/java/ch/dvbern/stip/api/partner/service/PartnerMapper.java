package ch.dvbern.stip.api.partner.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.generated.dto.PartnerDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface PartnerMapper {
    Partner toEntity(PartnerDto partnerDto);

    PartnerDto toDto(Partner partner);

    Partner partialUpdate(PartnerUpdateDto partnerUpdateDto, @MappingTarget Partner partner);

    PartnerUpdateDto toUpdateDto(Partner partner);
}
