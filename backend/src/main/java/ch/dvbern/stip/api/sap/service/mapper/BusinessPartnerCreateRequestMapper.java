package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.SenderParmsDelivery;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BusinessPartnerCreateRequestMapper {
    BusinessParnterCreateMapper businessPartnerMapper = new BusinessParnterCreateMapper();

    public BusinessPartnerCreateRequest toBusinessPartnerCreateRequest(Auszahlung auszahlung, BigInteger sysid, BigDecimal deliveryId){
        BusinessPartnerCreateRequest businessPartnerCreateRequest = new BusinessPartnerCreateRequest();
        BusinessPartnerCreateRequest.BUSINESSPARTNER businesspartner = businessPartnerMapper.toBusniessPartner(auszahlung);
        businessPartnerCreateRequest.setBUSINESSPARTNER(businesspartner);
        SenderParmsDelivery senderParmsDelivery = new SenderParmsDelivery();
        senderParmsDelivery.setSYSID(sysid);
        senderParmsDelivery.setDELIVERYID(deliveryId);
        businessPartnerCreateRequest.setSENDER(senderParmsDelivery);
        return businessPartnerCreateRequest;
    }

}
