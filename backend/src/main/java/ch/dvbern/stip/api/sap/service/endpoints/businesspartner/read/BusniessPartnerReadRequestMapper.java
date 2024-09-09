package ch.dvbern.stip.api.sap.service.endpoints.businesspartner.read;

import java.math.BigInteger;

public class BusniessPartnerReadRequestMapper {
    private BusinessPartnerReadFilterParamsMapper businessPartnerReadFilterParamsMapper = new BusinessPartnerReadFilterParamsMapper();
    public BusinessPartnerReadRequest toBusinessPartnerReadRequest(String extId, BigInteger sysid){
        BusinessPartnerReadRequest businessPartnerReadRequest = new BusinessPartnerReadRequest();
        SenderParms senderParams = new SenderParms();
        senderParams.setSYSID(sysid);
        businessPartnerReadRequest.setSENDER(senderParams);
        businessPartnerReadRequest.setFILTERPARMS(businessPartnerReadFilterParamsMapper.toFilterParams(extId));
        return businessPartnerReadRequest;
    }
}
