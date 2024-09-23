package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;

public class BusinessParnterCreateMapper {
    BusinessPartnerCreateAdresseMapper adresseMapper = new BusinessPartnerCreateAdresseMapper();
    BusinessPartnerCreatePersDataMapper persDataMapper = new BusinessPartnerCreatePersDataMapper();
    BusinessPartnerCreateOrgDataMapper orgDataMapper = new BusinessPartnerCreateOrgDataMapper();

    BusinessPartnerCreateRequest.BUSINESSPARTNER toBusniessPartner(Auszahlung auszahlung){
        BusinessPartnerCreateRequest.BUSINESSPARTNER businesspartner = new BusinessPartnerCreateRequest.BUSINESSPARTNER();
        BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA persdata = persDataMapper.toBusinessPartnerPERSDATA(auszahlung);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA orgdata = orgDataMapper.toBusinessPartnerORGDATA(auszahlung);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS address = adresseMapper.toBusinessPartnerAdresse(auszahlung);
        businesspartner.setPERSDATA(persdata);
        businesspartner.setORGDATA(orgdata);
        businesspartner.getADDRESS().add(address);
        return businesspartner;
    }
}
