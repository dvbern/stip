package ch.dvbern.stip.api.auszahlung.sap.businesspartner.read;

public class BusinessPartnerReadFilterParamsMapper {
    public BusinessPartnerReadRequest.FILTERPARMS toFilterParams(String extId) {
        BusinessPartnerReadRequest.FILTERPARMS filter = new BusinessPartnerReadRequest.FILTERPARMS();
        filter.setEXTID(extId);
        return filter;
    }
}
