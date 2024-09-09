package ch.dvbern.stip.api.sap.service.endpoints.businesspartner.change;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;

public class BusinessParnterChangeMapper {
    BusinessPartnerChangeAdresseMapper adresseMapper = new BusinessPartnerChangeAdresseMapper();
    BusinessPartnerChangePersDataMapper persDataMapper = new BusinessPartnerChangePersDataMapper();
    BusinessPartnerChangeOrgDataMapper orgDataMapper = new BusinessPartnerChangeOrgDataMapper();

    BusinessPartnerChangeRequest.BUSINESSPARTNER toBusniessPartner(Auszahlung auszahlung){
        BusinessPartnerChangeRequest.BUSINESSPARTNER businesspartner = new BusinessPartnerChangeRequest.BUSINESSPARTNER();
        BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA persdata = persDataMapper.toBusinessPartnerPERSDATA(auszahlung);
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA orgdata = orgDataMapper.toBusinessPartnerORGDATA(auszahlung);
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS address = adresseMapper.toBusinessPartnerAdresse(auszahlung);
        businesspartner.setPERSDATA(persdata);
        businesspartner.setORGDATA(orgdata);
        businesspartner.getADDRESS().add(address);
        return businesspartner;
    }
}
