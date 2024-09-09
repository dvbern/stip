package ch.dvbern.stip.api.sap.service.endpoints.businesspartner.change;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BusinessPartnerChangeRequestMapper {
    BusinessParnterChangeMapper businessPartnerMapper = new BusinessParnterChangeMapper();

    public BusinessPartnerChangeRequest toBusinessPartnerChangeRequest(Auszahlung auszahlung, BigInteger sysid, BigDecimal deliveryId){
        BusinessPartnerChangeRequest businessPartnerChangeRequest = new BusinessPartnerChangeRequest();
        BusinessPartnerChangeRequest.BUSINESSPARTNER businesspartner = businessPartnerMapper.toBusniessPartner(auszahlung);
        businessPartnerChangeRequest.setBUSINESSPARTNER(businesspartner);
        SenderParmsDelivery senderParmsDelivery = new SenderParmsDelivery();
        senderParmsDelivery.setSYSID(sysid);
        senderParmsDelivery.setDELIVERYID(deliveryId);
        businessPartnerChangeRequest.setSENDER(senderParmsDelivery);
        return businessPartnerChangeRequest;
    }

}
