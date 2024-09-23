package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.sap.generated.businesspartner.read.BusinessPartnerReadRequest;

public class BusinessPartnerReadFilterParamsMapper {
    public BusinessPartnerReadRequest.FILTERPARMS toFilterParams(String extId) {
        BusinessPartnerReadRequest.FILTERPARMS filter = new BusinessPartnerReadRequest.FILTERPARMS();
        filter.setEXTID(extId);
        return filter;
    }
}
