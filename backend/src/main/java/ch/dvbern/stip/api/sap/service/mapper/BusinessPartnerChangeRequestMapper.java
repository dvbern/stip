package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.SenderParmsDelivery;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BusinessPartnerChangeRequestMapper {
    BusinessPartnerChangeMapper businessPartnerMapper = new BusinessPartnerChangeMapper();

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
